package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.ProductIngredient;
import com.example.demo.repository.ProductIngredientRepository;

@Service
public class ProductIngredientService {

	@Autowired
	private ProductIngredientRepository productIngredientRepo;

	public List<ProductIngredient> findAllProductIngredients() {
		return productIngredientRepo.findAll();
	}

	public ProductIngredient findProductIngredientById(Integer id) {
		return productIngredientRepo.findById(id).orElse(null);
	}

	public ProductIngredient saveProductIngredient(ProductIngredient productIngredient) {
		return productIngredientRepo.save(productIngredient);
	}

	public void deleteProductIngredientById(Integer id) {
		productIngredientRepo.deleteById(id);
	}
}
