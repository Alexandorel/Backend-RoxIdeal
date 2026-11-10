package com.roxideal.backend.finish;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "finishes")
public class Finish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false, unique = true)
    private String name;

    private String swatch;

    protected Finish() {
        // JPA
    }

    public Finish(String slug, String name, String swatch) {
        this.slug = slug;
        this.name = name;
        this.swatch = swatch;
    }

    public Long getId() {
        return id;
    }

    public String getSlug() {
        return slug;
    }

    public String getName() {
        return name;
    }

    public String getSwatch() {
        return swatch;
    }
}
