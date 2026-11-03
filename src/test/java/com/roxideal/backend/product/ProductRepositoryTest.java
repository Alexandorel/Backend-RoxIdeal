package com.roxideal.backend.product;

import com.roxideal.backend.category.Category;
import com.roxideal.backend.category.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// Runs on the real Postgres (Flyway SQL is Postgres-specific); @DataJpaTest rolls back after each test
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTest {

    @Autowired
    ProductRepository repository;

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    void searchAppliesOnlyGivenFiltersAndCombinesThem() {
        Category category = categoryRepository.save(new Category("Zz search test"));
        repository.save(product("Zzalfa rosu", 5, category));
        repository.save(product("Zzalfa albastru", 0, category));
        repository.save(product("Zzbeta rosu", 5, null));

        assertThat(names(repository.search(category.getId(), null, false)))
                .containsExactly("Zzalfa albastru", "Zzalfa rosu");
        assertThat(names(repository.search(category.getId(), null, true)))
                .containsExactly("Zzalfa rosu");
        assertThat(names(repository.search(category.getId(), "ROSU", true)))
                .containsExactly("Zzalfa rosu");
        assertThat(names(repository.search(null, "zzbeta", false)))
                .containsExactly("Zzbeta rosu");
    }

    private static Product product(String name, int stock, Category category) {
        return new Product(name, null, BigDecimal.ONE, stock, null, Unit.BUC, category);
    }

    private static List<String> names(List<Product> products) {
        return products.stream().map(Product::getName).toList();
    }
}
