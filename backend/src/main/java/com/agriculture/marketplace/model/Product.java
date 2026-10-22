package com.agriculture.marketplace.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer productId;
	@Column(nullable = false, length = 100)
	
	private String productName;
	private String category;
	@Column(length = 500)
	
	private String description;
	@Column(nullable = false, precision = 10, scale = 2)
	
	private BigDecimal price;
	@Column(nullable = false, precision = 10, scale = 2)
	
	private BigDecimal quantity;
	private String unit;
	@Column(length = 500)
	
	private String imageUrl;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "farmer_id")
	private User farmer;
	private LocalDateTime createdAt;

	@PrePersist
	void onCreate() {
		createdAt = LocalDateTime.now();
	}

	public Product() {
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer v) {
		productId = v;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String v) {
		productName = v;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String v) {
		category = v;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String v) {
		description = v;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal v) {
		price = v;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal v) {
		quantity = v;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String v) {
		unit = v;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String v) {
		imageUrl = v;
	}

	public User getFarmer() {
		return farmer;
	}

	public void setFarmer(User v) {
		farmer = v;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
}
