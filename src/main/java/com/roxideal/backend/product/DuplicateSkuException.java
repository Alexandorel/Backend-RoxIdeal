package com.roxideal.backend.product;

public class DuplicateSkuException extends RuntimeException {

    public DuplicateSkuException(String sku) {
        super("Product SKU already exists: " + sku);
    }
}
