package org.petmarket.advertisements.attributes.dto;

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
@AllArgsConstructor
@NoArgsConstructor
public class AttributeRequestDto {

    @NotNull(message = "The 'groupId' cannot be null")
    private Long groupId;
    @NotBlank(message = "The 'title' cannot be empty")
    @Size(min = 1, max = 250, message
            = "Title must be between 1 and 250 characters")
    private String title;
    private int sortValue;

    @Schema(example = "Коротка", description = "Attribute title")
    public String getTitle() {
        return title;
    }

    @Schema(example = "1", description = "Attribute group ID")
    public Long getGroupId() {
        return groupId;
    }

    @Schema(example = "120", description = "Sort order")
    public int getSortValue() {
        return sortValue;
    }
}
