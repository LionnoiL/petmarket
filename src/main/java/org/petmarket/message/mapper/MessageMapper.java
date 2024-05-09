package org.petmarket.message.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.petmarket.config.MapperConfig;
import org.petmarket.message.dto.MessageResponseDto;
import org.petmarket.message.entity.Message;
import org.petmarket.message.dto.MessageRequestDto;

@Mapper(config = MapperConfig.class)
public interface MessageMapper {
    @Mapping(target = "author.id", source = "authorId")
    @Mapping(target = "recipient.id", source = "recipientId")
    Message messageRequestDtoToMessage(MessageRequestDto messageRequestDto);

    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "recipientId", source = "recipient.id")
    MessageResponseDto messageToMessageResponseDto(Message message);
}
