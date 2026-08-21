package com.example.usermanagement.controller.user;

import com.example.usermanagement.service.UserService;

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

import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ContextConfiguration(classes = {
        UserController.class,
        UserAdminControllerSecurityTest.TestSecurityConfig.class
})
class UserAdminControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    @WithMockUser(
            username = "jolia@example.com",
            roles = "USER"
    )
    void deleteUser_shouldRejectUser_whenRoleIsUser()
            throws Exception {

        mockMvc.perform(
                        delete("/api/users/1")
                                .with(csrf())
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(
            username = "admin@example.com",
            roles = "ADMIN"
    )
    void deleteUser_shouldAllowAdmin_whenRoleIsAdmin()
            throws Exception {

        doNothing()
                .when(userService)
                .deleteById(1L);

        mockMvc.perform(
                        delete("/api/users/1")
                                .with(csrf())
                )
                .andExpect(status().isNoContent());
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