package org.petmarket.advertisements.attributes.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.petmarket.advertisements.attributes.entity.AttributeType;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttributeGroupResponseDto {

    @Schema(example = "1", description = "ID")
    private Long id;

    @Schema(example = "2023-10-17", description = "The date the attribute group was created")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate created;

    @Schema(example = "2023-10-17", description = "Attribute group update date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate updated;

    @Schema(example = "LIST", description = "Attribute group type")
    private AttributeType type;

    @JsonProperty("sort_value")
    @Schema(example = "120", description = "Sort order")
    private int sortValue;

    @JsonProperty("lang_code")
    @Schema(example = "ua", description = "Attribute group language")
    private String langCode;

    @Schema(example = "Довжина шерсті", description = "Attribute group title")
    private String title;

    @Schema(example = "Градація довжини шерсті тварин", description = "Attribute group description")
    private String description;

    @JsonProperty("use_in_filter")
    @Schema(example = "True", description = "The attribute group is used in the construction of the filter")
    private boolean useInFilter;

    @Override
    public String toString() {
        return "AttributeGroupResponseDto{" +
                "id=" + id +
                ", created=" + created +
                ", updated=" + updated +
                ", type=" + type +
                ", sortValue=" + sortValue +
                ", langCode='" + langCode + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", useInFilter=" + useInFilter +
                '}';
    }
}
