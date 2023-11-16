package org.petmarket.advertisements.attributes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.petmarket.advertisements.attributes.entity.AttributeType;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttributeGroupRequestDto {

    @Schema(example = "LIST", description = "Attribute group type")
    private AttributeType type;

    @JsonProperty("sort_value")
    @Schema(example = "120", description = "Sort order")
    private int sortValue;
    @NotBlank(message = "The 'title' cannot be empty")
    @Size(min = 1, max = 250, message
        = "Title must be between 1 and 250 characters")
    @Schema(example = "Довжина шерсті", description = "Attribute group title")
    private String title;

    @Schema(example = "Градація довжини шерсті тварин", description = "Attribute group description")
    private String description;

    @Schema(example = "True", description = "The attribute group is used in the construction of the filter")
    @JsonProperty("use_in_filter")
    private boolean useInFilter;

    @NotEmpty(message = "categoriesIds cannot be empty")
    @Schema(description = "List of category IDs")
    @JsonProperty("categories_ids")
    private List<Long> categoriesIds;
}
