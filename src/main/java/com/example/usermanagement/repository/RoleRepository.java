package com.example.usermanagement.repository;

import com.example.usermanagement.entity.Role;
import com.example.usermanagement.entity.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName name);

}
