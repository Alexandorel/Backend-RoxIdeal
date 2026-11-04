package com.roxideal.backend.product;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @EntityGraph(attributePaths = {"category", "specs"})
    @Query("""
            select p from Product p
            where (:categoryId is null or p.category.id = :categoryId)
              and (:q is null or lower(p.name) like lower(concat('%', :q, '%')))
              and (:inStock = false or p.stock > 0)
            order by p.name
            """)
    List<Product> search(@Param("categoryId") Long categoryId,
                         @Param("q") String q,
                         @Param("inStock") boolean inStock);

    @EntityGraph(attributePaths = {"category", "specs"})
    Optional<Product> findById(Long id);

    boolean existsByCategoryId(Long categoryId);

    boolean existsBySku(String sku);
}
