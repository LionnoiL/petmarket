package org.petmarket.message.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private UserResponseDto author;
    private UserResponseDto recipient;
    @JsonProperty("message_status")
    private MessageStatus messageStatus;
    private LocalDateTime created;
    private LocalDateTime updated;
    private boolean sender;
    private boolean edited;
}
