package com.agriculture.marketplace.controller;

import com.agriculture.marketplace.model.Product;
import com.agriculture.marketplace.service.ProductService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
	private final ProductService service;

	public ProductController(ProductService service) {
		this.service = service;
	}

	@GetMapping
	public List<Product> all(@RequestParam(name = "search", required = false) String search) {
		return service.all(search);
	}

	@GetMapping("/{id}")
	public Product byId(@PathVariable("id") Integer id) {
		return service.byId(id);
	}

	@PostMapping
	public Product create(@RequestBody Product p) {
		return service.save(p);
	}

	@PutMapping("/{id}")
	public Product update(@PathVariable("id") Integer id, @RequestBody Product p) {
		return service.update(id, p);
	}

	@DeleteMapping("/{id}")
	public MapMessage delete(@PathVariable("id") Integer id) {
		service.delete(id);
		return new MapMessage("Product deleted successfully");
	}

	public record MapMessage(String message) {
	}
}
