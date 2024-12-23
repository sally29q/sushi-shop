package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.ProductIngredient;
import com.example.demo.service.ProductIngredientService;

@RestController
@RequestMapping("/product-ingredients")
@CrossOrigin
public class ProductIngredientController {

	@Autowired
	private ProductIngredientService productIngredientService;

	@GetMapping
	public List<ProductIngredient> getAllProductIngredients() {
		return productIngredientService.findAllProductIngredients();
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductIngredient> getProductIngredientById(@PathVariable Integer id) {
		ProductIngredient productIngredient = productIngredientService.findProductIngredientById(id);
		return productIngredient != null ? ResponseEntity.ok(productIngredient) : ResponseEntity.notFound().build();
	}

	@PostMapping
	public ProductIngredient createProductIngredient(@RequestBody ProductIngredient productIngredient) {
		return productIngredientService.saveProductIngredient(productIngredient);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProductIngredient(@PathVariable Integer id) {
		if (productIngredientService.findProductIngredientById(id) == null) {
			return ResponseEntity.notFound().build();
		}
		productIngredientService.deleteProductIngredientById(id);
		return ResponseEntity.noContent().build();
	}
}
