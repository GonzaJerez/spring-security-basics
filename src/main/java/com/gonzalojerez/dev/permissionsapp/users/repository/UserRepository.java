package com.gonzalojerez.dev.permissionsapp.users.repository;

import com.gonzalojerez.dev.permissionsapp.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    @Transactional
    Optional<User> findByEmail(String email);

    @Transactional
    Optional<User> findByUsername(String username);
}
