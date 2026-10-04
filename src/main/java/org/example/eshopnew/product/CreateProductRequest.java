package org.example.eshopnew.product;

import java.math.BigDecimal;

public record CreateProductRequest(
        String name,
        BigDecimal price,
        int stock
) {
}
