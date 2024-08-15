package com.gonzalojerez.dev.permissionsapp.users.service;

import com.gonzalojerez.dev.permissionsapp.security.permissions.Permissions;
import com.gonzalojerez.dev.permissionsapp.roles.model.Role;
import com.gonzalojerez.dev.permissionsapp.roles.service.RoleService;
import com.gonzalojerez.dev.permissionsapp.users.dto.CreateUserDto;
import com.gonzalojerez.dev.permissionsapp.users.dto.UpdateUserDto;
import com.gonzalojerez.dev.permissionsapp.users.model.User;
import com.gonzalojerez.dev.permissionsapp.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleService roleService;

    public User create(CreateUserDto createUserDto){
        User newUser = User.builder()
                .email(createUserDto.getEmail())
                .name(createUserDto.getName())
                .password(passwordEncoder.encode(createUserDto.getPassword()))
                .permissions(Permissions.UPDATE_HIMSELF.name())
                .build();
        return userRepository.save(newUser);
    }

    public User update(String id, UpdateUserDto updateUserDto, Authentication auth){

        User updatedUser = userRepository.findById(UUID.fromString(id)).orElseThrow();

        if(!auth.getName().equals(updatedUser.getEmail()) && auth.getAuthorities().stream().noneMatch(a -> a.toString().equals(Permissions.UPDATE_USERS.name()))){
            throw new RuntimeException("No tienes permisos para actualizar a este usuario");
        }

        if(updateUserDto.getName() != null){
            updatedUser.setName(updateUserDto.getName());
        }
        if(updateUserDto.getEmail() != null){
            updatedUser.setEmail(updateUserDto.getEmail());
        }
        if(updateUserDto.getPassword() != null){
            updatedUser.setPassword(passwordEncoder.encode(updateUserDto.getPassword()));
        }
        if(updateUserDto.getPermissions() != null){
            Set<Permissions> permissions = new HashSet<>();
            for(String p: updateUserDto.getPermissions().split(",")){
                try {
                permissions.add(Permissions.valueOf(p));
                } catch (IllegalArgumentException e){
                    System.out.println(e.getMessage());
                }
            }

            permissions.forEach(p -> {
                updatedUser.setPermissions(p.name());
            });
        }
        if(updateUserDto.getRoles_ids() != null && !updateUserDto.getRoles_ids().isEmpty()){
            Set<Role> roles = new HashSet<>();
            updateUserDto.getRoles_ids().forEach(roleId -> {
                Role role = roleService.findOne(roleId).orElseThrow();
                roles.add(role);
            });
            updatedUser.setRoles(roles);
        }

        return userRepository.save(updatedUser);
    }
}
