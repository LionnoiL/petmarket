package org.petmarket.advertisements.attributes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttributeResponseDto {

    private String langCode;
    private Long groupId;
    private String groupTitle;
    private Long attributeId;
    private String title;
    private int sortValue;

    @Schema(example = "2", description = "ID")
    public Long getAttributeId() {
        return attributeId;
    }

    @Schema(example = "ua", description = "Attribute language")
    public String getLangCode() {
        return langCode;
    }

    @Schema(example = "Коротка", description = "Attribute title")
    public String getTitle() {
        return title;
    }

    @Schema(example = "1", description = "Attribute group ID")
    public Long getGroupId() {
        return groupId;
    }

    @Schema(example = "Довжина шерсті", description = "Attribute group title")
    public String getGroupTitle() {
        return groupTitle;
    }

    @Schema(example = "120", description = "Sort order")
    public int getSortValue() {
        return sortValue;
    }

    @Override
    public String toString() {
        return "AttributeResponseDto{" +
                "langCode='" + langCode + '\'' +
                ", groupId=" + groupId +
                ", groupTitle='" + groupTitle + '\'' +
                ", attributeId=" + attributeId +
                ", title='" + title + '\'' +
                ", sortValue=" + sortValue +
                '}';
    }
}
