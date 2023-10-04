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

    private String firstName;
    private String lastName;
    @NotNull(message = "The 'email' cannot be null")
    @NotBlank(message = "The 'email' cannot be empty")
    @Pattern(regexp = "^([^ ]+@[^ ]+\\.[a-z]{2,6}|)$")
    private String email;
    private String phone;

    @NotNull(message = "The 'password' cannot be null")
    @NotBlank(message = "The 'password' cannot be empty")
    @Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,})")
    private String password;

    @Schema(example = "Andrii", description = "User first name")
    public String getFirstName() {
        return firstName;
    }

    @Schema(example = "Haponov", description = "User last name")
    public String getLastName() {
        return lastName;
    }

    @Schema(example = "andriy@mail.com", description = "User email")
    public String getEmail() {
        return email;
    }

    @Schema(example = "099-123-45-67", description = "User phone")
    public String getPhone() {
        return phone;
    }

    @Schema(example = "Password", description = "User password")
    public String getPassword() {
        return password;
    }
}
