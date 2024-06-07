package org.petmarket.message.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.petmarket.message.dto.*;
import org.petmarket.message.service.MessageAccessCheckerService;
import org.petmarket.message.service.MessageService;
import org.petmarket.utils.annotations.parametrs.ParameterId;
import org.petmarket.utils.annotations.responses.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Chat")
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
    public MessageResponseDto addMessage(@Valid @RequestBody MessageRequestDto messageRequestDto, BindingResult bindingResult) {
        messageAccessCheckerService.checkCreateAccess(messageRequestDto);
        return messageService.addMessage(messageRequestDto);
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
                              @Valid @RequestBody MessageUpdateDto messageUpdateDto, BindingResult bindingResult) {
        messageAccessCheckerService.checkUpdateAccess(List.of(messageService.getMessageById(id)));
        messageService.updateMessage(id, messageUpdateDto);
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
}
