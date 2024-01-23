package org.petmarket.advertisements.attributes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@EqualsAndHashCode(of = {"id"})
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttributeResponseDto {

    @JsonProperty("lang_code")
    @Schema(example = "2", description = "ID")
    private String langCode;

    @JsonProperty("group_id")
    @Schema(example = "ua", description = "Attribute language")
    private Long groupId;

    @JsonProperty("group_title")
    @Schema(example = "Коротка", description = "Attribute title")
    private String groupTitle;

    @JsonProperty("attribute_id")
    @Schema(example = "1", description = "Attribute group ID")
    private Long attributeId;

    @Schema(example = "Довжина шерсті", description = "Attribute group title")
    private String title;

    @JsonProperty("sort_value")
    @Schema(example = "120", description = "Sort order")
    private int sortValue;

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
