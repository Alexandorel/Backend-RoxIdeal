package com.roxideal.backend.product;

import com.roxideal.backend.category.Category;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 2000)
    private String description;

    private BigDecimal price;

    @Column(nullable = false)
    private int stock;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private Unit unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "fk_products_category"))
    private Category category;

    @Column(nullable = false, unique = true)
    private String sku;

    private String brand;

    private String spec;

    private String finish;

    private String tag;

    @Column(precision = 6, scale = 3)
    private BigDecimal coverage;

    @Column(length = 2000)
    private String care;

    private String swatch;

    @ElementCollection
    @CollectionTable(name = "product_specs", joinColumns = @JoinColumn(name = "product_id",
            foreignKey = @ForeignKey(name = "fk_product_specs_product")))
    @OrderColumn(name = "position")
    private List<ProductSpec> specs = new ArrayList<>();

    protected Product() {
        // JPA
    }

    public Product(String name, String description, BigDecimal price, int stock, String imageUrl,
                   Unit unit, Category category, String sku, String brand, String spec, String finish,
                   String tag, BigDecimal coverage, String care, String swatch, List<ProductSpec> specs) {
        update(name, description, price, stock, imageUrl, unit, category,
                sku, brand, spec, finish, tag, coverage, care, swatch, specs);
    }

    public void update(String name, String description, BigDecimal price, int stock, String imageUrl,
                       Unit unit, Category category, String sku, String brand, String spec, String finish,
                       String tag, BigDecimal coverage, String care, String swatch, List<ProductSpec> specs) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.unit = unit;
        this.category = category;
        this.sku = sku;
        this.brand = brand;
        this.spec = spec;
        this.finish = finish;
        this.tag = tag;
        this.coverage = coverage;
        this.care = care;
        this.swatch = swatch;
        this.specs.clear();
        if (specs != null) {
            this.specs.addAll(specs);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Unit getUnit() {
        return unit;
    }

    public Category getCategory() {
        return category;
    }

    public String getSku() {
        return sku;
    }

    public String getBrand() {
        return brand;
    }

    public String getSpec() {
        return spec;
    }

    public String getFinish() {
        return finish;
    }

    public String getTag() {
        return tag;
    }

    public BigDecimal getCoverage() {
        return coverage;
    }

    public String getCare() {
        return care;
    }

    public String getSwatch() {
        return swatch;
    }

    public List<ProductSpec> getSpecs() {
        return specs;
    }
}
