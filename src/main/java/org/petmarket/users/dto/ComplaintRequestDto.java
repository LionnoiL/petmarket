package org.petmarket.users.dto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintRequestDto {
    private String complaint;
    private Long complainedUserId;
}
