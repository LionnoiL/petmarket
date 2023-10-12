package org.petmarket.options.mapper;

import org.mapstruct.Mapper;
import org.petmarket.options.dto.OptionsRequestDto;
import org.petmarket.options.dto.OptionsResponseDto;
import org.petmarket.options.entity.Options;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OptionsMapper {

    OptionsResponseDto mapEntityToDto(Options entity);

    Options mapDtoRequestToEntity(OptionsRequestDto dto);

    List<OptionsResponseDto> mapEntityToDto(List<Options> entities);
}
