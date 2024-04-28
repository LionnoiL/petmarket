package org.petmarket.message.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.petmarket.message.dto.MessageRequestDto;
import org.petmarket.message.dto.MessageResponseDto;
import org.petmarket.message.dto.MessageUpdateDto;
import org.petmarket.message.service.MessageAccessCheckerService;
import org.petmarket.message.service.MessageService;
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
    public void addMessage(@RequestBody MessageRequestDto messageRequestDto) {
        messageAccessCheckerService.checkCreateAccess(messageRequestDto);
        messageService.addMessage(messageRequestDto);
    }

    //TODO: add delete received messages, sent messages, and messages by user id

    @Operation(summary = "Delete message by id")
    @DeleteMapping("/{id}")
    @ApiResponseSuccessful
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseNotFound
    @PreAuthorize("isAuthenticated()")
    public void deleteMessage(@PathVariable Long id) {
        messageAccessCheckerService.checkDeleteAccess(id);
        messageService.deleteMessage(id);
    }

    @Operation(summary = "Update message by id")
    @PutMapping("/{id}")
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @PreAuthorize("isAuthenticated()")
    public void updateMessage(@PathVariable Long id, @RequestBody MessageUpdateDto messageUpdateDto) {
        messageAccessCheckerService.checkUpdateAccess(List.of(messageService.getMessageById(id)));
        messageService.updateMessage(id, messageUpdateDto);
    }

    @Operation(summary = "Get user sent messages")
    @GetMapping("/sent")
    @ApiResponseSuccessful
    @ApiResponseUnauthorized
    @PreAuthorize("isAuthenticated()")
    public Page<MessageResponseDto> getSentMessages(
            @ParameterPageNumber @RequestParam(defaultValue = "1") @Positive int page,
            @ParameterPageSize @RequestParam(defaultValue = "30") @Positive int size) {
        return messageService.getSentUserMessages(PageRequest.of(page - 1, size));
    }

    @Operation(summary = "Get user received messages")
    @GetMapping("/received")
    @ApiResponseSuccessful
    @ApiResponseUnauthorized
    @PreAuthorize("isAuthenticated()")
    public Page<MessageResponseDto> getReceivedMessages(
            @ParameterPageNumber @RequestParam(defaultValue = "1") @Positive int page,
            @ParameterPageSize @RequestParam(defaultValue = "30") @Positive int size) {
        return messageService.getReceivedUserMessages(PageRequest.of(page - 1, size));
    }

    @Operation(summary = "Get user messages")
    @GetMapping
    @ApiResponseSuccessful
    @ApiResponseUnauthorized
    @PreAuthorize("isAuthenticated()")
    public Page<MessageResponseDto> getMessages(
            @ParameterPageNumber @RequestParam(defaultValue = "1") @Positive int page,
            @ParameterPageSize @RequestParam(defaultValue = "30") @Positive int size) {
        return messageService.getUserMessages(PageRequest.of(page - 1, size));
    }

    @Operation(summary = "Mark message as read")
    @PutMapping("/{id}/read")
    @ApiResponseSuccessful
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @PreAuthorize("isAuthenticated()")
    public void markAsRead(@PathVariable Long id) {
        messageAccessCheckerService.checkReadAccess(id);
        messageService.markAsRead(id);
    }
}
