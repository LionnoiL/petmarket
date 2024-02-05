package org.petmarket.cart.dto;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponseDto {

    private Long id;
    private Long advertisementId;
    private int quantity;
    private BigDecimal price;
    private String title;
}
