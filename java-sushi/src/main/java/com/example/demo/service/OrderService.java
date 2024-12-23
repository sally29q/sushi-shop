package com.example.demo.service;

import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderDetailsDTO;
import com.example.demo.model.Order;
import com.example.demo.model.OrderDetails;
import com.example.demo.model.Product;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepo;

	@Autowired
	private ProductRepository productRepo;

	// 查詢所有訂單
	public List<Order> findAllOrders() {
		return orderRepo.findAll();
	}

	// 根據 id 查詢訂單
	public Order findOrderById(Integer id) {
		return orderRepo.findById(id).orElse(null);
	}

	// 分頁查詢訂單
	public Page<Order> findOrdersByPage(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return orderRepo.findAll(pageable);
	}

	// 建立訂單
	public Order saveOrder(OrderDTO orderRequest) {
		Order order = new Order();
		mapOrderFromDTO(order, orderRequest);

		Set<OrderDetails> orderDetails = mapOrderDetailsFromDTO(order, orderRequest);
		order.setOrderDetails(orderDetails);

		return orderRepo.save(order);
	}

	// 修改訂單
	public Order updateOrder(Integer orderId, OrderDTO orderRequest) {
		Order existingOrder = orderRepo.findById(orderId)
				.orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
		mapOrderFromDTO(existingOrder, orderRequest);

		Set<OrderDetails> updatedOrderDetails = mapOrderDetailsFromDTO(existingOrder, orderRequest);
		existingOrder.getOrderDetails().clear();
		existingOrder.getOrderDetails().addAll(updatedOrderDetails);

		return orderRepo.save(existingOrder);
	}

	// 刪除訂單
	public void deleteOrderById(Integer id) {
		Order order = findOrderById(id);
		if (order != null) {
			orderRepo.delete(order);
		}
	}

	// mapOrderFromDTO 方法
	private void mapOrderFromDTO(Order order, OrderDTO orderRequest) {
		order.setUserPhone(orderRequest.getUserPhone());
		order.setOrderType(orderRequest.getOrderType());
		order.setTotalPrice(orderRequest.getTotalPrice());
		order.setOrderStatus(orderRequest.getOrderStatus());
		order.setCreatedAt(orderRequest.getCreatedAt() != null ? orderRequest.getCreatedAt() : new Date());
		order.setUpdatedAt(orderRequest.getUpdatedAt() != null ? orderRequest.getUpdatedAt() : new Date());
	}

	// mapOrderDetailsFromDTO 方法
	private Set<OrderDetails> mapOrderDetailsFromDTO(Order order, OrderDTO orderRequest) {
		Set<OrderDetails> orderDetailsSet = new HashSet<>();

		for (OrderDetailsDTO detailsDTO : orderRequest.getOrderDetails()) {
			Product product = productRepo.findByProductName(detailsDTO.getProductName())
					.orElseThrow(() -> new RuntimeException("Product not found: " + detailsDTO.getProductName()));

			OrderDetails orderDetails = new OrderDetails();
			orderDetails.setOrder(order);
			orderDetails.setProduct(product);
			orderDetails.setQuantity(detailsDTO.getQuantity());
			orderDetails.setPrice(detailsDTO.getPrice());
			orderDetails.setSubtotal(detailsDTO.getSubtotal());
			orderDetailsSet.add(orderDetails);
		}
		return orderDetailsSet;
	}

}
