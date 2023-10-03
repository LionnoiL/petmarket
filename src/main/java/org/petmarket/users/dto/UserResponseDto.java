package org.petmarket.users.dto;

import lombok.*;
import org.petmarket.users.entity.Role;
import org.petmarket.users.entity.UserStatus;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private UserStatus status;
    private List<Role> roles;
}
