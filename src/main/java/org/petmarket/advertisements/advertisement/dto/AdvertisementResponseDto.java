package org.petmarket.advertisements.advertisement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.petmarket.advertisements.advertisement.entity.AdvertisementStatus;
import org.petmarket.advertisements.advertisement.entity.AdvertisementType;
import org.petmarket.advertisements.attributes.dto.AttributeValueResponseDto;
import org.petmarket.advertisements.category.dto.AdvertisementCategoryResponseDto;
import org.petmarket.advertisements.images.dto.AdvertisementImageResponseDto;
import org.petmarket.advertisements.review.dto.AdvertisementReviewResponseDto;
import org.petmarket.delivery.dto.DeliveryResponseDto;
import org.petmarket.location.dto.LocationResponseDto;
import org.petmarket.payment.dto.PaymentResponseDto;
import org.petmarket.users.dto.UserResponseDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdvertisementResponseDto {

    private Long id;
    private LocalDate created;
    private LocalDate updated;
    private LocalDate ending;
    private String langCode;
    private AdvertisementStatus status;
    private UserResponseDto author;
    private String alias;
    private String title;
    private String description;
    private BigDecimal price;
    private LocationResponseDto location;
    private AdvertisementCategoryResponseDto category;
    private List<DeliveryResponseDto> deliveries;
    private List<PaymentResponseDto> payments;
    private int quantity;
    private AdvertisementType type;
    private int rating;
    private List<AttributeValueResponseDto> attributes;
    private List<AdvertisementReviewResponseDto> reviews;
    private List<AdvertisementImageResponseDto> images;

    public Long getId() {
        return id;
    }

    public LocalDate getCreated() {
        return created;
    }

    public LocalDate getUpdated() {
        return updated;
    }

    public LocalDate getEnding() {
        return ending;
    }

    public String getLangCode() {
        return langCode;
    }

    public AdvertisementStatus getStatus() {
        return status;
    }

    public UserResponseDto getAuthor() {
        return author;
    }

    public String getAlias() {
        return alias;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public LocationResponseDto getLocation() {
        return location;
    }

    public AdvertisementCategoryResponseDto getCategory() {
        return category;
    }

    public List<DeliveryResponseDto> getDeliveries() {
        return deliveries;
    }

    public List<PaymentResponseDto> getPayments() {
        return payments;
    }

    public int getQuantity() {
        return quantity;
    }

    public AdvertisementType getType() {
        return type;
    }

    public int getRating() {
        return rating;
    }

    public List<AttributeValueResponseDto> getAttributes() {
        return attributes;
    }

    public List<AdvertisementReviewResponseDto> getReviews() {
        return reviews;
    }

    public List<AdvertisementImageResponseDto> getImages() {
        return images;
    }
}
