package com.agriculture.marketplace.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer orderItemId;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	private CustomerOrder order;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;
	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal quantity;
	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal priceAtPurchase;

	public OrderItem() {
	}

	public Integer getOrderItemId() {
		return orderItemId;
	}

	public CustomerOrder getOrder() {
		return order;
	}

	public void setOrder(CustomerOrder v) {
		order = v;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product v) {
		product = v;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal v) {
		quantity = v;
	}

	public BigDecimal getPriceAtPurchase() {
		return priceAtPurchase;
	}

	public void setPriceAtPurchase(BigDecimal v) {
		priceAtPurchase = v;
	}
}
