package org.petmarket.advertisements.advertisement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

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
}
