package org.petmarket.advertisements.images.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdvertisementImageResponseDto {

    @NotBlank
    @Schema(example = "10", description = "")
    private Long id;

    @NotBlank
    @Schema(example = "1", description = "ID advertisement")
    @JsonProperty("advertisement_id")
    private long advertisementId;

    @Schema(example = "true", description = "The main image of the advertisement")
    @JsonProperty("main_image")
    private boolean mainImage;

    @Schema(example = "https://petmarket.s3.eu-north-1.amazonaws.com/advertisements/images/3_7xKrP_b.webp",
            description = "Link to a large advertisement image"
    )
    @NotBlank
    private String url;

    @Schema(example = "https://petmarket.s3.eu-north-1.amazonaws.com/advertisements/images/3_7xKrP_s.webp",
            description = "Link to a small advertisement image"
    )
    @NotBlank
    @JsonProperty("url_small")
    private String urlSmall;
}
