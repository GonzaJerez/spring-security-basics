package com.gonzalojerez.dev.permissionsapp.users.model;

import com.gonzalojerez.dev.permissionsapp.roles.model.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = true)
    private String name;

    @Column(nullable = true)
    private String email;

    @Column(nullable = true)
    private String username;

    @Column(nullable = true)
    private String password;

    private String permissions;

    @Column(name = "auth_types")
    private String authTypes;

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = Role.class)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
}
