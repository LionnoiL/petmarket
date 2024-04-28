package org.petmarket.message.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageUpdateDto {
    @NotBlank(message = "The 'text' cannot be empty")
    @Schema(example = "Hello, how are you?")
    private String text;
}
