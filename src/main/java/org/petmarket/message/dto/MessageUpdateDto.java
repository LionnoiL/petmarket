package org.petmarket.message.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageUpdateDto {
    @NotBlank(message = "The 'text' cannot be empty")
    @Schema(example = "Hello, how are you?")
    @Size(max = 10000, message = "The 'text' length must be less than or equal to 10000")
    private String text;
}
