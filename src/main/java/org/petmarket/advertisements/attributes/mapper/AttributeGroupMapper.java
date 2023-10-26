package org.petmarket.advertisements.attributes.mapper;

import org.mapstruct.Mapper;
import org.petmarket.advertisements.attributes.dto.AttributeGroupResponseDto;
import org.petmarket.advertisements.attributes.entity.AttributeGroup;

@Mapper(componentModel = "spring")
public interface AttributeGroupMapper {

    AttributeGroupResponseDto mapEntityToDto(AttributeGroup entity);
}
