package org.petmarket.advertisements.attributes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.petmarket.advertisements.attributes.entity.AttributeType;

import java.time.LocalDate;

@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttributeGroupResponseDto {

    private Long id;
    private LocalDate created;
    private LocalDate updated;
    private AttributeType type;
    private int sortValue;
    private String langCode;
    private String title;
    private String description;
    private boolean useInFilter;

    @Schema(example = "1", description = "ID")
    public Long getId() {
        return id;
    }

    @Schema(example = "2023-10-17", description = "The date the attribute group was created")
    public LocalDate getCreated() {
        return created;
    }

    @Schema(example = "2023-10-17", description = "Attribute group update date")
    public LocalDate getUpdated() {
        return updated;
    }

    @Schema(example = "LIST", description = "Attribute group type")
    public AttributeType getType() {
        return type;
    }

    @Schema(example = "120", description = "Sort order")
    public int getSortValue() {
        return sortValue;
    }

    @Schema(example = "ua", description = "Attribute group language")
    public String getLangCode() {
        return langCode;
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
