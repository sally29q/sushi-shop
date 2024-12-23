package com.example.demo.service;

import com.example.demo.dto.ProductDTO;
import com.example.demo.model.Ingredient;
import com.example.demo.model.Product;
import com.example.demo.model.ProductIngredient;
import com.example.demo.repository.IngredientRepository;
import com.example.demo.repository.ProductRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepo;

	@Autowired
	private IngredientRepository ingredientRepo;

	// 列出所有商品
	public List<Product> findAllProducts() {
		return productRepo.findAll();
	}

	// 分頁列出商品
	public Page<Product> getProductsByPage(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return productRepo.findAll(pageable);
	}

	// 根據 id 搜尋商品
	public Product findProductById(Integer id) {
		return productRepo.findById(id).orElse(null);
	}

	// 根據商品名稱模糊搜尋
	public List<Product> findProductsByName(String productName) {
		return productRepo.findByProductNameContaining(productName);
	}

	// 根據種類搜尋
	public List<Product> findProductsByCategory(String category) {
		return productRepo.findByCategory(category);
	}

	// 根據種類或商品名稱模糊搜尋
	public List<Product> findProductsByNameOrCategory(String productName, String category) {
		return productRepo.findByProductNameOrCategory(productName, category);
	}

	// 新增商品
	public Product saveProduct(ProductDTO productRequest) {
		Product product = new Product();
		product.setProductName(productRequest.getProductName());
		product.setPrice(productRequest.getPrice());
		product.setCategory(productRequest.getCategory());
		product.setPhoto(productRequest.getPhoto());

		Set<ProductIngredient> productIngredients = new HashSet<>();
		productRequest.getIngredientDetails().forEach((ingredientName, quantity) -> {
			Ingredient ingredient = ingredientRepo.findByName(ingredientName);
			if (ingredient == null) {
				throw new RuntimeException("Ingredient not found: " + ingredientName);
			}
			ProductIngredient productIngredient = new ProductIngredient();
			productIngredient.setIngredient(ingredient);
			productIngredient.setProduct(product);
			productIngredient.setIngredientQuantity(quantity);
			productIngredients.add(productIngredient);
		});

		product.setProductIngredients(productIngredients);
		return productRepo.save(product);
	}

	// 更新商品
	public Product updateProduct(Integer productId, ProductDTO productRequest) {
		Product existingProduct = productRepo.findById(productId)
				.orElseThrow(() -> new RuntimeException("Product not found: " + productId));
		existingProduct.setProductName(productRequest.getProductName());
		existingProduct.setPrice(productRequest.getPrice());
		existingProduct.setCategory(productRequest.getCategory());
		existingProduct.setPhoto(productRequest.getPhoto());

		Set<ProductIngredient> updatedProductIngredients = new HashSet<>();
		productRequest.getIngredientDetails().forEach((ingredientName, quantity) -> {
			Ingredient ingredient = ingredientRepo.findByName(ingredientName);
			if (ingredient == null) {
				throw new RuntimeException("Ingredient not found: " + ingredientName);
			}
			ProductIngredient productIngredient = existingProduct.getProductIngredients().stream()
					.filter(pi -> pi.getIngredient().getIngredientId().equals(ingredient.getIngredientId())).findFirst()
					.orElse(new ProductIngredient());
			productIngredient.setIngredient(ingredient);
			productIngredient.setProduct(existingProduct);
			productIngredient.setIngredientQuantity(quantity);
			updatedProductIngredients.add(productIngredient);
		});

		existingProduct.getProductIngredients().clear();
		existingProduct.getProductIngredients().addAll(updatedProductIngredients);

		return productRepo.save(existingProduct);
	}

	// 刪除商品
	public void deleteProductById(Integer id) {
		productRepo.deleteById(id);
	}
}
