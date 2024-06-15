package org.petmarket.message.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.message.dto.*;
import org.petmarket.message.entity.Message;
import org.petmarket.message.entity.MessageStatus;
import org.petmarket.message.mapper.MessageMapper;
import org.petmarket.message.repository.MessageRepository;
import org.petmarket.users.dto.UserResponseDto;
import org.petmarket.users.entity.User;
import org.petmarket.users.mapper.UserMapper;
import org.petmarket.users.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {
    @Mock
    private MessageRepository messageRepository;

    @Mock
    private MessageMapper messageMapper;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private MessageService messageService;

    @Test
    void testAddMessage() {
        try (MockedStatic<UserService> mockedUserService = mockStatic(UserService.class)) {
            // Arrange
            MessageRequestDto messageRequestDto = new MessageRequestDto();
            messageRequestDto.setRecipientId(2L);
            messageRequestDto.setText("Hello");

            User currentUser = new User();
            currentUser.setId(1L);

            Message message = new Message();
            message.setText("Hello");
            message.setRecipient(User.builder().id(2L).build());
            message.setAuthor(currentUser);
            message.setStatus(MessageStatus.UNREAD);

            MessageResponseDto messageResponseDto = new MessageResponseDto();
            messageResponseDto.setText("Hello");

            when(userService.getCurrentUser()).thenReturn(currentUser);
            when(messageMapper.messageRequestDtoToMessage(messageRequestDto)).thenReturn(message);
            when(messageRepository.save(message)).thenReturn(message);
            when(messageMapper.messageToMessageResponseDto(message)).thenReturn(messageResponseDto);
            mockedUserService.when(UserService::getCurrentUserId).thenReturn(1L);

            // Act
            MessageResponseDto result = messageService.addMessage(messageRequestDto);

            // Assert
            assertEquals("Hello", result.getText());
            verify(messageRepository, times(1)).save(message);
        }
    }

    @Test
    void testUpdateMessage() {
        // Arrange
        Long messageId = 1L;
        MessageUpdateDto messageUpdateDto = new MessageUpdateDto();
        messageUpdateDto.setText("Updated Text");

        Message message = new Message();
        message.setId(messageId);
        message.setText("Old Text");

        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));
        when(messageRepository.save(message)).thenReturn(message);

        // Act
        messageService.updateMessage(messageId, messageUpdateDto);

        // Assert
        assertEquals("Updated Text", message.getText());
        verify(messageRepository, times(1)).save(message);
    }

    @Test
    void testMarkAsRead() {
        // Arrange
        Long messageId = 1L;
        Message message = new Message();
        message.setId(messageId);
        message.setStatus(MessageStatus.UNREAD);

        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));
        when(messageRepository.save(message)).thenReturn(message);

        // Act
        messageService.markAsRead(messageId);

        // Assert
        assertEquals(MessageStatus.READ, message.getStatus());
        verify(messageRepository, times(1)).save(message);
    }

    @Test
    void testMarkChatMessagesAsRead() {
        try (MockedStatic<UserService> mockedUserService = mockStatic(UserService.class)) {
            // Arrange
            Long chatUserId = 2L;
            Long currentUserId = 1L;

            when(UserService.getCurrentUserId()).thenReturn(currentUserId);
            mockedUserService.when(UserService::getCurrentUserId).thenReturn(currentUserId);

            // Act
            messageService.markChatMessagesAsRead(chatUserId);

            // Assert
            verify(messageRepository, times(1)).markChatMessagesAsRead(currentUserId, chatUserId);
        }
    }

    @Test
    void testGetChatWithUser() {
        try (MockedStatic<UserService> mockedUserService = mockStatic(UserService.class)) {
            // Arrange
            Long userId = 1L;
            Long chatUserId = 2L;
            Pageable pageable = PageRequest.of(0, 10);

            User chatUser = new User();
            chatUser.setId(chatUserId);

            Message message = new Message();
            message.setId(1L);
            message.setAuthor(User.builder().id(userId).build());
            message.setCreated(LocalDateTime.now());
            message.setUpdated(message.getCreated());

            MessageResponseDto messageResponseDto = new MessageResponseDto();
            messageResponseDto.setId(1L);
            messageResponseDto.setAuthorId(userId);

            List<Message> messageList = List.of(message);
            Page<Message> messagePage = new PageImpl<>(messageList);

            when(userService.findById(chatUserId)).thenReturn(chatUser);
            when(messageRepository.findAllByUserAndChatUserId(userId, chatUserId, pageable)).thenReturn(messagePage);
            when(messageMapper.messageToMessageResponseDto(any(Message.class)))
                    .thenAnswer((Answer<MessageResponseDto>) invocation -> {
                        Message msg = invocation.getArgument(0);
                        MessageResponseDto dto = new MessageResponseDto();
                        dto.setId(msg.getId());
                        dto.setAuthorId(msg.getAuthor().getId());
                        dto.setCreated(msg.getCreated());
                        dto.setUpdated(msg.getUpdated());
                        return dto;
                    });
            when(userMapper.mapEntityToDto(chatUser)).thenReturn(UserResponseDto
                    .builder()
                    .id(chatUserId)
                    .email("username")
                    .build());

            mockedUserService.when(UserService::getCurrentUserId).thenReturn(userId);

            // Act
            ChatResponseDto result = messageService.getChatWithUser(chatUserId, pageable);

            // Assert
            assertNotNull(result);
            assertEquals(chatUserId, result.getChatUser().getId());
            assertEquals(1, result.getMessages().getTotalElements());
            assertTrue(result.getMessages().getContent().get(0).isSender());
            assertFalse(result.getMessages().getContent().get(0).isEdited());

            verify(messageRepository, times(1)).findAllByUserAndChatUserId(userId, chatUserId, pageable);
            verify(userService, times(1)).findById(chatUserId);
            verify(messageMapper, times(1)).messageToMessageResponseDto(any(Message.class));
            verify(userMapper, times(1)).mapEntityToDto(any(User.class));
        }
    }

    @Test
    void testGetChatWithUserWhenMessageWasEdited() {
        try (MockedStatic<UserService> mockedUserService = mockStatic(UserService.class)) {
            // Arrange
            Long userId = 1L;
            Long chatUserId = 2L;
            Pageable pageable = PageRequest.of(0, 10);

            User chatUser = new User();
            chatUser.setId(chatUserId);

            Message message = new Message();
            message.setId(1L);
            message.setAuthor(User.builder().id(userId).build());
            message.setCreated(LocalDateTime.now());
            message.setUpdated(LocalDateTime.now().plusHours(1));

            MessageResponseDto messageResponseDto = new MessageResponseDto();
            messageResponseDto.setId(1L);
            messageResponseDto.setAuthorId(userId);

            List<Message> messageList = List.of(message);
            Page<Message> messagePage = new PageImpl<>(messageList);

            when(userService.findById(chatUserId)).thenReturn(chatUser);
            when(messageRepository.findAllByUserAndChatUserId(userId, chatUserId, pageable)).thenReturn(messagePage);
            when(messageMapper.messageToMessageResponseDto(any(Message.class)))
                    .thenAnswer((Answer<MessageResponseDto>) invocation -> {
                        Message msg = invocation.getArgument(0);
                        MessageResponseDto dto = new MessageResponseDto();
                        dto.setId(msg.getId());
                        dto.setAuthorId(msg.getAuthor().getId());
                        dto.setCreated(msg.getCreated());
                        dto.setUpdated(msg.getUpdated());
                        return dto;
                    });
            when(userMapper.mapEntityToDto(chatUser)).thenReturn(UserResponseDto
                    .builder()
                    .id(chatUserId)
                    .email("username")
                    .build());

            mockedUserService.when(UserService::getCurrentUserId).thenReturn(userId);

            // Act
            ChatResponseDto result = messageService.getChatWithUser(chatUserId, pageable);

            // Assert
            assertNotNull(result);
            assertEquals(chatUserId, result.getChatUser().getId());
            assertEquals(1, result.getMessages().getTotalElements());
            assertTrue(result.getMessages().getContent().get(0).isSender());
            assertTrue(result.getMessages().getContent().get(0).isEdited());

            verify(messageRepository, times(1)).findAllByUserAndChatUserId(userId, chatUserId, pageable);
            verify(userService, times(1)).findById(chatUserId);
            verify(messageMapper, times(1)).messageToMessageResponseDto(any(Message.class));
            verify(userMapper, times(1)).mapEntityToDto(any(User.class));
        }
    }

    @Test
    void testGetLatestChats() {
        try (MockedStatic<UserService> mockedUserService = mockStatic(UserService.class)) {
            // Arrange
            Long userId = 1L;
            Pageable pageable = PageRequest.of(0, 10);
            List<UserChatsResponseDto> chats = List.of(new UserChatsResponseMockDto());
            Page<UserChatsResponseDto> chatPage = new PageImpl<>(chats);

            mockedUserService.when(UserService::getCurrentUserId).thenReturn(userId);
            when(messageRepository.findLatestChatMessagesByUserId(userId, pageable)).thenReturn(chatPage);

            // Act
            Page<UserChatsResponseDto> result = messageService.getLatestChats(pageable);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
            verify(messageRepository, times(1)).findLatestChatMessagesByUserId(userId, pageable);
        }
    }

    @Test
    void testDeleteMessage() {
        // Arrange
        Long messageId = 1L;
        Message message = new Message();
        message.setId(messageId);

        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));

        // Act
        messageService.deleteMessage(messageId);

        // Assert
        verify(messageRepository, times(1)).deleteById(messageId);
    }

    @Test
    void testDeleteMessageWhenMessageNotFound() {
        // Arrange
        Long messageId = 1L;

        when(messageRepository.findById(messageId)).thenReturn(Optional.empty());

        // Act And Assert
        assertThrows(ItemNotFoundException.class, () -> messageService.deleteMessage(messageId));

        verify(messageRepository, never()).deleteById(anyLong());
    }

    @Test
    void testGetMessageById() {
        // Arrange
        Long messageId = 1L;
        Message message = new Message();
        message.setId(messageId);

        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));

        // Act
        Message result = messageService.getMessageById(messageId);

        // Assert
        assertNotNull(result);
        assertEquals(messageId, result.getId());
    }

    @Test
    void testGetMessageByIdWhenMessageNotFound() {
        // Arrange
        Long messageId = 1L;

        when(messageRepository.findById(messageId)).thenReturn(Optional.empty());

        // Act And Assert
        assertThrows(ItemNotFoundException.class, () -> messageService.getMessageById(messageId));
    }

    private static class UserChatsResponseMockDto implements UserChatsResponseDto {
        @Override
        public String getText() {
            return "";
        }

        @Override
        public LocalDateTime getCreated() {
            return null;
        }

        @Override
        public String getMessageStatus() {
            return "";
        }

        @Override
        public Long getChatUserId() {
            return 0L;
        }

        @Override
        public String getChatUserEmail() {
            return "";
        }
    }
}
