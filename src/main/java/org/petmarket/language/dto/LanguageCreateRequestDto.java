package org.petmarket.language.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LanguageCreateRequestDto {

    @NotNull(message = "The 'langCode' cannot be null")
    @NotBlank(message = "The 'langCode' cannot be empty")
    @Size(min = 1, max = 10, message
            = "Name must be between 1 and 10 characters")
    private String langCode;

    @NotNull(message = "The 'name' cannot be null")
    @NotBlank(message = "The 'name' cannot be empty")
    @Size(min = 1, max = 255, message
            = "Name must be between 1 and 255 characters")
    private String name;

    private Boolean enable;

    @Schema(example = "pl")
    public String getLangCode() {
        return langCode;
    }

    @Schema(example = "Poland")
    public String getName() {
        return name;
    }

    @Schema(example = "true")
    public Boolean getEnable() {
        return enable;
    }
}
