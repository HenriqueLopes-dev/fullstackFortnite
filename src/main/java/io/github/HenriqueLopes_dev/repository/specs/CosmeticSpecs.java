package io.github.HenriqueLopes_dev.repository.specs;

import io.github.HenriqueLopes_dev.model.Cosmetic;
import io.github.HenriqueLopes_dev.model.CosmeticBundle;
import io.github.HenriqueLopes_dev.model.CosmeticBundleRelation;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CosmeticSpecs {

    public static Specification<Cosmetic> nameLike(String name, Boolean onShop) {
        return (root, query, cb) -> {

            if (name == null || name.isBlank()) {
                return null;
            }

            assert query != null;
            query.distinct(true);

            Predicate nameMatchesCosmetic =
                    cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");

            if (onShop != null && onShop) {
                Join<Cosmetic, CosmeticBundleRelation> relationJoin =
                        root.join("bundles", JoinType.INNER);

                Join<CosmeticBundleRelation, CosmeticBundle> bundleJoin =
                        relationJoin.join("bundle", JoinType.INNER);

                Predicate nameMatchesBundle =
                        cb.like(cb.lower(bundleJoin.get("name")), "%" + name.toLowerCase() + "%");

                return cb.or(nameMatchesCosmetic, nameMatchesBundle);
            }

            return nameMatchesCosmetic;
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
        return (root, query, cb) ->
                cb.between(
                        root.get("added"),
                        inclusionDate.atStartOfDay(),
                        inclusionDate.plusDays(1).atStartOfDay().minusNanos(1)
                );
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
            if (onSale == null) {
                return cb.conjunction();
            }

            Join<Cosmetic, CosmeticBundleRelation> relationJoin = root.join("bundles", JoinType.INNER);
            Join<CosmeticBundleRelation, CosmeticBundle> bundleJoin = relationJoin.join("bundle", JoinType.INNER);

            assert query != null;
            query.distinct(true);

            if (onSale) {
                return cb.isTrue(bundleJoin.get("isOnSale"));
            }
            return cb.isFalse(bundleJoin.get("isOnSale"));

        };
    }

}
