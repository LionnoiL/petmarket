package org.petmarket.security.jwt;

import org.petmarket.users.entity.Role;
import org.petmarket.users.entity.User;
import org.petmarket.users.entity.UserStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JwtUserFactory {

    public static JwtUser create(User user) {
        return JwtUser.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .authorities(mapToGrantedAuthorities(new ArrayList<>(user.getRoles())))
                .enabled(user.getStatus().equals(UserStatus.ACTIVE))
                .lastPasswordResetDate(user.getUpdated()).build();
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<Role> userRoles) {
        return userRoles.stream()
                .map(role ->
                        new SimpleGrantedAuthority(role.getName())
                ).collect(Collectors.toList());
    }
}
