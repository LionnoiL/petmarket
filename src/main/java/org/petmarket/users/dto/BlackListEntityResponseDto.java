package org.petmarket.users.dto;

import lombok.*;
import org.petmarket.users.entity.User;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlackListEntityResponseDto {
    private Long id;
    private User user;
}
