package org.petmarket.cart.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"id","created", "updated", "total_quantity", "total_sum", "total_number_of_positions",
        "number_of_orders", "orders"})
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartForCheckoutResponseDto {

    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updated;

    @JsonProperty("total_quantity")
    @Schema(example = "1")
    private int totalQuantity;

    @JsonProperty("total_sum")
    @Schema(example = "13500")
    private BigDecimal totalSum;

    @JsonProperty("total_number_of_positions")
    @Schema(example = "1")
    private int totalNumberPositions;

    @JsonProperty("number_of_orders")
    @Schema(example = "1")
    private int numberOfOrders;

    private List<CartOrderResponseDto> orders = new ArrayList<>();
}
