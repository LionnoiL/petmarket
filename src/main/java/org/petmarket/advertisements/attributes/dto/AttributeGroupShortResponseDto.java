package org.petmarket.advertisements.attributes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
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
public class AttributeGroupShortResponseDto {

    @Schema(example = "Довжина шерсті", description = "Attribute group title")
    private String title;

    private List<AttributeShortResponseDto> attributes;
}
