package org.petmarket.advertisements.attributes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class AttributeRequestDto {

    @JsonProperty("group_id")
    @NotNull(message = "The 'groupId' cannot be null")
    @Schema(example = "1", description = "Attribute group ID")
    private Long groupId;

    @NotBlank(message = "The 'title' cannot be empty")
    @Size(min = 1, max = 250, message
        = "Title must be between 1 and 250 characters")
    @Schema(example = "Коротка", description = "Attribute title")
    private String title;

    @JsonProperty("sort_value")
    @Schema(example = "120", description = "Sort order")
    private int sortValue;
}
