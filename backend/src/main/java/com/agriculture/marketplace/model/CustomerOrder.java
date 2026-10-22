package com.agriculture.marketplace.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "orders")
public class CustomerOrder {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer orderId;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "customer_id", nullable = false)
	private User customer;
	private LocalDateTime orderDate;
	@Column(precision = 10, scale = 2, nullable = false)
	private BigDecimal totalAmount = BigDecimal.ZERO;
	@Column(nullable = false, length = 30)
	private String status = "PENDING";
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<OrderItem> items = new ArrayList<>();

	@PrePersist
	void onCreate() {
		orderDate = LocalDateTime.now();
	}

	public CustomerOrder() {
	}

	public Integer getOrderId() {
		return orderId;
	}

	public User getCustomer() {
		return customer;
	}

	public void setCustomer(User v) {
		customer = v;
	}

	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal v) {
		totalAmount = v;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String v) {
		status = v;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public void addItem(OrderItem item) {
		items.add(item);
		item.setOrder(this);
	}
}
