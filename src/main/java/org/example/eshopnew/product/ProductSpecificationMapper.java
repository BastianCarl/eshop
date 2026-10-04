package org.example.eshopnew.product;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class ProductSpecificationMapper {

    public Specification<Product> toSpecification(ProductFilter filter) {
        Specification<Product> spec = Specification.unrestricted();
        if (filter.name() != null) {
            spec = spec.and(ProductSpecifications.nameContains(filter.name()));
        }
        if (filter.minPrice() != null) {
            spec = spec.and(ProductSpecifications.priceGreaterThanOrEqual(filter.minPrice()));
        }
        if (filter.maxPrice() != null) {
            spec = spec.and(ProductSpecifications.priceLessThanOrEqual(filter.maxPrice()));
        }
        if (filter.inStock() != null) {
            spec = spec.and(filter.inStock()
                    ? ProductSpecifications.inStock()
                    : ProductSpecifications.outOfStock());
        }
        return spec;
    }
}
