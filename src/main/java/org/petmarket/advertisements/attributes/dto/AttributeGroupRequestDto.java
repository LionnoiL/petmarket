package org.petmarket.advertisements.attributes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.petmarket.advertisements.attributes.entity.AttributeType;

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

    public AttributeType getType() {
        return type;
    }

    public int getSortValue() {
        return sortValue;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isUseInFilter() {
        return useInFilter;
    }

    public List<Long> getCategoriesIds() {
        return categoriesIds;
    }
}
