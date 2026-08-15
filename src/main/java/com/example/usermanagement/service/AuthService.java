package com.example.usermanagement.service;

import com.example.usermanagement.dto.auth.RegisterRequest;
import com.example.usermanagement.entity.Role;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.entity.enums.RoleName;
import com.example.usermanagement.exception.UserAlreadyExistsException;
import com.example.usermanagement.repository.RoleRepository;
import com.example.usermanagement.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
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
        user.setEnabled(false);

        user.getRoles().add(userRole);

        userRepository.save(user);
    }
}
