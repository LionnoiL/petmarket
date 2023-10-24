package org.petmarket.advertisements.attributes.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.petmarket.advertisements.attributes.dto.AttributeValueResponseDto;
import org.petmarket.advertisements.attributes.entity.AttributeValue;

@Mapper(componentModel = "spring")
public interface AttributeValuesMapper {

    @Mapping(target = "advertisementId", source = "advertisement.id")
    @Mapping(target = "attributeId", source = "attribute.id")
    AttributeValueResponseDto mapEntityToDto(AttributeValue entity);
}
