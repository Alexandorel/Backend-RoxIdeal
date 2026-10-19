package com.roxideal.backend.product;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<ProductDto> findAll() {
        return repository.findAll().stream().map(ProductDto::from).toList();
    }

    public ProductDto findById(Long id) {
        return repository.findById(id)
                .map(ProductDto::from)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
