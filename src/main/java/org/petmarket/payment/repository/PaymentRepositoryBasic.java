package org.petmarket.payment.repository;

import org.petmarket.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepositoryBasic extends JpaRepository<Payment, Long> {
}
