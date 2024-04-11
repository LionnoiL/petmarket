package org.petmarket.review.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserReviewListResponseDto {

    private int rating;

    @JsonProperty("reviews_count")
    private int reviewsCount;

    @JsonProperty("rating_list")
    private RatingList ratingList;

    private List<UserReviewResponseDto> reviews;
}
