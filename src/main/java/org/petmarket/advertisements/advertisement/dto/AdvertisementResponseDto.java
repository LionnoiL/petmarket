package org.petmarket.advertisements.advertisement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.petmarket.advertisements.advertisement.entity.AdvertisementStatus;
import org.petmarket.advertisements.advertisement.entity.AdvertisementType;
import org.petmarket.advertisements.attributes.dto.AttributeResponseDto;
import org.petmarket.advertisements.category.dto.AdvertisementCategoryResponseDto;
import org.petmarket.advertisements.images.dto.AdvertisementImageResponseDto;
import org.petmarket.delivery.dto.DeliveryResponseDto;
import org.petmarket.location.dto.LocationResponseDto;
import org.petmarket.payment.dto.PaymentResponseDto;
import org.petmarket.review.dto.AdvertisementReviewResponseDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
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
    private AuthorResponseDto author;
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
    private List<AttributeResponseDto> attributes;
    private List<AdvertisementReviewResponseDto> reviews;
    private List<AdvertisementImageResponseDto> images;
}
