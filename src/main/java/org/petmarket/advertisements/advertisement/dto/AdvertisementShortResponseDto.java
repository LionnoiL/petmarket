package org.petmarket.advertisements.advertisement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.petmarket.advertisements.advertisement.entity.AdvertisementStatus;
import org.petmarket.advertisements.advertisement.entity.AdvertisementType;
import org.petmarket.advertisements.attributes.dto.AttributeResponseDto;
import org.petmarket.advertisements.images.dto.AdvertisementImageShortResponseDto;
import org.petmarket.location.dto.LocationShortResponseDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdvertisementShortResponseDto {

    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updated;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate ending;

    @JsonProperty("lang_code")
    private String langCode;

    private AdvertisementStatus status;
    private AuthorShortResponseDto author;
    private String alias;
    private String title;
    private String description;
    private BigDecimal price;
    private LocationShortResponseDto location;
    private AdvertisementType type;
    private int rating;

    @JsonProperty("in_current_user_favorite_list")
    private boolean inFavoriteList;

    @JsonProperty("favorite_attributes")
    private List<AttributeResponseDto> favoriteAttributes;
    private List<AttributeResponseDto> attributes;
    private List<AdvertisementImageShortResponseDto> images;
}
