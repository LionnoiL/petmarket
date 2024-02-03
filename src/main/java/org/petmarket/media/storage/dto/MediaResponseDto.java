package org.petmarket.media.storage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MediaResponseDto {
    @Schema(example = "1")
    private Long id;
    @Schema(example = "https://petmarket.s3.eu-north-1.amazonaws.com/media/images/3_7xKrP_b.webp",
            description = "Link to a large advertisement image"
    )
    private String url;
    @Schema(example = "https://petmarket.s3.eu-north-1.amazonaws.com/media/images/3_7xKrP_s.webp",
            description = "Link to a small advertisement image")
    @JsonProperty("url_small")
    private String urlSmall;
}
