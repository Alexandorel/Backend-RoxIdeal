package com.roxideal.backend.user;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoginRequestTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private Set<String> invalidFields(LoginRequest request) {
        return validator.validate(request).stream()
                .map(ConstraintViolation::getPropertyPath)
                .map(Object::toString)
                .collect(Collectors.toSet());
    }

    @Test
    void oldShortPasswordIsAccepted() {
        assertEquals(Set.of(), invalidFields(new LoginRequest("ana@mail.com", "parola")));
    }

    @Test
    void passwordOver72BytesIsRejected() {
        assertEquals(Set.of("password"), invalidFields(new LoginRequest("ana@mail.com", "ă".repeat(40))));
    }
}
