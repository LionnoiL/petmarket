package org.petmarket.users.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class ComplaintRequestDto {
    @NotBlank(message = "The 'text' cannot be empty")
    @Schema(example = "Hello, how are you?")
    @Size(max = 10000, message = "The 'text' length must be less than or equal to 10000")
    private String complaint;

    @Schema(example = "1")
    @JsonProperty("complained_user_id")
    private Long complainedUserId;
}
