package org.example.eshopnew.product;

import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public final class ProductSpecifications {

    private ProductSpecifications() {
    }

    // --- name ---

    public static Specification<Product> nameEquals(String name) {
        return (root, query, cb) -> cb.equal(cb.lower(root.get("name")), name.toLowerCase());
    }

    public static Specification<Product> nameContains(String name) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    // --- price ---

    public static Specification<Product> priceEquals(BigDecimal price) {
        return (root, query, cb) -> cb.equal(root.get("price"), price);
    }

    public static Specification<Product> priceGreaterThanOrEqual(BigDecimal minPrice) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Product> priceLessThanOrEqual(BigDecimal maxPrice) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    public static Specification<Product> priceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, cb) -> cb.between(root.get("price"), minPrice, maxPrice);
    }

    // --- stock ---

    public static Specification<Product> stockEquals(int stock) {
        return (root, query, cb) -> cb.equal(root.get("stock"), stock);
    }

    public static Specification<Product> stockGreaterThanOrEqual(int minStock) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("stock"), minStock);
    }

    public static Specification<Product> stockLessThanOrEqual(int maxStock) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("stock"), maxStock);
    }

    public static Specification<Product> inStock() {
        return (root, query, cb) -> cb.greaterThan(root.get("stock"), 0);
    }

    public static Specification<Product> outOfStock() {
        return (root, query, cb) -> cb.equal(root.get("stock"), 0);
    }
}
