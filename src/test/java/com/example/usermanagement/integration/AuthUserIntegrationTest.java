package com.example.usermanagement.integration;

import com.example.usermanagement.dto.auth.LoginRequest;
import com.example.usermanagement.dto.auth.RegisterRequest;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthUserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.findByEmail("integration@example.com")
                .ifPresent(userRepository::delete);
    }

    @Test
    void register_shouldCreateUser() throws Exception {

        RegisterRequest request = new RegisterRequest(
                "Integration",
                "Test",
                "integration@example.com",
                "password123",
                "09120000000"
        );

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated());

        User user = userRepository
                .findByEmail("integration@example.com")
                .orElseThrow();

        org.junit.jupiter.api.Assertions.assertEquals(
                "integration@example.com",
                user.getEmail()
        );
    }

    @Test
    void login_shouldReturnAccessToken() throws Exception {

        createUser();

        LoginRequest request = new LoginRequest(
                "integration@example.com",
                "password123"
        );

        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    @Test
    void protectedEndpoint_shouldRejectRequestWithoutToken()
            throws Exception {

        mockMvc.perform(
                        get("/api/users/me")
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void protectedEndpoint_shouldAllowRequestWithValidToken()
            throws Exception {

        createUser();

        String token = loginAndGetToken();

        mockMvc.perform(
                        get("/api/users/me")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(status().isOk());
    }

    @Test
    void usersEndpoint_shouldAllowAuthenticatedUser()
            throws Exception {

        createUser();

        String token = loginAndGetToken();

        mockMvc.perform(
                        get("/api/users")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(status().isOk());
    }

    private void createUser() {

        RegisterRequest request = new RegisterRequest(
                "Integration",
                "Test",
                "integration@example.com",
                "password123",
                "09120000000"
        );

        try {
            mockMvc.perform(
                    post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
            ).andExpect(status().isCreated());

        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private String loginAndGetToken() throws Exception {

        LoginRequest request = new LoginRequest(
                "integration@example.com",
                "password123"
        );

        String response = mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode json = objectMapper.readTree(response);

        return json.get("accessToken").asText();
    }
}