package io.github.HenriqueLopes_dev.repository.specs;

import io.github.HenriqueLopes_dev.model.Cosmetic;
import io.github.HenriqueLopes_dev.model.CosmeticBundle;
import io.github.HenriqueLopes_dev.model.CosmeticBundleRelation;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class CosmeticSpecs {

    public static Specification<Cosmetic> nameLike(String name, boolean onShop) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) return null;

            if (onShop) {
                Join<Object, Object> bundleJoin = root.join("bundles", JoinType.LEFT).join("bundle", JoinType.LEFT);

                Predicate nameMatchesCosmetic = cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
                Predicate nameMatchesBundle = cb.like(cb.lower(bundleJoin.get("name")), "%" + name.toLowerCase() + "%");

                return cb.or(nameMatchesCosmetic, nameMatchesBundle);
            }

            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }


    public static Specification<Cosmetic> typeLike(String type) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("type")), "%" + (type).toLowerCase() + "%");
    }

    public static Specification<Cosmetic> rarityLike(String rarity) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("rarity")), "%" + (rarity).toLowerCase() + "%");
    }

    public static Specification<Cosmetic> inclusionDateEqual(LocalDate inclusionDate) {
        return (root, query, cb) -> cb.equal(root.get("added"), inclusionDate);
    }

    public static Specification<Cosmetic> isNewEqual(Boolean isNew) {
        return (root, query, cb) -> cb.equal(root.get("isNew"), isNew);
    }

    public static Specification<Cosmetic> onShopEqual(boolean onShop) {
        return (root, query, cb) -> {
            // evita duplicatas caso use join
            assert query != null;
            query.distinct(true);

            if (onShop) {
                // inner join garante apenas os que tem bundle
                Join<Cosmetic, CosmeticBundle> join = root.join("bundles", JoinType.INNER);
                return cb.isNotNull(join.get("id")); // sempre true, mas força o INNER JOIN
            } else {
                // não está na loja = não pertence a nenhum bundle
                return cb.isEmpty(root.get("bundles"));
            }
        };
    }

    public static Specification<Cosmetic> onSaleEqual(Boolean onSale) {
        return (root, query, cb) -> {
            // Se onSale for null, retorna a conjunção (sem filtro)
            if (onSale == null) {
                return cb.conjunction();
            }

            // 1. Join do Cosmetic para CosmeticBundleRelation (o campo "bundles" no Cosmetic)
            Join<Cosmetic, CosmeticBundleRelation> relationJoin = root.join("bundles", JoinType.INNER);

            // 2. Join do CosmeticBundleRelation para CosmeticBundle (o campo "bundle" no CosmeticBundleRelation)
            Join<CosmeticBundleRelation, CosmeticBundle> bundleJoin = relationJoin.join("bundle", JoinType.INNER);

            // Garante que o resultado da consulta será distinto (evita duplicatas de Cosmetic)
            assert query != null;
            query.distinct(true);

            // Filtra pelo campo "isOnSale" do CosmeticBundle
            if (onSale) {
                // Se onSale for true, queremos bundles em promoção
                return cb.isTrue(bundleJoin.get("isOnSale"));
            } else {
                // Se onSale for false, queremos bundles que não estão em promoção
                return cb.isFalse(bundleJoin.get("isOnSale"));
            }
        };
    }

}
