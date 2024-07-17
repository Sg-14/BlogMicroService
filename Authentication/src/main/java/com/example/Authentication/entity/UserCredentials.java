package com.example.Authentication.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "user_credentials")
public class UserCredentials {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String email;
    private String password;

    @ElementCollection
    @CollectionTable(name = "old_passwords", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    @Column(name = "old_password")
    private Set<String> oldPassword = new HashSet<>();
}
