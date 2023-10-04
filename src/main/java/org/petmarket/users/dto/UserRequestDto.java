package org.petmarket.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    @NotNull(message = "The 'email' cannot be null")
    @NotBlank(message = "The 'email' cannot be empty")
    @Pattern(regexp = "^([^ ]+@[^ ]+\\.[a-z]{2,6}|)$")
    private String email;

    @NotNull(message = "The 'password' cannot be null")
    @NotBlank(message = "The 'password' cannot be empty")
    @Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,})")
    private String password;

    @Schema(example = "andriy@mail.com", description = "User email")
    public String getEmail() {
        return email;
    }

    @Schema(example = "Password1234", description = "User password")
    public String getPassword() {
        return password;
    }
}
