package org.petmarket.payment.repository;

import org.petmarket.payment.entity.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends PaymentRepositoryBasic {

    Optional<Payment> findByIdAndEnableIsTrue(Long id);

    List<Payment> findAllByEnableIsTrue();
}
