package com.gonzalojerez.dev.permissionsapp.roles.service;

import com.gonzalojerez.dev.permissionsapp.security.permissions.Permissions;
import com.gonzalojerez.dev.permissionsapp.roles.dto.CreateRoleDto;
import com.gonzalojerez.dev.permissionsapp.roles.model.Role;
import com.gonzalojerez.dev.permissionsapp.roles.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public Optional<Role> findOne(String id) {
        try {
            return roleRepository.findById(UUID.fromString(id));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("No tiene formato uuid");
        }
    }

    public Role create(CreateRoleDto createRoleDto) {
        var roleBuilder = Role.builder()
                .name(createRoleDto.getName());

        if (createRoleDto.getPermissions() != null) {
            Set<Permissions> permissions = new HashSet<>();
            for (String p : createRoleDto.getPermissions().split(",")) {
                try {
                    permissions.add(Permissions.valueOf(p));
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                    throw new RuntimeException("No se pudo parsear a un permiso válido: " + p);
                }
            }

            roleBuilder.permissions(permissions.stream().map(Enum::name).collect(Collectors.joining(",")));

        }

        Role role = roleBuilder.build();
        return roleRepository.save(role);
    }
}
