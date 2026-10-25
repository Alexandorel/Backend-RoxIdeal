package com.roxideal.backend.category;

public record CategoryDto(Long id, String name) {

    public static CategoryDto from(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }
}
