package org.petmarket.message.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.petmarket.message.dto.ChatResponseDto;
import org.petmarket.message.dto.UserChatsResponseDto;
import org.petmarket.message.service.MessageService;
import org.petmarket.utils.annotations.parametrs.ParameterId;
import org.petmarket.utils.annotations.parametrs.ParameterPageNumber;
import org.petmarket.utils.annotations.parametrs.ParameterPageSize;
import org.petmarket.utils.annotations.responses.ApiResponseForbidden;
import org.petmarket.utils.annotations.responses.ApiResponseSuccessful;
import org.petmarket.utils.annotations.responses.ApiResponseUnauthorized;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Chat")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(value = "/v1/chat")
public class ChatController {
    private final MessageService messageService;

    @Operation(summary = "Get all chats of current user")
    @GetMapping
    @ApiResponseSuccessful
    @ApiResponseUnauthorized
    @PreAuthorize("isAuthenticated()")
    public Page<UserChatsResponseDto> getLatestChats(
            @ParameterPageNumber @RequestParam(defaultValue = "1") @Positive int page,
            @ParameterPageSize @RequestParam(defaultValue = "30") @Positive int size) {
        return messageService.getLatestChats(PageRequest.of(page - 1, size));
    }

    @Operation(summary = "Get chat messages by chat user id")
    @GetMapping("/{chatUserId}")
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

    @Operation(summary = "Mark chat messages as read")
    @PutMapping("/{chatUserId}/read")
    @ApiResponseSuccessful
    @ApiResponseUnauthorized
    @PreAuthorize("isAuthenticated()")
    public void markChatMessagesAsRead(@ParameterId @PathVariable Long chatUserId) {
        messageService.markChatMessagesAsRead(chatUserId);
    }
}
