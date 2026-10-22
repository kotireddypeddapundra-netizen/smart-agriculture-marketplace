package com.agriculture.marketplace.repository;

import com.agriculture.marketplace.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
	
}
