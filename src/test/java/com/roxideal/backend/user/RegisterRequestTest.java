package com.roxideal.backend.user;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RegisterRequestTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private Set<String> invalidFields(RegisterRequest request) {
        return validator.validate(request).stream()
                .map(ConstraintViolation::getPropertyPath)
                .map(Object::toString)
                .collect(Collectors.toSet());
    }

    private RegisterRequest request(String firstName, String phone, String password) {
        return new RegisterRequest(firstName, "Pop", "ana@mail.com", phone, password);
    }

    @Test
    void validRequestHasNoErrors() {
        assertEquals(Set.of(), invalidFields(request("Ana", "0722123456", "parola123")));
    }

    @Test
    void phoneWithSpacesIsCleanedAndValid() {
        RegisterRequest request = request("Ana", "0722 123 456", "parola123");

        assertEquals("0722123456", request.phone());
        assertEquals(Set.of(), invalidFields(request));
    }

    @Test
    void landlineWithCountryCodeIsValid() {
        assertEquals(Set.of(), invalidFields(request("Ana", "+40212345678", "parola123")));
    }

    @Test
    void phoneWithInvalidPrefixIsRejected() {
        assertEquals(Set.of("phone"), invalidFields(request("Ana", "0822123456", "parola123")));
    }

    @Test
    void passwordShorterThan8IsRejected() {
        assertEquals(Set.of("password"), invalidFields(request("Ana", "0722123456", "parola1")));
    }

    @Test
    void passwordOver72BytesIsRejected() {
        assertEquals(Set.of("password"), invalidFields(request("Ana", "0722123456", "ă".repeat(40))));
    }

    @Test
    void firstNameOver50CharsIsRejected() {
        assertEquals(Set.of("firstName"), invalidFields(request("A".repeat(51), "0722123456", "parola123")));
    }
}
