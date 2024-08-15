package com.gonzalojerez.dev.permissionsapp.users.controller;

import com.gonzalojerez.dev.permissionsapp.users.dto.CreateUserDto;
import com.gonzalojerez.dev.permissionsapp.users.dto.UpdateUserDto;
import com.gonzalojerez.dev.permissionsapp.users.model.User;
import com.gonzalojerez.dev.permissionsapp.users.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    User createUser(@Valid @RequestBody CreateUserDto createUserDto){
        return this.userService.create(createUserDto);
    }

    @PreAuthorize("hasAuthority('UPDATE_USERS') || hasAuthority('UPDATE_HIMSELF') || hasAuthority('UPDATE_ROLE_USERS')")
    @PatchMapping("/{id}")
    User updateUser(@PathVariable String id, @Valid @RequestBody UpdateUserDto updateUserDto, Authentication auth){
        return this.userService.update(id, updateUserDto, auth);
    }
}
