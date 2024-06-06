package org.petmarket.users.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlackListResponseDto {
    private Long id;

    @JsonProperty("user_response_dto")
    private UserResponseDto userResponseDto;
}
