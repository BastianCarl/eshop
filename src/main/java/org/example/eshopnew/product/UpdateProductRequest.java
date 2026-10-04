package org.example.eshopnew.product;

import java.math.BigDecimal;

public record UpdateProductRequest(
        String name,
        BigDecimal price,
        int stock
) {
}
