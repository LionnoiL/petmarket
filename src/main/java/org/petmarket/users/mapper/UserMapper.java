package org.petmarket.users.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.petmarket.advertisements.advertisement.dto.AuthorResponseDto;
import org.petmarket.advertisements.advertisement.dto.AuthorShortResponseDto;
import org.petmarket.config.MapperConfig;
import org.petmarket.users.dto.UserRequestDto;
import org.petmarket.users.dto.UserResponseDto;
import org.petmarket.users.entity.User;

import java.util.List;

@Mapper(config = MapperConfig.class)
public interface UserMapper {

    UserResponseDto mapEntityToDto(User entity);

    AuthorResponseDto mapEntityToAuthorDto(User entity);

    @Mapping(target = "shortName", expression =
            """
                    java(entity.getFirstName() + (entity.getLastName() != null && !entity.getLastName().isEmpty() ? " " + 
                    entity.getLastName().charAt(0) + "." : ""))
                          """)
    AuthorShortResponseDto mapEntityToAuthorShortDto(User entity);

    User mapDtoRequestToDto(UserRequestDto dto);

    List<UserResponseDto> mapEntityToDto(List<User> entities);
}
