package org.petmarket.review.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.petmarket.review.entity.ReviewType;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserReviewResponseDto {

    private Long id;

    @Schema(example = "2023-10-17", description = "Review date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate created;

    @Schema(example = "BUYER_TO_SELLER")
    private ReviewType type;

    @Schema(example = "2")
    private int value;

    @Schema(example = "Товар поганої якості")
    private String description;

    @JsonProperty("user_id")
    @Schema(example = "1")
    private Long userId;

    @JsonProperty("author_first_name")
    @Schema(example = "Andrii")
    private String authorFirstName;

    @JsonProperty("author_last_name")
    @Schema(example = "Haponov")
    private String authorLastName;

    @JsonProperty("author_id")
    @Schema(example = "120")
    private long authorId;

    @Override
    public String toString() {
        return "AdvertisementReviewResponseDto{" +
                "id=" + id +
                ", created=" + created +
                ", type=" + type +
                ", value=" + value +
                ", description='" + description + '\'' +
                ", userId=" + userId +
                ", authorFirstName='" + authorFirstName + '\'' +
                ", authorLastName='" + authorLastName + '\'' +
                ", authorId=" + authorId +
                '}';
    }
}
