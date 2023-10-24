package org.petmarket.payment.mapper;

import org.mapstruct.Mapper;
import org.petmarket.payment.dto.PaymentResponseDto;
import org.petmarket.payment.entity.Payment;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentResponseDto mapEntityToDto(Payment entity);
}
