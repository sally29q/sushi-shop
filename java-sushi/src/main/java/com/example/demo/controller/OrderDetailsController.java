package com.example.demo.controller;

import com.example.demo.model.OrderDetails;
import com.example.demo.service.OrderDetailsService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order-details")
@CrossOrigin
public class OrderDetailsController {

	@Autowired
	private OrderDetailsService orderDetailsService;

	// 查詢所有訂單明细
	@GetMapping
	public List<OrderDetails> getAllOrderDetails() {
		return orderDetailsService.findAllOrderDetails();
	}

	// 根據 id 查詢訂單明细
	@GetMapping("/{id}")
	public ResponseEntity<OrderDetails> getOrderDetailsById(@PathVariable Integer id) {
		OrderDetails orderDetails = orderDetailsService.findOrderDetailsById(id);
		return orderDetails != null ? ResponseEntity.ok(orderDetails) : ResponseEntity.notFound().build();
	}

	// 建立訂單明细
	@PostMapping
	public OrderDetails createOrderDetails(@RequestBody OrderDetails orderDetails) {
		return orderDetailsService.saveOrderDetails(orderDetails);
	}

	// 修改訂單明细
	@PutMapping("/{id}")
	public ResponseEntity<OrderDetails> updateOrderDetails(@PathVariable Integer id,
			@RequestBody OrderDetails updatedDetails) {
		OrderDetails existingDetails = orderDetailsService.findOrderDetailsById(id);
		if (existingDetails == null) {
			return ResponseEntity.notFound().build();
		}
		existingDetails.setQuantity(updatedDetails.getQuantity());
		existingDetails.setPrice(updatedDetails.getPrice());
		existingDetails.setSubtotal(updatedDetails.getSubtotal());
		return ResponseEntity.ok(orderDetailsService.saveOrderDetails(existingDetails));
	}

	// 刪除訂單明细
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteOrderDetails(@PathVariable Integer id) {
		if (orderDetailsService.findOrderDetailsById(id) == null) {
			return ResponseEntity.notFound().build();
		}
		orderDetailsService.deleteOrderDetailsById(id);
		return ResponseEntity.noContent().build();
	}
}
