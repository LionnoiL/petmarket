package org.petmarket.users.dto;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComplaintResponseDto {
    private Long id;
    private String complaint;
    private Long userId;
    private Long complainedUserId;
    private String status;
    private Date created;
    private Date updated;
}
