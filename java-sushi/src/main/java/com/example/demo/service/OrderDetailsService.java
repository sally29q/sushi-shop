package com.example.demo.service;

import com.example.demo.model.OrderDetails;
import com.example.demo.repository.OrderDetailsRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailsService {

	@Autowired
	private OrderDetailsRepository orderDetailsRepo;

	// 查詢所有訂單明细
	public List<OrderDetails> findAllOrderDetails() {
		return orderDetailsRepo.findAll();
	}

	// 根據 id 查詢訂單明细
	public OrderDetails findOrderDetailsById(Integer id) {
		return orderDetailsRepo.findById(id).orElse(null);
	}

	// 建立或修改訂單明细
	public OrderDetails saveOrderDetails(OrderDetails orderDetails) {
		return orderDetailsRepo.save(orderDetails);
	}

	// 刪除訂單明细
	public void deleteOrderDetailsById(Integer id) {
		orderDetailsRepo.deleteById(id);
	}
}
