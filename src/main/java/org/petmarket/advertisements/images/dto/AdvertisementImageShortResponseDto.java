package org.petmarket.advertisements.images.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.petmarket.advertisements.images.entity.AdvertisementImageType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdvertisementImageShortResponseDto {

    @NotBlank
    @Schema(example = "10")
    private Long id;

    @Schema(example = "https://petmarket.s3.eu-north-1.amazonaws.com/advertisements/images/3_7xKrP_s.webp",
            description = "Link to a small advertisement image"
    )
    @NotBlank
    @JsonProperty("url")
    private String url;

    private AdvertisementImageType type;
}
