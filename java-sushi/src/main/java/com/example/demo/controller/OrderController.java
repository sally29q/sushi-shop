package com.example.demo.controller;

import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderDetailsDTO;
import com.example.demo.model.Order;
import com.example.demo.service.OrderService;
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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
@CrossOrigin
public class OrderController {

	@Autowired
	private OrderService orderService;

	// 查詢所有訂單
	@GetMapping
	public List<OrderDTO> getAllOrders() {
		List<Order> orders = orderService.findAllOrders();
		return transIntoDTO(orders);
	}

	// 根據 id 查詢訂單
	@GetMapping("/{id}")
	public ResponseEntity<OrderDTO> getOrderById(@PathVariable Integer id) {
	    Order order = orderService.findOrderById(id);
	    
	    if (order != null) {
	        // 將 Order 轉換為 OrderDTO
	        OrderDTO orderDTO = new OrderDTO();
	        orderDTO.setOrderId(order.getOrderId());
	        orderDTO.setUserPhone(order.getUserPhone());
	        orderDTO.setOrderType(order.getOrderType());
	        orderDTO.setTotalPrice(order.getTotalPrice());
	        orderDTO.setOrderStatus(order.getOrderStatus());
	        orderDTO.setCreatedAt(order.getCreatedAt());
	        orderDTO.setUpdatedAt(order.getUpdatedAt());

	        // 將 Order 的訂單詳情轉換為 OrderDetailsDTO 列表
	        List<OrderDetailsDTO> orderDetailsDTOList = order.getOrderDetails().stream()
	            .map(orderDetail -> new OrderDetailsDTO(
	                orderDetail.getProduct().getProductName(),
	                orderDetail.getQuantity(),
	                orderDetail.getProduct().getPrice(),
	                orderDetail.getSubtotal()
	            ))
	            .collect(Collectors.toList());
	        
	        orderDTO.setOrderDetails(orderDetailsDTOList);
	        
	        return ResponseEntity.ok(orderDTO);
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}


	// 分頁查詢訂單
	@GetMapping("/page")
	public ResponseEntity<Page<Order>> getOrdersByPage(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Page<Order> orders = orderService.findOrdersByPage(page, size);
		return new ResponseEntity<>(orders, HttpStatus.OK);
	}

	// 建立訂單
	@PostMapping
	public ResponseEntity<Order> createOrder(@RequestBody OrderDTO orderRequest) {
		try {
			Order savedOrder = orderService.saveOrder(orderRequest);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	// 修改訂單
	@PutMapping("/{orderId}")
	public ResponseEntity<OrderDTO> updateOrder(@PathVariable Integer orderId, @RequestBody OrderDTO orderRequest) {
	    try {
	        // 更新订单
	        Order updatedOrder = orderService.updateOrder(orderId, orderRequest);

	        if (updatedOrder != null) {
	            // 将更新后的 Order 转换为 OrderDTO
	            OrderDTO orderDTO = new OrderDTO();
	            orderDTO.setOrderId(updatedOrder.getOrderId());
	            orderDTO.setUserPhone(updatedOrder.getUserPhone());
	            orderDTO.setOrderType(updatedOrder.getOrderType());
	            orderDTO.setTotalPrice(updatedOrder.getTotalPrice());
	            orderDTO.setOrderStatus(updatedOrder.getOrderStatus());
	            orderDTO.setCreatedAt(updatedOrder.getCreatedAt());
	            orderDTO.setUpdatedAt(updatedOrder.getUpdatedAt());

	            // 将 Order 的订单详情转换为 OrderDetailsDTO 列表
	            List<OrderDetailsDTO> orderDetailsDTOList = updatedOrder.getOrderDetails().stream()
	                .map(orderDetail -> new OrderDetailsDTO(
	                		orderDetail.getProduct().getProductName(),
	    	                orderDetail.getQuantity(),
	    	                orderDetail.getProduct().getPrice(),
	    	                orderDetail.getSubtotal()
	                ))
	                .collect(Collectors.toList());

	            orderDTO.setOrderDetails(orderDetailsDTOList);

	            return ResponseEntity.ok(orderDTO);
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	        }

	    } catch (RuntimeException e) {
	        // 处理更新失败的情况
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	    }
	}


	// 刪除訂單
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteOrder(@PathVariable Integer id) {
		if (orderService.findOrderById(id) == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("未找到訂單");
		}
		orderService.deleteOrderById(id);
		return ResponseEntity.ok("訂單已成功刪除");
	}
	
	// transIntoDTO 方法
	private List<OrderDTO> transIntoDTO(List<Order> orders) {
		return orders.stream()
				.map(order -> new OrderDTO(order.getOrderId(),order.getUserPhone(), order.getOrderType(), order.getTotalPrice(),
						order.getOrderStatus(), order.getCreatedAt(), order.getUpdatedAt(),
						order.getOrderDetails().stream()
								.map(orderDetails -> new OrderDetailsDTO(orderDetails.getProduct().getProductName(),
										orderDetails.getQuantity(), orderDetails.getProduct().getPrice(),
										orderDetails.getSubtotal()))
								.collect(Collectors.toList())))
				.collect(Collectors.toList());
	}

}
