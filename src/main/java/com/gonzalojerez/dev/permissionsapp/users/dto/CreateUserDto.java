package com.gonzalojerez.dev.permissionsapp.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class CreateUserDto {

//    @NotEmpty
    private String name;

//    @NotEmpty
//    @Email
    private String email;

    private String username;

//    @NotEmpty
//    @Length(min = 6)
    private String password;

    private String authTypes;
}
