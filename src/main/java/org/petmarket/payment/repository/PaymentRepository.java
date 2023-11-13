package org.petmarket.payment.repository;

import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.payment.entity.Payment;

import java.util.List;
import java.util.Optional;

import static org.petmarket.utils.MessageUtils.PAYMENT_NOT_FOUND;

public interface PaymentRepository extends PaymentRepositoryBasic {

    Optional<Payment> findByIdAndEnableIsTrue(Long id);

    List<Payment> findAllByEnableIsTrue();

    default List<Payment> getPaymentsFromIds(List<Long> ids) {
        return ids.stream()
                .map(id -> findById(id).orElseThrow(
                        () -> {
                            throw new ItemNotFoundException(PAYMENT_NOT_FOUND);
                        }
                ))
                .toList();
    }
}
