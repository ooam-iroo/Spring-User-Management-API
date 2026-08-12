package com.example.usermanagement.dto.auth;

public record LoginResponse(

        String accessToken,

        String refreshToken,

        String tokenType,

        Long expiresIn
) {
}
