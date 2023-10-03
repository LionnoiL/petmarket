package org.petmarket.users.converter;

import org.mapstruct.Mapper;
import org.petmarket.users.dto.UserRequestDto;
import org.petmarket.users.dto.UserResponseDto;
import org.petmarket.users.entity.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {

    UserResponseDto mapEntityToDto(User entity);

    User mapDtoRequestToDto(UserRequestDto dto);

    List<UserResponseDto> mapEntityToDto(List<User> entities);
}
