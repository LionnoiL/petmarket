package org.petmarket.advertisements.advertisement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.petmarket.users.entity.UserType;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorShortResponseDto {

    private String shortName;
    private int rating;

    @JsonProperty("reviews_count")
    private int reviewsCount;

    @JsonProperty("user_type")
    private UserType userType;
}
