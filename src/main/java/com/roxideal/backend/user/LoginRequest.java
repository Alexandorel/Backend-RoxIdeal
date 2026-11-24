package com.roxideal.backend.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Emailul este obligatoriu")
        @Email(message = "Email invalid")
        String email,

        @NotBlank(message = "Parola este obligatorie")
        @MaxBytes(value = 72, message = "Parola este prea lungă")
        String password) {

    public LoginRequest {
        email = User.normalizeEmail(email);
    }
}
