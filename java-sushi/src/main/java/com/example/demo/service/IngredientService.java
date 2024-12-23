package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Ingredient;
import com.example.demo.repository.IngredientRepository;

@Service
public class IngredientService {

	@Autowired
	private IngredientRepository ingredientRepo;

	// 列出所有食材
	public List<Ingredient> findAllIngredients() {
		return ingredientRepo.findAll();
	}

	// 根據 id 搜尋食材
	public Ingredient findIngredientById(Integer id) {
		return ingredientRepo.findById(id).orElse(null);
	}

	// // 根據食材名稱模糊搜尋
	public List<Ingredient> findIngredientsByName(String ingredientName) {
		return ingredientRepo.findByIngredientNameContaining(ingredientName);
	}

	// 新增或更新食材
	public Ingredient saveIngredient(Ingredient ingredient) {
		return ingredientRepo.save(ingredient);
	}

	// 刪除食材
	public void deleteIngredientById(Integer id) {
		ingredientRepo.deleteById(id);
	}
}
