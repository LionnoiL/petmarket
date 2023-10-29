package org.petmarket.advertisements.attributes.dto;

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

    public Long getId() {
        return id;
    }

    public LocalDate getCreated() {
        return created;
    }

    public LocalDate getUpdated() {
        return updated;
    }

    public AttributeType getType() {
        return type;
    }

    public int getSortValue() {
        return sortValue;
    }

    public String getLangCode() {
        return langCode;
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
}
