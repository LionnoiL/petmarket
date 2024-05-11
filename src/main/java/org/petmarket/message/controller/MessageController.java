package org.petmarket.message.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.petmarket.message.dto.*;
import org.petmarket.message.service.MessageAccessCheckerService;
import org.petmarket.message.service.MessageService;
import org.petmarket.utils.annotations.parametrs.ParameterId;
import org.petmarket.utils.annotations.parametrs.ParameterPageNumber;
import org.petmarket.utils.annotations.parametrs.ParameterPageSize;
import org.petmarket.utils.annotations.responses.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Message")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(value = "/v1/messages")
public class MessageController {
    private final MessageService messageService;
    private final MessageAccessCheckerService messageAccessCheckerService;

    @Operation(summary = "Create new message")
    @PostMapping
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @PreAuthorize("isAuthenticated()")
    public void addMessage(@Valid @RequestBody MessageRequestDto messageRequestDto) {
        messageAccessCheckerService.checkCreateAccess(messageRequestDto);
        messageService.addMessage(messageRequestDto);
    }

    @Operation(summary = "Delete message by id")
    @DeleteMapping("/{id}")
    @ApiResponseSuccessful
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseNotFound
    @PreAuthorize("isAuthenticated()")
    public void deleteMessage(@ParameterId @PathVariable Long id) {
        messageAccessCheckerService.checkDeleteAccess(id);
        messageService.deleteMessage(id);
    }

    @Operation(summary = "Update message by id")
    @PutMapping("/{id}")
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseNotFound
    @PreAuthorize("isAuthenticated()")
    public void updateMessage(@ParameterId @PathVariable Long id,
                              @Valid @RequestBody MessageUpdateDto messageUpdateDto) {
        messageAccessCheckerService.checkUpdateAccess(List.of(messageService.getMessageById(id)));
        messageService.updateMessage(id, messageUpdateDto);
    }

    @Operation(summary = "Get chat messages by chat user id")
    @GetMapping("/chat/{chatUserId}")
    @ApiResponseSuccessful
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @PreAuthorize("isAuthenticated()")
    public ChatResponseDto getChatMessages(
            @ParameterPageNumber @RequestParam(defaultValue = "1") @Positive int page,
            @ParameterPageSize @RequestParam(defaultValue = "30") @Positive int size,
            @ParameterId @PathVariable Long chatUserId) {
        return messageService.getChatWithUser(chatUserId,
                PageRequest.of(page - 1, size));
    }

    @Operation(summary = "Mark message as read")
    @PutMapping("/{id}/read")
    @ApiResponseSuccessful
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @PreAuthorize("isAuthenticated()")
    public void markAsRead(@ParameterId @PathVariable Long id) {
        messageAccessCheckerService.checkReadAccess(id);
        messageService.markAsRead(id);
    }

    @Operation(summary = "Mark chat messages as read")
    @PutMapping("/chat/{chatUserId}/read")
    @ApiResponseSuccessful
    @ApiResponseUnauthorized
    @PreAuthorize("isAuthenticated()")
    public void markChatMessagesAsRead(@ParameterId @PathVariable Long chatUserId) {
        messageService.markChatMessagesAsRead(chatUserId);
    }

    @Operation(summary = "Get all chats of current user")
    @GetMapping("/chat")
    @ApiResponseSuccessful
    @ApiResponseUnauthorized
    @PreAuthorize("isAuthenticated()")
    public Page<UserChatsResponseDto> getLatestChatMessages(
            @ParameterPageNumber @RequestParam(defaultValue = "1") @Positive int page,
            @ParameterPageSize @RequestParam(defaultValue = "30") @Positive int size) {
        return messageService.getLatestChatMessages(PageRequest.of(page - 1, size));
    }
}
