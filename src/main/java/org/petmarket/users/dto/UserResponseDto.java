package org.petmarket.users.dto;

import lombok.*;
import org.petmarket.users.entity.Role;
import org.petmarket.users.entity.UserStatus;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private long id;
    private Date created;
    private Date updated;
    private UserStatus status;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String site;
    private int rating;

    //TODO location, language

    private List<Role> roles;
}
