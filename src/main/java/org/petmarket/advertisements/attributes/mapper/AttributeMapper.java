package org.petmarket.advertisements.attributes.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.petmarket.advertisements.attributes.dto.AttributeRequestDto;
import org.petmarket.advertisements.attributes.dto.AttributeResponseDto;
import org.petmarket.advertisements.attributes.entity.Attribute;

@Mapper(componentModel = "spring")
public interface AttributeMapper {

    @Mapping(target = "attributeId", source = "id")
    @Mapping(target = "groupId", source = "group.id")
    AttributeResponseDto mapEntityToDto(Attribute entity);

    Attribute mapDtoRequestToEntity(AttributeRequestDto request);
}
