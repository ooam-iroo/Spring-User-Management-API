package com.example.usermanagement.controller.admin;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
@ContextConfiguration(classes = {
        AdminController.class,
        AdminControllerSecurityTest.TestSecurityConfig.class
})
class AdminControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void dashboard_shouldRejectRequest_whenUserIsNotAuthenticated()
            throws Exception {

        mockMvc.perform(
                        get("/api/admin/dashboard")
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(
            username = "jolia@example.com",
            roles = "USER"
    )
    void dashboard_shouldRejectUser_whenRoleIsUser()
            throws Exception {

        mockMvc.perform(
                        get("/api/admin/dashboard")
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(
            username = "admin@example.com",
            roles = "ADMIN"
    )
    void dashboard_shouldAllowAdmin_whenRoleIsAdmin()
            throws Exception {

        mockMvc.perform(
                        get("/api/admin/dashboard")
                )
                .andExpect(status().isOk());
    }

    @Configuration
    @EnableMethodSecurity
    static class TestSecurityConfig {

        @Bean
        SecurityFilterChain securityFilterChain(
                HttpSecurity http
        ) throws Exception {

            return http
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth
                            .anyRequest().authenticated()
                    )
                    .build();
        }
    }
}