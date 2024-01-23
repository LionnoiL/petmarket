package org.petmarket.advertisements.attributes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttributeShortResponseDto {

    @JsonProperty("attribute_id")
    @Schema(example = "1", description = "Attribute ID")
    private Long attributeId;

    @Schema(example = "Довжина шерсті", description = "Attribute title")
    private String title;
}
