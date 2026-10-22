package com.agriculture.marketplace.controller;

import com.agriculture.marketplace.service.DashboardService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
	private final DashboardService service;

	public DashboardController(DashboardService service) {
		this.service = service;
	}

	@GetMapping("/farmer/{id}")
	public Map<String, Object> farmer(@PathVariable("id") Integer id) {
		return service.farmer(id);
	}

	@GetMapping("/customer/{id}")
	public Map<String, Object> customer(@PathVariable("id") Integer id) {
		return service.customer(id);
	}
}
