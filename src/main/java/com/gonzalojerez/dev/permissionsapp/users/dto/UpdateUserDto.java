package com.gonzalojerez.dev.permissionsapp.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Getter
@Setter
public class UpdateUserDto {

    private String name;

    @Email
    private String email;

    @Length(min = 6)
    private String password;

    private String permissions;

    private Set<String> roles_ids;

}
