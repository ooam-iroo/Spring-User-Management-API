package com.example.usermanagement.service;

import com.example.usermanagement.dto.user.UserResponse;
import com.example.usermanagement.exception.UserNotFoundException;
import com.example.usermanagement.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService (
            UserRepository userRepository
    ){
        this.userRepository = userRepository;
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll()
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
