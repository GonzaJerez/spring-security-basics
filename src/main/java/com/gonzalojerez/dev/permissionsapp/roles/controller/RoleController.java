package com.gonzalojerez.dev.permissionsapp.roles.controller;

import com.gonzalojerez.dev.permissionsapp.roles.dto.CreateRoleDto;
import com.gonzalojerez.dev.permissionsapp.roles.model.Role;
import com.gonzalojerez.dev.permissionsapp.roles.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("roles")
public class RoleController {

    @Autowired
    RoleService roleService;

    @PreAuthorize("hasAuthority('CREATE_ROLES')")
    @PostMapping
    public Role createRole(@Valid @RequestBody CreateRoleDto createRoleDto){
        return this.roleService.create(createRoleDto);
    }
}
