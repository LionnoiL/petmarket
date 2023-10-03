package org.petmarket.security.jwt;

import lombok.Data;

@Data
public class AuthenticationRequestDto {

    private String email;
    private String password;
}
