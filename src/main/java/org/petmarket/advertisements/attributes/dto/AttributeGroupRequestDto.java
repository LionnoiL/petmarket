package org.petmarket.advertisements.attributes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.petmarket.advertisements.attributes.entity.AttributeType;

import java.util.List;

@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttributeGroupRequestDto {

    private AttributeType type;
    private int sortValue;
    @NotBlank(message = "The 'title' cannot be empty")
    @Size(min = 1, max = 250, message
        = "Title must be between 1 and 250 characters")
    private String title;
    private String description;
    private boolean useInFilter;
    @NotEmpty(message = "categoriesIds cannot be empty")
    private List<Long> categoriesIds;

    @Schema(example = "LIST", description = "Attribute group type")
    public AttributeType getType() {
        return type;
    }

    @Schema(example = "120", description = "Sort order")
    public int getSortValue() {
        return sortValue;
    }

    @Schema(example = "Довжина шерсті", description = "Attribute group title")
    public String getTitle() {
        return title;
    }

    @Schema(example = "Градація довжини шерсті тварин", description = "Attribute group description")
    public String getDescription() {
        return description;
    }

    @Schema(example = "True", description = "The attribute group is used in the construction of the filter")
    public boolean isUseInFilter() {
        return useInFilter;
    }

    @Schema(description = "List of category IDs")
    public List<Long> getCategoriesIds() {
        return categoriesIds;
    }
}
