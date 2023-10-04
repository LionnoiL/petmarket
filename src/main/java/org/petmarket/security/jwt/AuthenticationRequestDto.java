package org.petmarket.security.jwt;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AuthenticationRequestDto {

    @NotNull(message = "The 'email' cannot be null")
    @NotBlank(message = "The 'email' cannot be empty")
    @Pattern(regexp = "^([^ ]+@[^ ]+\\.[a-z]{2,6}|)$")
    private String email;

    @NotNull(message = "The 'password' cannot be null")
    @NotBlank(message = "The 'password' cannot be empty")
    @Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,})")
    private String password;
}
