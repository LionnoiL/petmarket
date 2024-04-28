package org.petmarket.message.service;

import lombok.RequiredArgsConstructor;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.message.dto.MessageResponseDto;
import org.petmarket.message.dto.MessageUpdateDto;
import org.petmarket.message.entity.Message;
import org.petmarket.message.dto.MessageRequestDto;
import org.petmarket.message.entity.MessageStatus;
import org.petmarket.message.mapper.MessageMapper;
import org.petmarket.message.repository.MessageRepository;
import org.petmarket.users.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final UserService userService;

    public void addMessage(MessageRequestDto messageRequestDto) {
        Message message = messageMapper.messageRequestDtoToMessage(messageRequestDto);
        message.setStatus(MessageStatus.UNREAD);
        messageRepository.save(message);
    }

    public void updateMessage(Long id, MessageUpdateDto messageUpdateDto) {
        Message message = getMessageById(id);
        message.setText(messageUpdateDto.getText());
        messageRepository.save(message);
    }

    public void markAsRead(Long id) {
        Message message = getMessageById(id);
        message.setStatus(MessageStatus.READ);
        messageRepository.save(message);
    }

    public Page<MessageResponseDto> getUserMessages(Pageable pageable) {
        Long userId = userService.getCurrentUser().getId();

        return new PageImpl<>(messageRepository.findByAuthorIdOrRecipientId(userId, userId, pageable)
                .map(messageMapper::messageToMessageResponseDto)
                .stream().peek(messageResponseDto -> {
                    if (messageResponseDto.getAuthor().getId() == userId) {
                        messageResponseDto.setSender(true);
                    }

                    if (!messageResponseDto.getCreated().equals(messageResponseDto.getUpdated())) {
                        messageResponseDto.setEdited(true);
                    }
                }).sorted(Comparator.comparing(MessageResponseDto::getCreated).reversed()).toList());
    }

    public Page<MessageResponseDto> getSentUserMessages(Pageable pageable) {
        return new PageImpl<>(messageRepository.findByAuthorId(userService.getCurrentUser().getId(), pageable)
                .map(messageMapper::messageToMessageResponseDto)
                .stream().peek(messageResponseDto -> {
                    messageResponseDto.setSender(true);

                    if (!messageResponseDto.getCreated().equals(messageResponseDto.getUpdated())) {
                        messageResponseDto.setEdited(true);
                    }
                }).sorted(Comparator.comparing(MessageResponseDto::getCreated).reversed()).toList());
    }

    public Page<MessageResponseDto> getReceivedUserMessages(Pageable pageable) {
        return new PageImpl<>(messageRepository.findByRecipientId(userService.getCurrentUser().getId(), pageable)
                .map(messageMapper::messageToMessageResponseDto)
                .stream().peek(messageResponseDto -> {
                    if (!messageResponseDto.getCreated().equals(messageResponseDto.getUpdated())) {
                        messageResponseDto.setEdited(true);
                    }
                }).sorted(Comparator.comparing(MessageResponseDto::getCreated).reversed()).toList());
    }

    public void deleteMessage(Long id) {
        getMessageById(id);
        messageRepository.deleteById(id);
    }

    public Message getMessageById(Long id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(String.format("Message with id %s not found", id)));
    }
}
