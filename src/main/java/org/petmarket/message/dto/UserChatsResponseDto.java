package org.petmarket.message.dto;

import java.time.LocalDateTime;

public interface UserChatsResponseDto {
    String getText();

    LocalDateTime getCreated();

    String getMessageStatus();

    Long getChatUserId();

    String getChatUserEmail();
}
