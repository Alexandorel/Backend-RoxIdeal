package com.roxideal.backend.product;

import com.roxideal.backend.category.Category;
import com.roxideal.backend.category.CategoryDto;

import java.math.BigDecimal;


public record ProductDto(
        Long id,
        String name,
        String description,
        BigDecimal price,
        int stock,
        String imageUrl,
        Unit unit,
        CategoryDto category) {

    public static ProductDto from(Product product) {
        Category category = product.getCategory();
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getImageUrl(),
                product.getUnit(),
                category == null ? null : CategoryDto.from(category));
    }
}
