package com.agriculture.marketplace.repository;

import com.agriculture.marketplace.model.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<CustomerOrder, Integer> {
	
	List<CustomerOrder> findByCustomerUserIdOrderByOrderDateDesc(Integer customerId);
}
