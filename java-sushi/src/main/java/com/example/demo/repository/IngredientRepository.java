package com.example.demo.repository;

import com.example.demo.model.Ingredient;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {

	// 根據 ingredientName 查詢
	@Query("SELECT i FROM Ingredient i WHERE i.ingredientName = :name")
	Ingredient findByName(String name);

	// 根據 ingredientName 模糊查詢
	List<Ingredient> findByIngredientNameContaining(String ingredientName);

}