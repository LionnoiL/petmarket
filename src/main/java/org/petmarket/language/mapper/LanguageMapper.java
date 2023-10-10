package org.petmarket.language.mapper;

import org.mapstruct.Mapper;
import org.petmarket.language.dto.LanguageCreateRequestDto;
import org.petmarket.language.dto.LanguageResponseDto;
import org.petmarket.language.entity.Language;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LanguageMapper {

    LanguageResponseDto mapEntityToDto(Language entity);

    Language mapDtoRequestToDto(LanguageCreateRequestDto dto);

    List<LanguageResponseDto> mapEntityToDto(List<Language> entities);
}
