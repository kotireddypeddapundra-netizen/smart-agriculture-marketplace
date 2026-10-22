package com.agriculture.marketplace.service;

import com.agriculture.marketplace.model.*;
import com.agriculture.marketplace.repository.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ProductService {
	private final ProductRepository products;
	private final UserRepository users;

	public ProductService(ProductRepository products, UserRepository users) {
		this.products = products;
		this.users = users;
	}

	public List<Product> all(String search) {
		return search == null || search.isBlank() ? products.findAll()
				: products.findByProductNameContainingIgnoreCase(search);
	}

	public Product byId(Integer id) {
		return products.findById(id).orElseThrow(() -> new NoSuchElementException("Product not found: " + id));
	}

	public Product save(Product p) {
		if (p.getPrice() == null || p.getPrice().signum() < 0 || p.getQuantity() == null
				|| p.getQuantity().signum() < 0)
			throw new IllegalArgumentException("Price and quantity must be zero or greater");
		if (p.getFarmer() != null && p.getFarmer().getUserId() != null) {
			User f = users.findById(p.getFarmer().getUserId())
					.orElseThrow(() -> new NoSuchElementException("Farmer not found"));
			if (!"FARMER".equals(f.getRole()))
				throw new IllegalArgumentException("Selected user is not a farmer");
			p.setFarmer(f);
		}
		return products.save(p);
	}

	public Product update(Integer id, Product p) {
		Product old = byId(id);
		old.setProductName(p.getProductName());
		old.setCategory(p.getCategory());
		old.setDescription(p.getDescription());
		old.setPrice(p.getPrice());
		old.setQuantity(p.getQuantity());
		old.setUnit(p.getUnit());
		old.setImageUrl(p.getImageUrl());
		return save(old);
	}

	public void delete(Integer id) {
		if (!products.existsById(id))
			throw new NoSuchElementException("Product not found");
		products.deleteById(id);
	}
}
