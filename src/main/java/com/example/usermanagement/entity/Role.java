package com.example.usermanagement.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
public class Role extends BaseEntity {


    @Column(
            nullable = false,
            unique = true,
            length = 50
    )
    private String name;


    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

}