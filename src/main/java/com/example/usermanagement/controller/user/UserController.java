package com.example.usermanagement.controller.user;

import com.example.usermanagement.dto.user.UserPageResponse;
import com.example.usermanagement.service.UserService;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<UserPageResponse> findAll(
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                userService.findAll(pageable)
        );
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminOnly() {
        return ResponseEntity.ok("Hello Admin");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(
            @PathVariable Long id
    ) {
        userService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
