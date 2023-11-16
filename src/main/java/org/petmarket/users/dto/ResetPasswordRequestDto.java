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
public class ResetPasswordRequestDto {

    @NotBlank
    @JsonProperty("verification_code")
    @Schema(example = "41d994af-6611-4b31-9933-f493d0330acd", description = "Verification code from email link")
    private String verificationCode;

    @NotNull(message = "The 'password' cannot be null")
    @NotBlank(message = "The 'password' cannot be empty")
    @Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,})")
    @JsonProperty("new_password")
    @Schema(example = "NewPassword1", description = "New user password")
    private String newPassword;
}
