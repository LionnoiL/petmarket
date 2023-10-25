package org.petmarket.advertisements.attributes.dto;

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

    public Long getAttributeId() {
        return attributeId;
    }

    public String getLangCode() {
        return langCode;
    }

    public String getTitle() {
        return title;
    }

    public Long getGroupId() {
        return groupId;
    }

    public String getGroupTitle() {
        return groupTitle;
    }
}
