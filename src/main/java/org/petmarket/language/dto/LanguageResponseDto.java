package org.petmarket.language.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LanguageResponseDto {

    @JsonProperty("lang_code")
    private String langCode;

    @Schema(example = "Poland")
    private String name;

    @Schema(example = "true")
    private Boolean enable;
}
