package com.example.usermanagement.service;

import com.example.usermanagement.exception.UserNotFoundException;
import com.example.usermanagement.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void deleteById_shouldDeleteUser_whenUserExists() {

        long userId = 1L;

        when(userRepository.existsById(userId))
                .thenReturn(true);

        userService.deleteById(userId);

        verify(userRepository)
                .existsById(userId);

        verify(userRepository)
                .deleteById(userId);
    }

    @Test
    void deleteById_shouldThrowException_whenUserDoesNotExist() {

        long userId = 999L;

        when(userRepository.existsById(userId))
                .thenReturn(false);

        assertThrows(
                UserNotFoundException.class,
                () -> userService.deleteById(userId)
        );

        verify(userRepository)
                .existsById(userId);

        verify(userRepository, never())
                .deleteById(anyLong());
    }
}