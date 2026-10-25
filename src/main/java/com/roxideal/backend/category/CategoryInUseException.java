package com.roxideal.backend.category;

public class CategoryInUseException extends RuntimeException {

    public CategoryInUseException(Long id) {
        super("Category " + id + " still has products");
    }
}
