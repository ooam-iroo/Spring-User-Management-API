package com.example.usermanagement.controller.user;

import com.example.usermanagement.service.UserService;
import com.example.usermanagement.dto.user.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController (
            UserService userService
    ) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public String getCurrentUser(
            Authentication authentication
    ){
        return authentication.getName();
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }
}
