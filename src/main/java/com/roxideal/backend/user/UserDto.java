package com.roxideal.backend.user;

public record UserDto(String firstName, String lastName, String email, Role role) {

    public static UserDto from(User user) {
        return new UserDto(user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole());
    }
}
