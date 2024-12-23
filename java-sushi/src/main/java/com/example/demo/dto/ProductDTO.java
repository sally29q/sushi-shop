package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

	private Integer productId;
	private String productName;
	private Integer price;
	private String category;
	private byte[] photo;
	private Map<String, Integer> ingredientDetails;

}
