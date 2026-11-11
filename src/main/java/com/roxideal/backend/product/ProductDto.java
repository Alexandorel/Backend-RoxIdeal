package com.roxideal.backend.product;

import com.roxideal.backend.category.Category;
import com.roxideal.backend.category.CategoryDto;
import com.roxideal.backend.finish.Finish;
import com.roxideal.backend.finish.FinishDto;

import java.math.BigDecimal;
import java.util.List;


public record ProductDto(
        Long id,
        String name,
        String description,
        BigDecimal price,
        int stock,
        String imageUrl,
        Unit unit,
        CategoryDto category,
        String sku,
        String brand,
        String spec,
        FinishDto finish,
        String tag,
        BigDecimal coverage,
        String care,
        String swatch,
        List<ProductSpec> specs) {

    public static ProductDto from(Product product) {
        Category category = product.getCategory();
        Finish finish = product.getFinish();
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getImageUrl(),
                product.getUnit(),
                category == null ? null : CategoryDto.from(category),
                product.getSku(),
                product.getBrand(),
                product.getSpec(),
                finish == null ? null : FinishDto.from(finish),
                product.getTag(),
                product.getCoverage(),
                product.getCare(),
                product.getSwatch(),
                List.copyOf(product.getSpecs()));
    }
}
