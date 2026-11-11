package com.roxideal.backend.product;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.List;

public record ProductRequest(
        @NotBlank String name,
        String description,
        @Positive BigDecimal price,
        @PositiveOrZero int stock,
        String imageUrl,
        @NotNull Unit unit,
        @NotNull Long categoryId,
        @NotBlank String sku,
        String brand,
        String spec,
        Long finishId,
        String tag,
        @Positive BigDecimal coverage,
        String care,
        String swatch,
        List<@Valid ProductSpec> specs) {
}
