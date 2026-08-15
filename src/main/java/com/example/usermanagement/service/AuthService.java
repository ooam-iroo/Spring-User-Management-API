package com.example.usermanagement.service;

import com.example.usermanagement.dto.auth.RefreshTokenRequest;
import com.example.usermanagement.dto.auth.RegisterRequest;
import com.example.usermanagement.entity.RefreshToken;
import com.example.usermanagement.entity.Role;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.entity.enums.RoleName;
import com.example.usermanagement.exception.UserAlreadyExistsException;
import com.example.usermanagement.repository.RoleRepository;
import com.example.usermanagement.repository.UserRepository;
import com.example.usermanagement.dto.auth.LoginRequest;
import com.example.usermanagement.dto.auth.LoginResponse;
import com.example.usermanagement.security.jwt.JwtService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            RefreshTokenService refreshTokenService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException(
                    "User already exists with email: " + request.email()
            );
        }

        Role userRole = roleRepository
                .findByName(RoleName.USER)
                .orElseThrow(() -> new IllegalStateException(
                        "Default USER role not found"
                ));

        User user = new User();

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPhoneNumber(request.phoneNumber());
        user.setPassword(
                passwordEncoder.encode(request.password())
        );
        user.setEnabled(true);

        user.getRoles().add(userRole);

        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.email(),
                                request.password()
                        )
                );

        User user = userRepository
                .findByEmail(request.email())
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found")
                );

        String accessToken =
                jwtService.generateAccessToken(
                        authentication.getName()
                );

        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user);

        return new LoginResponse(
                accessToken,
                refreshToken.getToken(),
                "Bearer",
                900L
        );
    }

    @Transactional
    public LoginResponse refreshToken(RefreshTokenRequest request) {

        RefreshToken refreshToken =
                refreshTokenService.findByToken(
                        request.refreshToken()
                );

        User user = refreshToken.getUser();

        String accessToken =
                jwtService.generateAccessToken(
                        user.getEmail()
                );

        return new LoginResponse(
                accessToken,
                refreshToken.getToken(),
                "Bearer",
                900L
        );
    }
}
