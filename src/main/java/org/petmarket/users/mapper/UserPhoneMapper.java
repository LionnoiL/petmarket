package org.petmarket.users.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.petmarket.config.MapperConfig;
import org.petmarket.users.dto.UserPhoneDto;
import org.petmarket.users.entity.UserPhone;

import java.util.Set;

@Mapper(config = MapperConfig.class)
public interface UserPhoneMapper {

    @Mapping(target = "main", source = "main")
    UserPhoneDto mapEntityToDto(UserPhone entity);

    Set<UserPhoneDto> mapEntityToDto(Set<UserPhone> entities);
}
