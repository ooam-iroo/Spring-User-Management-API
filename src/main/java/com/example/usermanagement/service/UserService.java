package com.example.usermanagement.service;

import com.example.usermanagement.dto.user.UserPageResponse;
import com.example.usermanagement.dto.user.UserResponse;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.exception.UserNotFoundException;
import com.example.usermanagement.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService (
            UserRepository userRepository
    ){
        this.userRepository = userRepository;
    }

    public UserPageResponse findAll(
            String email,
            Pageable pageable) {

        Page<User> userPage;

        if (email != null && !email.isBlank()) {
            userPage = userRepository
                    .findByEmailContainingIgnoreCase(email, pageable);
        } else {
            userPage = userRepository.findAll(pageable);
        }

        List<UserResponse> users = userPage.getContent()
                .stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getPhoneNumber(),
                        user.getEnabled()
                ))
                .toList();

        return new UserPageResponse(
                users,
                userPage.getNumber(),
                userPage.getSize(),
                userPage.getTotalElements(),
                userPage.getTotalPages()
        );
    }

    @Transactional
    public void deleteById(long id){
        if (!userRepository.existsById(id)){
            throw new UserNotFoundException(
              "User not found with id: " + id
            );
        }
        userRepository.deleteById(id);
    }
}
