package org.petmarket.advertisements.attributes.mapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.attributes.dto.AttributeGroupResponseDto;
import org.petmarket.advertisements.attributes.dto.AttributeGroupShortResponseDto;
import org.petmarket.advertisements.attributes.dto.AttributeShortResponseDto;
import org.petmarket.advertisements.attributes.entity.Attribute;
import org.petmarket.language.entity.Language;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class AttributeShortMapper {

    private final AttributeGroupTranslateMapper groupMapper;
    private final AttributeTranslateMapper attributeMapper;

    public List<AttributeGroupShortResponseDto> fromAttributes(List<Attribute> attributes, Language language) {
        return attributes.stream()
                .map(a -> groupMapper.mapEntityToDto(a.getGroup(), language))
                .distinct()
                .sorted(Comparator.comparingInt(AttributeGroupResponseDto::getSortValue).reversed())
                .map(group -> new AttributeGroupShortResponseDto(
                        group.getTitle(),
                        attributes.stream()
                                .distinct()
                                .filter(attribute -> attribute.getGroup().getId() == group.getId())
                                .sorted(Comparator.comparingInt(Attribute::getSortValue).reversed())
                                .map(a -> attributeMapper.mapEntityToDto(a, language))
                                .map(a -> new AttributeShortResponseDto(a.getAttributeId(), a.getTitle()))
                                .toList()
                )).toList();
    }
}
