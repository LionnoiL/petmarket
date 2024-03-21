package org.petmarket.delivery.repository;

import org.petmarket.delivery.entity.Delivery;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.springframework.data.jpa.repository.Query;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.petmarket.utils.MessageUtils.DELIVERY_NOT_FOUND;

public interface DeliveryRepository extends DeliveryRepositoryBasic {

    Optional<Delivery> findByIdAndEnableIsTrue(Long id);

    List<Delivery> findAllByEnableIsTrue();

    default List<Delivery> getDeliveriesFromIds(List<Long> ids) {
        if (ids == null) {
            return Collections.emptyList();
        }
        return ids.stream()
                .map(id -> findById(id).orElseThrow(
                        () -> {
                            throw new ItemNotFoundException(DELIVERY_NOT_FOUND);
                        }
                ))
                .toList();
    }

    @Query(value = "SELECT * FROM deliveries ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<Delivery> findRandomEntities(int count);
}
