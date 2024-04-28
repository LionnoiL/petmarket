package org.petmarket.message.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.petmarket.config.MapperConfig;
import org.petmarket.message.dto.MessageResponseDto;
import org.petmarket.message.entity.Message;
import org.petmarket.message.dto.MessageRequestDto;
import org.petmarket.users.mapper.UserMapper;

@Mapper(config = MapperConfig.class, uses = {UserMapper.class})
public interface MessageMapper {
    @Mapping(target = "author.id", source = "authorId")
    @Mapping(target = "recipient.id", source = "recipientId")
    Message messageRequestDtoToMessage(MessageRequestDto messageRequestDto);

    MessageResponseDto messageToMessageResponseDto(Message message);
}
