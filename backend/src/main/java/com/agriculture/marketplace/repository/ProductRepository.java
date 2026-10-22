package com.agriculture.marketplace.repository;

import com.agriculture.marketplace.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	List<Product> findByProductNameContainingIgnoreCase(String productName);

	List<Product> findByFarmerUserId(Integer farmerId);
}
