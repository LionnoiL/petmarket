package org.petmarket.delivery.repository;

import org.petmarket.delivery.entity.Delivery;

import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends DeliveryRepositoryBasic {

    Optional<Delivery> findByIdAndEnableIsTrue(Long id);

    List<Delivery> findAllByEnableIsTrue();
}
