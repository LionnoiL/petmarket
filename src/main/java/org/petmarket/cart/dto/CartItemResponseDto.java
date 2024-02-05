package org.petmarket.cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponseDto {

    @Schema(example = "1231")
    private Long id;

    @Schema(example = "1")
    private Long advertisementId;

    @Schema(example = "1")
    private int quantity;

    @Schema(example = "1000")
    private BigDecimal price;

    @Schema(example = "Продам собаку породи такса.")
    private String title;
}
