package com.example.demo.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

	private Integer OrderId;
	private String userPhone;
	private String orderType;
	private Integer totalPrice;
	private Integer orderStatus;
	private Date createdAt;
	private Date updatedAt;
	private List<OrderDetailsDTO> orderDetails;

}
