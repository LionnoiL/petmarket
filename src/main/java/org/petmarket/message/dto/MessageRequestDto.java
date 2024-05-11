package org.petmarket.message.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class MessageRequestDto {
    @NotBlank(message = "The 'text' cannot be empty")
    @Schema(example = "Hello, how are you?")
    @Size(max = 10000, message = "The 'text' length must be less than or equal to 10000")
    private String text;

    @Schema(example = "1")
    @JsonProperty("author_id")
    private Long authorId;

    @Schema(example = "1")
    @JsonProperty("recipient_id")
    private Long recipientId;
}
