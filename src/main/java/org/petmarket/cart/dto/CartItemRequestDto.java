package org.petmarket.cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemRequestDto {

    @NotNull
    @Schema(example = "1")
    private Long advertisementId;

    @NotBlank(message = "The 'title' cannot be empty")
    @Size(min = 1, max = 250, message
            = "Title must be between 1 and 250 characters")
    @Schema(example = "Продам собаку породи такса.")
    private String title;

    @DecimalMin(value = "1", message = "Quantity must be greater than or equal to 1")
    @Schema(example = "1")
    private int quantity;
}
