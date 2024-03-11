package org.petmarket.security.front;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FrontTokenRequestDto {

    @NotBlank
    @Schema(example = "41d994af-6611-4b31-9933-f493d0330acd", description = "Front secret token")
    private String token;
}
