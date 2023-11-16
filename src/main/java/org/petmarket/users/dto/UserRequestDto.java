package org.petmarket.users.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    @NotNull(message = "The 'email' cannot be null")
    @NotBlank(message = "The 'email' cannot be empty")
    @Pattern(regexp = "^([^ ]+@[^ ]+\\.[a-z]{2,6}|)$")
    @Schema(example = "andriy@mail.com", description = "User email")
    private String email;

    @NotNull(message = "The 'password' cannot be null")
    @NotBlank(message = "The 'password' cannot be empty")
    @Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,})")
    @Schema(example = "Password1234", description = "User password")
    private String password;

    @JsonProperty("remember_me")
    @Schema(example = "True", description = "If we pass this parameter, then upon successful authorization, " +
            "an additional refresh token will be transferred.")
    private Boolean rememberMe;
}
