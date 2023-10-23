package org.petmarket.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDto {

    private String name;

    public String getName() {
        return name;
    }
}
