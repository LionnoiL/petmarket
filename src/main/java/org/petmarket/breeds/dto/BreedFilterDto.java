package org.petmarket.breeds.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BreedFilterDto {

    @Schema(description = "Breed Id", example = "1")
    private Long id;
    @Schema(description = "Breed Title", example = "Вівчарка")
    private String title;
    @JsonProperty("advertisements_count")
    private Long advertisementsCount;

    public BreedFilterDto(Long id, Long advertisementsCount) {
        this.id = id;
        this.advertisementsCount = advertisementsCount;
    }
}
