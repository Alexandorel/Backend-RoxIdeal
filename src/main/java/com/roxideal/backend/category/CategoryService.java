package com.roxideal.backend.category;

import com.roxideal.backend.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository repository;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository repository, ProductRepository productRepository) {
        this.repository = repository;
        this.productRepository = productRepository;
    }

    public List<CategoryDto> findAll() {
        return repository.findAll().stream().map(CategoryDto::from).toList();
    }

    public CategoryDto findById(Long id) {
        return repository.findById(id)
                .map(CategoryDto::from)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    public CategoryDto create(CategoryRequest request) {
        if (repository.existsByName(request.name())) {
            throw new DuplicateCategoryException(request.name());
        }
        return CategoryDto.from(repository.save(new Category(request.name())));
    }

    @Transactional
    public CategoryDto update(Long id, CategoryRequest request) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        if (!category.getName().equals(request.name()) && repository.existsByName(request.name())) {
            throw new DuplicateCategoryException(request.name());
        }
        category.update(request.name());
        return CategoryDto.from(category);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new CategoryNotFoundException(id);
        }
        if (productRepository.existsByCategoryId(id)) {
            throw new CategoryInUseException(id);
        }
        repository.deleteById(id);
    }
}
