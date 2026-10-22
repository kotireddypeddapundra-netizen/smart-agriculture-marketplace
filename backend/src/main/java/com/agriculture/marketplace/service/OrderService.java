package com.agriculture.marketplace.service;

import com.agriculture.marketplace.model.*;
import com.agriculture.marketplace.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderService {
	private final OrderRepository orders;
	private final UserRepository users;
	private final ProductRepository products;

	public OrderService(OrderRepository orders, UserRepository users, ProductRepository products) {
		this.orders = orders;
		this.users = users;
		this.products = products;
	}

	public record ItemRequest(Integer productId, BigDecimal quantity) {
	}

	public record CreateOrderRequest(Integer customerId, List<ItemRequest> items) {
	}

	@Transactional
	public CustomerOrder create(CreateOrderRequest req) {
		User customer = users.findById(req.customerId())
				.orElseThrow(() -> new NoSuchElementException("Customer not found"));
		if (!"CUSTOMER".equals(customer.getRole()))
			throw new IllegalArgumentException("Only customers can place orders");
		if (req.items() == null || req.items().isEmpty())
			throw new IllegalArgumentException("Order must contain items");
		CustomerOrder order = new CustomerOrder();
		order.setCustomer(customer);
		BigDecimal total = BigDecimal.ZERO;
		for (ItemRequest r : req.items()) {
			if (r.quantity() == null || r.quantity().signum() <= 0)
				throw new IllegalArgumentException("Quantity must be positive");
			Product p = products.findById(r.productId())
					.orElseThrow(() -> new NoSuchElementException("Product not found: " + r.productId()));
			if (p.getQuantity().compareTo(r.quantity()) < 0)
				throw new IllegalArgumentException("Insufficient stock for " + p.getProductName());
			p.setQuantity(p.getQuantity().subtract(r.quantity()));
			products.save(p);
			OrderItem item = new OrderItem();
			item.setProduct(p);
			item.setQuantity(r.quantity());
			item.setPriceAtPurchase(p.getPrice());
			order.addItem(item);
			total = total.add(p.getPrice().multiply(r.quantity()));
		}
		order.setTotalAmount(total);
		return orders.save(order);
	}

	public List<CustomerOrder> byCustomer(Integer id) {
		return orders.findByCustomerUserIdOrderByOrderDateDesc(id);
	}
}
