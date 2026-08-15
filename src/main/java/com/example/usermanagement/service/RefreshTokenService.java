package com.example.usermanagement.service;

import com.example.usermanagement.entity.RefreshToken;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.repository.RefreshTokenRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final long refreshTokenExpiration;

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            @Value("${app.jwt.refresh-token-expiration}") long refreshTokenExpiration
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    @Transactional
    public RefreshToken createRefreshToken(User user) {

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(
                LocalDateTime.now().plusSeconds(refreshTokenExpiration / 1000)
        );

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken findByToken(String token) {

        return refreshTokenRepository
                .findByToken(token)
                .map(this::verifyExpiration)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Refresh token not found"
                        )
                );
    }

    public RefreshToken verifyExpiration(RefreshToken refreshToken) {

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {

            refreshTokenRepository.delete(refreshToken);

            throw new IllegalArgumentException(
                    "Refresh token has expired"
            );
        }

        return refreshToken;
    }

    @Transactional
    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
}