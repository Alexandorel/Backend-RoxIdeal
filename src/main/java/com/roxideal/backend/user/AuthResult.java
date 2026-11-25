package com.roxideal.backend.user;

public record AuthResult(UserDto user, String token) {
}
