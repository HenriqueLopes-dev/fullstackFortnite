package io.github.HenriqueLopes_dev.repository.specs;

import io.github.HenriqueLopes_dev.model.Cosmetic;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class CosmeticSpecs {

    public static Specification<Cosmetic> nameLike(String name, boolean onShop) {
        return (root, query, cb) ->
        {
            if (onShop){
                Join<Object, Object> join = root.join("bundle", JoinType.LEFT);
                Predicate or = cb.or(
                        (cb.like(cb.lower(root.get("name")), "%" + (name).toLowerCase() + "%")),
                        (cb.like(cb.lower(join.get("name")), "%" + (name).toLowerCase() + "%"))
                );
                Predicate notNull = cb.isNotNull(root.get("bundle"));
                return cb.and(or, notNull);
            }
            return cb.like(cb.lower(root.get("name")), "%" + (name).toLowerCase() + "%");
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

    public static Specification<Cosmetic> onShopEqual(Boolean onShop) {
        return (root, query, cb) -> {
            if (onShop == null) return cb.conjunction();
            if (onShop) {
                return cb.isNotNull(root.get("bundle"));
            } else {
                return cb.isNull(root.get("bundle"));
            }
        };
    }

    public static Specification<Cosmetic> onSaleEqual(Boolean onSale) {
        return (root, query, cb) -> {
            Predicate isOnsale = cb.and(
                    cb.isNotNull(root.get("regularPrice")),
                    cb.lessThan(root.get("finalPrice"), root.get("regularPrice"))
            );
            return onSale ?  isOnsale : cb.not(isOnsale);
        };
    }
}
