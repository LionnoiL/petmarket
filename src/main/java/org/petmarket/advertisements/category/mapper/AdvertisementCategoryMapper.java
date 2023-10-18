package org.petmarket.advertisements.category.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.petmarket.advertisements.category.dto.AdvertisementCategoryResponseDto;
import org.petmarket.advertisements.category.entity.AdvertisementCategory;

@Mapper(componentModel = "spring")
public interface AdvertisementCategoryMapper {

    @Mapping(target = "parentId", source = "parent.id")
    AdvertisementCategoryResponseDto mapEntityToDto(AdvertisementCategory entity);
}
