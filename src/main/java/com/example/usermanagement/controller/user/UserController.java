package com.example.usermanagement.controller.user;

import com.example.usermanagement.dto.user.UserPageResponse;
import com.example.usermanagement.service.UserService;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import java.util.List;

@SecurityRequirement(name = "bearerAuth")
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
            @RequestParam(required = false) String email,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                userService.findAll(email, pageable)
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
