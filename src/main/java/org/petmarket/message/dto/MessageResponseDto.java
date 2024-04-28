package org.petmarket.message.dto;

import lombok.*;
import org.petmarket.message.entity.MessageStatus;
import org.petmarket.users.dto.UserResponseDto;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponseDto {
    private Long id;
    private String text;
    private MessageStatus status;
    private LocalDateTime created;
    private LocalDateTime updated;
    private boolean sender;
    private boolean edited;
    private UserResponseDto author;
    private UserResponseDto recipient;
}
