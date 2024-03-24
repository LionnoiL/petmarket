package org.petmarket.payment.repository;

import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.payment.entity.Payment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.petmarket.utils.MessageUtils.PAYMENT_NOT_FOUND;

public interface PaymentRepository extends PaymentRepositoryBasic {

    Optional<Payment> findByIdAndEnableIsTrue(Long id);

    List<Payment> findAllByEnableIsTrue();

    default List<Payment> getPaymentsFromIds(List<Long> ids) {
        if (ids == null) {
            return Collections.emptyList();
        }
        return ids.stream()
                .map(id -> findById(id).orElseThrow(
                        () -> {
                            throw new ItemNotFoundException(PAYMENT_NOT_FOUND);
                        }
                ))
                .toList();
    }

    @Query(value = "SELECT * FROM pays ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<Payment> findRandomEntities(@Param("count") int count);
}
