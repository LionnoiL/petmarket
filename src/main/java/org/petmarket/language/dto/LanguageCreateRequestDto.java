package org.petmarket.language.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LanguageCreateRequestDto {

    @NotNull(message = "The 'langCode' cannot be null")
    @NotBlank(message = "The 'langCode' cannot be empty")
    @Size(min = 1, max = 10, message
            = "Name must be between 1 and 10 characters")
    @Schema(example = "pl")
    @JsonProperty("lang_code")
    private String langCode;

    @NotNull(message = "The 'name' cannot be null")
    @NotBlank(message = "The 'name' cannot be empty")
    @Size(min = 1, max = 255, message
            = "Name must be between 1 and 255 characters")
    @Schema(example = "Poland")
    private String name;

    @Schema(example = "true")
    private Boolean enable;
}
