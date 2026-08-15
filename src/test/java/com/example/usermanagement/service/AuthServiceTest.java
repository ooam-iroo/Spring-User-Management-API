package com.example.usermanagement.service;

import com.example.usermanagement.dto.auth.LoginRequest;
import com.example.usermanagement.dto.auth.LoginResponse;
import com.example.usermanagement.dto.auth.RefreshTokenRequest;
import com.example.usermanagement.dto.auth.RegisterRequest;
import com.example.usermanagement.entity.RefreshToken;
import com.example.usermanagement.entity.Role;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.exception.UserAlreadyExistsException;
import com.example.usermanagement.repository.RoleRepository;
import com.example.usermanagement.repository.UserRepository;
import com.example.usermanagement.entity.enums.RoleName;

import com.example.usermanagement.security.jwt.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenService refreshTokenService;


    @Test
    void register_shouldCreateUser_whenEmailIsUnique() {

        // Arrange

        RegisterRequest request = new RegisterRequest(
                "John",
                "Doe",
                "john@example.com",
                "password123",
                "09123456789"
        );

        when(userRepository.existsByEmail("john@example.com"))
                .thenReturn(false);

        Role userRole = new Role();
        userRole.setName(RoleName.USER);

        when(roleRepository.findByName(RoleName.USER))
                .thenReturn(Optional.of(userRole));

        when(passwordEncoder.encode("password123"))
                .thenReturn("hashed-password");


        // Act

        authService.register(request);


        // Assert

        ArgumentCaptor<User> userCaptor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertEquals("John", savedUser.getFirstName());
        assertEquals("Doe", savedUser.getLastName());
        assertEquals("john@example.com", savedUser.getEmail());
        assertEquals("09123456789", savedUser.getPhoneNumber());

        assertEquals(
                "hashed-password",
                savedUser.getPassword()
        );

        assertFalse(savedUser.getEnabled());

        assertTrue(
                savedUser.getRoles().contains(userRole)
        );

        verify(userRepository).existsByEmail("john@example.com");
        verify(roleRepository).findByName(RoleName.USER);
        verify(passwordEncoder).encode("password123");
    }

    @Test
    void register_shouldThrowException_whenEmailAlreadyExists() {

        // Arrange

        RegisterRequest request = new RegisterRequest(
                "John",
                "Doe",
                "john@example.com",
                "password123",
                "09123456789"
        );

        when(userRepository.existsByEmail("john@example.com"))
                .thenReturn(true);


        // Act & Assert

        UserAlreadyExistsException exception = assertThrows(
                UserAlreadyExistsException.class,
                () -> authService.register(request)
        );


        // Assert

        assertEquals(
                "User already exists with email: john@example.com",
                exception.getMessage()
        );


        // Verify

        verify(userRepository, never()).save(any());
    }

    @Test
    void login_shouldReturnAccessToken_whenCredentialsAreValid() {

        // Arrange
        LoginRequest request = new LoginRequest(
                "john@example.com",
                "password123"
        );

        User user = new User();
        user.setEmail("john@example.com");

        Authentication authentication =
                mock(Authentication.class);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("refresh-token");
        refreshToken.setUser(user);

        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);

        when(authentication.getName())
                .thenReturn("john@example.com");

        when(userRepository.findByEmail("john@example.com"))
                .thenReturn(Optional.of(user));

        when(refreshTokenService.createRefreshToken(user))
                .thenReturn(refreshToken);

        when(jwtService.generateAccessToken("john@example.com"))
                .thenReturn("access-token");

        // Act
        LoginResponse response =
                authService.login(request);

        // Assert
        assertEquals("access-token", response.accessToken());
        assertEquals("refresh-token", response.refreshToken());
        assertEquals("Bearer", response.tokenType());

        verify(authenticationManager)
                .authenticate(any());

        verify(userRepository)
                .findByEmail("john@example.com");

        verify(refreshTokenService)
                .createRefreshToken(user);

        verify(jwtService)
                .generateAccessToken("john@example.com");
    }

    @Test
    void refreshToken_shouldReturnNewAccessToken() {

        RefreshTokenRequest request =
                new RefreshTokenRequest("refresh-token");

        User user = new User();
        user.setEmail("john@example.com");

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("refresh-token");
        refreshToken.setUser(user);

        when(refreshTokenService.findByToken("refresh-token"))
                .thenReturn(refreshToken);

        when(jwtService.generateAccessToken("john@example.com"))
                .thenReturn("new-access-token");

        LoginResponse response =
                authService.refreshToken(request);

        assertEquals(
                "new-access-token",
                response.accessToken()
        );

        assertEquals(
                "refresh-token",
                response.refreshToken()
        );

        assertEquals(
                "Bearer",
                response.tokenType()
        );

        verify(refreshTokenService)
                .findByToken("refresh-token");

        verify(jwtService)
                .generateAccessToken("john@example.com");
    }
}