package org.petmarket.message.service;

import lombok.RequiredArgsConstructor;
import org.petmarket.errorhandling.AccessDeniedException;
import org.petmarket.message.entity.Message;
import org.petmarket.message.dto.MessageRequestDto;
import org.petmarket.users.entity.User;
import org.petmarket.users.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MessageAccessCheckerService {
    private final UserService userService;
    private final MessageService messageService;

    public void checkCreateAccess(MessageRequestDto message) {
        getNonAdminUser().ifPresent(u -> {
            if (!message.getAuthorId().equals(u.getId())) {
                throw new AccessDeniedException("Access denied to create message");
            }
        });
    }

    public void checkUpdateAccess(List<Message> messages) {
        getNonAdminUser().ifPresent(u -> {
            for (Message message : messages) {
                if (!message.getAuthor().equals(u)) {
                    throw new AccessDeniedException(String
                            .format("Access denied to update message with id %s", message.getId()));
                }
            }
        });
    }

    public void checkReadAccess(Long messageId) {
        getNonAdminUser().ifPresent(u -> {
            Message message = messageService.getMessageById(messageId);
            if (!message.getRecipient().equals(u)) {
                throw new AccessDeniedException(String
                        .format("Access denied to read message with id %s", message.getId()));
            }
        });
    }

    public void checkDeleteAccess(Long messageId) {
        getNonAdminUser().ifPresent(u -> {
            Message message = messageService.getMessageById(messageId);
            if (!message.getAuthor().equals(u)) {
                throw new AccessDeniedException(String
                        .format("Access denied to delete message with id %s", message.getId()));
            }
        });
    }

    private Optional<User> getNonAdminUser() {
        if (userService.isCurrentUserAdmin()) {
            return Optional.empty();
        } else {
            return Optional.of(userService.getCurrentUser());
        }
    }
}
