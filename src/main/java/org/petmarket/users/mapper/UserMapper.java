package org.petmarket.users.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.petmarket.advertisements.advertisement.dto.AuthorResponseDto;
import org.petmarket.advertisements.advertisement.dto.AuthorShortResponseDto;
import org.petmarket.config.MapperConfig;
import org.petmarket.users.dto.UserPhoneDto;
import org.petmarket.users.dto.UserRequestDto;
import org.petmarket.users.dto.UserResponseDto;
import org.petmarket.users.entity.User;

import java.util.List;
import java.util.Optional;

@Mapper(config = MapperConfig.class, uses = {UserPhoneMapper.class})
public abstract class UserMapper {

    public abstract UserResponseDto mapEntityToDto(User entity);

    @AfterMapping
    public void addMainPhoneToDto(@MappingTarget UserResponseDto responseDto) {
        if (responseDto.getPhones() == null) {
            responseDto.setMainPhone("");
            return;
        }

        Optional<UserPhoneDto> mainPhone = responseDto.getPhones().stream().filter(UserPhoneDto::getMain).findFirst();
        if (mainPhone.isPresent()) {
            responseDto.setMainPhone(mainPhone.get().getPhoneNumber());
        } else {
            responseDto.setMainPhone("");
        }
    }

    public abstract AuthorResponseDto mapEntityToAuthorDto(User entity);

    @Mapping(target = "shortName", expression =
            """
              java(entity.getFirstName() + (entity.getLastName() != null && !entity.getLastName().isEmpty() ? " " +
              entity.getLastName().charAt(0) + "." : ""))
            """)
    public abstract AuthorShortResponseDto mapEntityToAuthorShortDto(User entity);

    public abstract User mapDtoRequestToDto(UserRequestDto dto);

    public abstract List<UserResponseDto> mapEntityToDto(List<User> entities);
}
