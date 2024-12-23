package com.example.demo.repository;

import com.example.demo.model.Product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository
		extends JpaRepository<Product, Integer>, PagingAndSortingRepository<Product, Integer> {

	// 根據商品名稱模糊搜尋
	List<Product> findByProductNameContaining(String productName);

	// 根據種類搜尋
	List<Product> findByCategory(String category);

	// 根據種類和商品名稱模糊搜尋
	@Query("SELECT p FROM Product p " + "WHERE (:productName IS NULL OR p.productName LIKE %:productName%) "
			+ "AND (:category IS NULL OR p.category = :category)")
	List<Product> findByProductNameOrCategory(@Param("productName") String productName,
			@Param("category") String category);

	Optional<Product> findByProductName(String productName);

}