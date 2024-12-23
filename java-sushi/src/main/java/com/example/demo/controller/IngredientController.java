package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.model.Ingredient;
import com.example.demo.service.IngredientService;

@RestController
@RequestMapping("/ingredients")
@CrossOrigin
public class IngredientController {

	@Autowired
	private IngredientService ingredientService;

	// 列出所有食材
	@GetMapping
	public List<Ingredient> getAllIngredients() {
		return ingredientService.findAllIngredients();
	}

	// 根據 id 搜尋食材
	@GetMapping("/{id}")
	public ResponseEntity<Ingredient> getIngredientById(@PathVariable Integer id) {
		Ingredient ingredient = ingredientService.findIngredientById(id);
		return ingredient != null ? ResponseEntity.ok(ingredient) : ResponseEntity.notFound().build();
	}

	// 根據食材名稱模糊搜尋
	@GetMapping("/search-by-name")
	public List<Ingredient> searchIngredients(@RequestParam String ingredientName) {
		return ingredientService.findIngredientsByName(ingredientName);
	}

	// 新增食材
	@PostMapping
	public Ingredient createIngredient(@RequestBody Ingredient ingredient) {
		return ingredientService.saveIngredient(ingredient);
	}

	// 更新食材數量
	@PutMapping("/{id}")
	public ResponseEntity<Ingredient> updateIngredient(@PathVariable Integer id, @RequestBody Ingredient ingredient) {
	    // 查找現有的食材
	    Ingredient existingIngredient = ingredientService.findIngredientById(id);
	    if (existingIngredient == null) {
	        return ResponseEntity.notFound().build(); // 返回404未找到
	    }
	    // 僅更新totalQuantity字段
	    existingIngredient.setTotalQuantity(ingredient.getTotalQuantity());

	    // 保存更新後的食材並返回
	    return ResponseEntity.ok(ingredientService.saveIngredient(existingIngredient));
	}

	// 刪除食材
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteIngredient(@PathVariable Integer id) {
		if (ingredientService.findIngredientById(id) == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("未找到食材");
		}
		ingredientService.deleteIngredientById(id);
		return ResponseEntity.ok("食材已成功刪除");
	}
}
