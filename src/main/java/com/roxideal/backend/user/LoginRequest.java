package com.roxideal.backend.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank @Email String email,
        @NotBlank String password) {

    public LoginRequest {
        email = User.normalizeEmail(email);
    }
}
