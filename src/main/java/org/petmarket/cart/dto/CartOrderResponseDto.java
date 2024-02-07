package org.petmarket.cart.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.petmarket.delivery.dto.DeliveryResponseDto;
import org.petmarket.payment.dto.PaymentResponseDto;

import java.util.List;

@JsonPropertyOrder({"seller_id", "seller_first_name", "seller_last_name", "payments", "deliveries", "items"})
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartOrderResponseDto {

    @JsonProperty("seller_id")
    @Schema(example = "1231")
    private Long sellerId;

    @JsonProperty("seller_first_name")
    private String sellerFirstName;

    @JsonProperty("seller_last_name")
    private String sellerLastName;

    private List<PaymentResponseDto> payments;
    private List<DeliveryResponseDto> deliveries;

    private List<CartItemResponseDto> items;
}
