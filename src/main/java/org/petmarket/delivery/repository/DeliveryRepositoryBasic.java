package org.petmarket.delivery.repository;

import org.petmarket.delivery.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepositoryBasic extends JpaRepository<Delivery, Long> {
}
