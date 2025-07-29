package com.quang.app.JavaWeb_cdquang.dto;

import lombok.Data;

@Data
public class OrderResponse {
	private Integer id;
	private Long totalPrice;
	private String status;
	private String createAt;
	private Integer itemCount;
	
	ItemOrderResponse item;
}
