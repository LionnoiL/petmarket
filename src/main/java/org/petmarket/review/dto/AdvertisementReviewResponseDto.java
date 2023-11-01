package org.petmarket.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.petmarket.review.entity.ReviewType;

import java.time.LocalDate;

@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdvertisementReviewResponseDto {

    private Long id;
    private LocalDate created;
    private ReviewType type;
    private int value;
    private String description;
    private Long advertisementId;
    private String authorFirstName;
    private String authorLastName;
    private long authorId;

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }

    public long getAuthorId() {
        return authorId;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getCreated() {
        return created;
    }

    public ReviewType getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public Long getAdvertisementId() {
        return advertisementId;
    }

    @Override
    public String toString() {
        return "AdvertisementReviewResponseDto{" +
                "id=" + id +
                ", created=" + created +
                ", type=" + type +
                ", value=" + value +
                ", description='" + description + '\'' +
                ", advertisementId=" + advertisementId +
                ", authorFirstName='" + authorFirstName + '\'' +
                ", authorLastName='" + authorLastName + '\'' +
                ", authorId=" + authorId +
                '}';
    }
}
