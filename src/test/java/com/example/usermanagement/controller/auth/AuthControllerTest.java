package com.example.usermanagement.controller.auth;

import com.example.usermanagement.dto.auth.RegisterRequest;
import com.example.usermanagement.exception.UserAlreadyExistsException;
import com.example.usermanagement.service.AuthService;
import com.example.usermanagement.security.jwt.JwtService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void register_shouldReturnSuccess_whenRequestIsValid()
            throws Exception {

        doNothing()
                .when(authService)
                .register(org.mockito.ArgumentMatchers.any(RegisterRequest.class));

        String request = """
                {
                    "firstName": "John",
                    "lastName": "Doe",
                    "email": "john@example.com",
                    "password": "password123",
                    "phoneNumber": "09123456789"
                }
                """;

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isCreated())
                .andExpect(
                        content().json("""
                {
                    "message": "User registered successfully"
                }
                """)
                );
    }

    @Test
    void register_shouldReturnBadRequest_whenEmailIsInvalid()
            throws Exception {

        String request = """
            {
                "firstName": "John",
                "lastName": "Doe",
                "email": "invalid-email",
                "password": "password123",
                "phoneNumber": "09123456789"
            }
            """;

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isBadRequest());
    }
}