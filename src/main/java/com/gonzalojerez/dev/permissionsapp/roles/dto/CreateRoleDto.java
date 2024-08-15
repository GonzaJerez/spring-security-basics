package com.gonzalojerez.dev.permissionsapp.roles.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRoleDto {
    @NotEmpty
    private String name;

    @NotEmpty
    private String permissions;
}
