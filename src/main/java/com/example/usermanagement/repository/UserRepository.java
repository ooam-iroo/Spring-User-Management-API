package com.example.usermanagement.repository;

import com.example.usermanagement.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("""
        SELECT DISTINCT u
        FROM User u
        LEFT JOIN FETCH u.roles
        WHERE u.email = :email
    """)
    Optional<User> findByEmailWithRoles(@Param("email") String email);

    Page<User> findByEmailContainingIgnoreCase(
            String email,
            Pageable pageable
    );
}