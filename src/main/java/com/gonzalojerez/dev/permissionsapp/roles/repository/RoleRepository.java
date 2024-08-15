package com.gonzalojerez.dev.permissionsapp.roles.repository;

import com.gonzalojerez.dev.permissionsapp.roles.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
}
