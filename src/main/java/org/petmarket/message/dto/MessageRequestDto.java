package org.petmarket.message.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequestDto {
    @NotBlank(message = "The 'text' cannot be empty")
    @Schema(example = "Hello, how are you?")
    private String text;

    @Schema(example = "1")
    @JsonProperty("author_id")
    private Long authorId;

    @Schema(example = "1")
    @JsonProperty("recipient_id")
    private Long recipientId;
}
