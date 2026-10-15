package com.roxideal.backend.product;

import java.math.BigDecimal;


public record ProductDto(
        Long id,
        String name,
        String description,
        BigDecimal price,
        int stock,
        String imageUrl) {

    public static ProductDto from(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getImageUrl());
    }
}
