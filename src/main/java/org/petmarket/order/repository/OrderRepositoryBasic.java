package org.petmarket.order.repository;

import org.petmarket.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepositoryBasic extends JpaRepository<Order, Long> {
}
