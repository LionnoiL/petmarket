package org.petmarket.message.service;

import lombok.RequiredArgsConstructor;
import org.petmarket.errorhandling.AccessDeniedException;
import org.petmarket.message.entity.Message;
import org.petmarket.message.dto.MessageRequestDto;
import org.petmarket.users.entity.User;
import org.petmarket.users.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MessageAccessCheckerService {
    private final UserService userService;
    private final MessageService messageService;

    public void checkCreateAccess(MessageRequestDto message) {
        if (!userService.isCurrentUserAdmin()) {
            User user = userService.getCurrentUser();

            if (!message.getAuthorId().equals(user.getId())) {
                throw new AccessDeniedException("Access denied to create message");
            }
        }
    }

    public void checkUpdateAccess(List<Message> messages) {
        if (!userService.isCurrentUserAdmin()) {
            User user = userService.getCurrentUser();

            for (Message message : messages) {
                if (!message.getAuthor().equals(user)) {
                    throw new AccessDeniedException(String
                            .format("Access denied to update message with id %s", message.getId()));
                }
            }
        }
    }

    public void checkViewAccess(List<Message> messages) {
        if (!userService.isCurrentUserAdmin()) {
            User user = userService.getCurrentUser();

            for (Message message : messages) {
                if (!message.getAuthor().equals(user) && !message.getRecipient().equals(user)) {
                    throw new AccessDeniedException(String
                            .format("Access denied to view message with id %s", message.getId()));
                }
            }
        }
    }

    public void checkReadAccess(Long messageId) {
        Message message = messageService.getMessageById(messageId);

        if (!userService.isCurrentUserAdmin()) {
            User user = userService.getCurrentUser();

            if (!message.getRecipient().equals(user)) {
                throw new AccessDeniedException(String
                        .format("Access denied to read message with id %s", message.getId()));
            }
        }
    }

    public void checkDeleteAccess(Long messageId) {
        Message message = messageService.getMessageById(messageId);

        if (!userService.isCurrentUserAdmin()) {
            User user = userService.getCurrentUser();

            if (!message.getAuthor().equals(user)) {
                throw new AccessDeniedException(String
                        .format("Access denied to delete message with id %s", message.getId()));
            }
        }
    }
}
