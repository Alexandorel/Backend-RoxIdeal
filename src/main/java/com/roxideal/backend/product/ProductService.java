package com.roxideal.backend.product;

import com.roxideal.backend.category.Category;
import com.roxideal.backend.category.CategoryNotFoundException;
import com.roxideal.backend.category.CategoryRepository;
import com.roxideal.backend.finish.Finish;
import com.roxideal.backend.finish.FinishNotFoundException;
import com.roxideal.backend.finish.FinishRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;
    private final FinishRepository finishRepository;

    public ProductService(ProductRepository repository, CategoryRepository categoryRepository,
                          FinishRepository finishRepository) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
        this.finishRepository = finishRepository;
    }

    public List<ProductDto> search(Long categoryId, String q, boolean inStock) {
        String text = StringUtils.hasText(q) ? q.trim() : null;
        return repository.search(categoryId, text, inStock).stream().map(ProductDto::from).toList();
    }

    public ProductDto findById(Long id) {
        return repository.findById(id)
                .map(ProductDto::from)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public ProductDto create(ProductRequest request) {
        if (repository.existsBySku(request.sku())) {
            throw new DuplicateSkuException(request.sku());
        }
        Product product = new Product(
                request.name(), request.description(), request.price(),
                request.stock(), request.imageUrl(),
                request.unit(), findCategory(request.categoryId()),
                request.sku(), request.brand(), request.spec(), findFinish(request.finishId()),
                request.tag(), request.coverage(), request.care(), request.swatch(), request.specs());
        return ProductDto.from(repository.save(product));
    }

    @Transactional
    public ProductDto update(Long id, ProductRequest request) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        if (!product.getSku().equals(request.sku()) && repository.existsBySku(request.sku())) {
            throw new DuplicateSkuException(request.sku());
        }
        product.update(request.name(), request.description(), request.price(),
                request.stock(), request.imageUrl(),
                request.unit(), findCategory(request.categoryId()),
                request.sku(), request.brand(), request.spec(), findFinish(request.finishId()),
                request.tag(), request.coverage(), request.care(), request.swatch(), request.specs());
        return ProductDto.from(product);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        repository.deleteById(id);
    }

    private Category findCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    private Finish findFinish(Long id) {
        if (id == null) {
            return null;
        }
        return finishRepository.findById(id)
                .orElseThrow(() -> new FinishNotFoundException(id));
    }
}
