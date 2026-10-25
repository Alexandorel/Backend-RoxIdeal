package com.roxideal.backend.category;

public class DuplicateCategoryException extends RuntimeException {

    public DuplicateCategoryException(String name) {
        super("Category already exists: " + name);
    }
}
