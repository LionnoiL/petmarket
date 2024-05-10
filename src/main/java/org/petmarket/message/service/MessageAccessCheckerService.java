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
        getNonAdminUser()
                .filter(user -> message.getAuthorId().equals(user.getId()))
                .orElseThrow(() -> new AccessDeniedException("Access denied to create message"));
    }

    public void checkUpdateAccess(List<Message> messages) {
        getNonAdminUser().ifPresent(user -> messages.stream()
                .filter(message -> message.getAuthor().equals(user))
                .findAny()
                .orElseThrow(() -> new AccessDeniedException("Access denied to update message")));
    }

    public void checkReadAccess(Long messageId) {
        Message message = messageService.getMessageById(messageId);
        getNonAdminUser()
                .filter(user -> message.getRecipient().equals(user))
                .orElseThrow(() -> new AccessDeniedException("Access denied to read message"));
    }

    public void checkDeleteAccess(Long messageId) {
        Message message = messageService.getMessageById(messageId);
        getNonAdminUser()
                .filter(user -> message.getAuthor().equals(user))
                .orElseThrow(() -> new AccessDeniedException("Access denied to delete message"));
    }

    private Optional<User> getNonAdminUser() {
        if (userService.isCurrentUserAdmin()) {
            return Optional.empty();
        }
        return Optional.ofNullable(userService.getCurrentUser());
    }
}
