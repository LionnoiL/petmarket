package org.petmarket.message.dto;

import lombok.*;
import org.petmarket.users.dto.UserResponseDto;
import org.springframework.data.domain.Page;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponseDto {
    private UserResponseDto chatUser;
    private Page<MessageResponseDto> messages;
}
