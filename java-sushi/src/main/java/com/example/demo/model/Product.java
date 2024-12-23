package com.example.demo.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private Integer productId;

	@Column(name = "product_name", nullable = false, length = 50)
	private String productName;

	@Column(name = "price", nullable = false)
	private Integer price;

	@Column(name = "category", length = 20)
	private String category;

	@Column(name = "photo")
	private byte[] photo;

	@JsonIgnore
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.LAZY)
	private Set<ProductIngredient> productIngredients = new HashSet<>();

	public void addProductIngredient(ProductIngredient productIngredient) {
		productIngredients.add(productIngredient);
		productIngredient.setProduct(this);
	}

	public void removeProductIngredient(ProductIngredient productIngredient) {
		productIngredients.remove(productIngredient);
		productIngredient.setProduct(null);
	}
}
