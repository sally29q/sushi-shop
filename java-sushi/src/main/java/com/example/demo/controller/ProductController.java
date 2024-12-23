package com.example.demo.controller;

import com.example.demo.dto.ProductDTO;
import com.example.demo.model.Product;
import com.example.demo.model.ProductIngredient;
import com.example.demo.service.ProductService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

@RestController
@RequestMapping("/products")
@CrossOrigin
public class ProductController {

	@Autowired
	private ProductService productService;

	// 列出所有商品
	@GetMapping
	public List<ProductDTO> getAllProducts() {
		List<Product> produdcts = productService.findAllProducts();
		return transIntoDTO(produdcts);
	}

	// 分頁列出商品
	@GetMapping("/page")
	public Page<Product> getProducts(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		return productService.getProductsByPage(page, size);
	}

	// 根據 id 搜尋商品
	@GetMapping("/{id}")
	public ResponseEntity<ProductDTO> getProductById(@PathVariable Integer id) {
	    Product product = productService.findProductById(id);
	    if (product != null) {
	        ProductDTO productDTO = new ProductDTO(
	            product.getProductId(),
	            product.getProductName(),
	            product.getPrice(),
	            product.getCategory(),
	            product.getPhoto(),
	            product.getProductIngredients().stream()
	                .collect(Collectors.toMap(
	                    pi -> pi.getIngredient().getIngredientName(),
	                    ProductIngredient::getIngredientQuantity
	                ))
	        );
	        return ResponseEntity.ok(productDTO);
	    }
	    return ResponseEntity.notFound().build();
	}


	// 根據商品名稱模糊搜尋
	@GetMapping("/search-by-name")
	public List<Product> searchProductsByName(@RequestParam String productName) {
		return productService.findProductsByName(productName);
	}

	// 根據種類搜尋
	@GetMapping("/search-by-category")
	public List<Product> searchProductsByCategory(@RequestParam String category) {
		return productService.findProductsByCategory(category);
	}

	// 根據種類或商品名稱模糊搜尋
	@GetMapping("/search-by-name-or-category")
	public List<Product> searchProductsByNameOrCategory(@RequestParam(required = false) String productName,
			@RequestParam(required = false) String category) {
		return productService.findProductsByNameOrCategory(productName, category);
	}

	// 新增商品
	@PostMapping
	public ResponseEntity<ProductDTO> saveProduct(@RequestBody ProductDTO productRequest) {
	    try {
	        // 呼叫服務層新增商品
	        Product savedProduct = productService.saveProduct(productRequest);
	        
	        // 將新增的 Product 實體轉換為 ProductDTO
	        ProductDTO savedProductDTO = new ProductDTO(
	                savedProduct.getProductId(),
	                savedProduct.getProductName(),
	                savedProduct.getPrice(),
	                savedProduct.getCategory(),
	                savedProduct.getPhoto(),
	                savedProduct.getProductIngredients().stream()
	                        .collect(Collectors.toMap(pi -> pi.getIngredient().getIngredientName(),
	                                ProductIngredient::getIngredientQuantity))
	        );
	        
	        // 回傳轉換後的 ProductDTO
	        return ResponseEntity.ok(savedProductDTO);
	    } catch (RuntimeException e) {
	        return ResponseEntity.badRequest().body(null);
	    }
	}

	// 更新商品
	@PutMapping("/{id}")
	public ResponseEntity<ProductDTO> updateProduct(@PathVariable Integer id, @RequestBody ProductDTO productRequest) {
	    try {
	        // 呼叫服務層更新商品
	        Product updatedProduct = productService.updateProduct(id, productRequest);
	        ProductDTO productDTO = transIntoDTO(List.of(updatedProduct)).get(0); // 轉換為DTO
	        return ResponseEntity.ok(productDTO);
	    } catch (RuntimeException e) {
	        return ResponseEntity.badRequest().body(null);
	    }
	}

	// 刪除商品
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable Integer id) {
		if (productService.findProductById(id) == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("未找到商品");
		}
		productService.deleteProductById(id);
		return ResponseEntity.ok("商品已成功刪除");
	}

	private List<ProductDTO> transIntoDTO(List<Product> products) {
		return products
				.stream().map(
						product -> new ProductDTO(product.getProductId(),product.getProductName(), product.getPrice(), product.getCategory(),
								product.getPhoto(),
								product.getProductIngredients().stream()
										.collect(Collectors.toMap(pi -> pi.getIngredient().getIngredientName(),
												ProductIngredient::getIngredientQuantity))))
				.collect(Collectors.toList());
	}

}
