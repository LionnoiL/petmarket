package org.petmarket.language.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LanguageResponseDto {

    private String langCode;
    private String name;
    private Boolean enable;

    public String getLangCode() {
        return langCode;
    }

    public String getName() {
        return name;
    }

    public Boolean getEnable() {
        return enable;
    }
}
