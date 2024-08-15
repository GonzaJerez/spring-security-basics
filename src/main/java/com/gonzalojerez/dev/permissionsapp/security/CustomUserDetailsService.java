package com.gonzalojerez.dev.permissionsapp.security;

import com.gonzalojerez.dev.permissionsapp.roles.model.Role;
import com.gonzalojerez.dev.permissionsapp.security.permissions.Permissions;
import com.gonzalojerez.dev.permissionsapp.users.model.User;
import com.gonzalojerez.dev.permissionsapp.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow();
        Set<GrantedAuthority> authorities = new HashSet<>();

        if(user.getPermissions().equals("*")){
            for(Permissions permission: Permissions.values()){
             authorities.add(new SimpleGrantedAuthority(permission.name()));
            }
        } else {
            for(String permission:user.getPermissions().split(",")){
                authorities.add(new SimpleGrantedAuthority(permission));
            }

            for(Role role: user.getRoles()){
                for(String permission: role.getPermissions().split(",")){
                    authorities.add(new SimpleGrantedAuthority(permission));
                }
            }
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                true,
                true,
                true,
                true,
                authorities
        );
    }
}
