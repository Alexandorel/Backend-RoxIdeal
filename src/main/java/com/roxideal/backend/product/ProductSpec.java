package com.roxideal.backend.product;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;

@Embeddable
public record ProductSpec(
        @Column(name = "spec_label", nullable = false) @NotBlank String label,
        @Column(name = "spec_value", nullable = false) @NotBlank String value) {
}
