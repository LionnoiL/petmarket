package org.petmarket.message.service;

import lombok.RequiredArgsConstructor;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.message.dto.*;
import org.petmarket.message.entity.Message;
import org.petmarket.message.entity.MessageStatus;
import org.petmarket.message.mapper.MessageMapper;
import org.petmarket.message.repository.MessageRepository;
import org.petmarket.users.mapper.UserMapper;
import org.petmarket.users.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final UserService userService;
    private final UserMapper userMapper;

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

    public void markChatMessagesAsRead(Long chatUserId) {
        messageRepository.markChatMessagesAsRead(UserService.getCurrentUserId(), chatUserId);
    }

    public ChatResponseDto getChatWithUser(Long chatUserId, Pageable pageable) {
        Long userId = UserService.getCurrentUserId();

        Page<MessageResponseDto> message = new PageImpl<>(messageRepository
                .findAllByUserAndChatUserId(userId, chatUserId, pageable)
                .map(messageMapper::messageToMessageResponseDto)
                .stream().peek(messageResponseDto -> {
                    if (messageResponseDto.getAuthorId().equals(userId)) {
                        messageResponseDto.setSender(true);
                    }

                    if (!messageResponseDto.getCreated().equals(messageResponseDto.getUpdated())) {
                        messageResponseDto.setEdited(true);
                    }
                }).toList());

        return new ChatResponseDto(userMapper.mapEntityToDto(userService.findById(chatUserId)), message);
    }

    public Page<UserChatsResponseDto> getLatestChatMessages(Pageable pageable) {
        return messageRepository.findLatestChatMessagesByUserId(UserService.getCurrentUserId(), pageable);
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
