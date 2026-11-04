package com.roxideal.backend.category;

public record CategoryDto(Long id, String name, String slug, String title) {

    public static CategoryDto from(Category category) {
        return new CategoryDto(category.getId(), category.getName(), category.getSlug(), category.getTitle());
    }
}
