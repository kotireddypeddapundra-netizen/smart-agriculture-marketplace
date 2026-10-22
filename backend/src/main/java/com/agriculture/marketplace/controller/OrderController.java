package com.agriculture.marketplace.controller;

import com.agriculture.marketplace.model.CustomerOrder;
import com.agriculture.marketplace.service.OrderService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
	private final OrderService service;

	public OrderController(OrderService service) {
		this.service = service;
	}

	@PostMapping
	public CustomerOrder create(@RequestBody OrderService.CreateOrderRequest request) {
		return service.create(request);
	}

	@GetMapping("/customer/{customerId}")
	public List<CustomerOrder> byCustomer(@PathVariable("customerId") Integer customerId) {
		return service.byCustomer(customerId);
	}
}
