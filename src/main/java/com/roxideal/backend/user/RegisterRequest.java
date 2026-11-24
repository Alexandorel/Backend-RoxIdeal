package com.roxideal.backend.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Prenumele este obligatoriu")
        @Size(max = 50, message = "Prenumele poate avea maxim 50 de caractere")
        String firstName,

        @NotBlank(message = "Numele este obligatoriu")
        @Size(max = 50, message = "Numele poate avea maxim 50 de caractere")
        String lastName,

        @NotBlank(message = "Emailul este obligatoriu")
        @Email(message = "Email invalid")
        @Size(max = 254, message = "Emailul poate avea maxim 254 de caractere")
        String email,

        @NotBlank(message = "Telefonul este obligatoriu")
        @Pattern(regexp = "^(\\+40|0)[237]\\d{8}$", message = "Număr de telefon invalid")
        String phone,

        @NotBlank(message = "Parola este obligatorie")
        @Size(min = 8, message = "Parola trebuie să aibă minim 8 caractere")
        @MaxBytes(value = 72, message = "Parola este prea lungă")
        String password) {

    public RegisterRequest {
        email = User.normalizeEmail(email);
        phone = phone == null ? null : phone.replaceAll("[\\s.-]", "");
    }
}
