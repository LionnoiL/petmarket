package org.petmarket.pages.mapper;

import org.mapstruct.Mapper;
import org.petmarket.pages.dto.SitePageCreateRequestDto;
import org.petmarket.pages.dto.SitePageResponseDto;
import org.petmarket.pages.entity.SitePage;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SitePageMapper {

    SitePageResponseDto mapEntityToDto(SitePage entity);

    SitePage mapDtoRequestToEntity(SitePageCreateRequestDto dto);

    List<SitePageResponseDto> mapEntityToDto(List<SitePage> entities);


}
