package com.roxideal.backend.product;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public ProductDto create(ProductRequest request) {
        Product product = new Product(
                request.name(), request.description(), request.price(),
                request.stock(), request.imageUrl());
        return ProductDto.from(repository.save(product));
    }

    @Transactional
    public ProductDto update(Long id, ProductRequest request) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        product.update(request.name(), request.description(), request.price(),
                request.stock(), request.imageUrl());
        return ProductDto.from(product);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
