package com.example.usermanagement.dto.user;

public record UserResponse(
        Long id,

        String firstName,

        String lastName,

        String email,

        String phoneNumber,

        boolean enabled
) {
}
