package com.example.Authentication.repository;

import com.example.Authentication.entity.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCredentialsRepository extends JpaRepository<UserCredentials, Long> {
    Optional<UserCredentials> findByEmail(String email);
    Optional<UserCredentials> findByUsernameOrEmail(String username, String email);
    Optional<UserCredentials> findByUsername(String username);

    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
