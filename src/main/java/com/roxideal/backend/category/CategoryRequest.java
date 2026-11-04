package com.roxideal.backend.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CategoryRequest(
        @NotBlank String name,
        @NotBlank @Pattern(regexp = "[a-z0-9-]+") String slug,
        @NotBlank String title) {
}
