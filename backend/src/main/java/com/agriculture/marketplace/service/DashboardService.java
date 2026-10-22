package com.agriculture.marketplace.service;

import com.agriculture.marketplace.repository.*;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;

@Service
public class DashboardService {
	private final ProductRepository products;
	private final OrderRepository orders;
	private final UserRepository users;

	public DashboardService(ProductRepository p, OrderRepository o, UserRepository u) {
		products = p;
		orders = o;
		users = u;
	}

	public Map<String, Object> farmer(Integer id) {
		var farmer = users.findById(id).orElseThrow(() -> new NoSuchElementException("User not found"));
		if (!"FARMER".equals(farmer.getRole()))
			throw new IllegalArgumentException("User is not a farmer");
		var list = products.findByFarmerUserId(id);
		long active = list.stream().filter(p -> p.getQuantity() != null && p.getQuantity().signum() > 0).count();
		return Map.of("farmerId", id, "totalListings", list.size(), "activeProducts", active);
	}

	public Map<String, Object> customer(Integer id) {
		var list = orders.findByCustomerUserIdOrderByOrderDateDesc(id);
		long pending = list.stream().filter(o -> "PENDING".equals(o.getStatus())).count();
		BigDecimal total = list.stream()
				.map(o -> o.getTotalAmount() != null ? o.getTotalAmount() : BigDecimal.ZERO)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		return Map.of("customerId", id, "totalOrders", list.size(), "pendingOrders", pending, "totalSpent", total,
				"recentOrders", list.stream().limit(5).toList());
	}
}
