package com.example.usermanagement.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 8)
        String password,

        @NotBlank
        @Size(min = 10)
        String phoneNumber
) {
}
