package com.quang.app.JavaWeb_cdquang.dto;

import java.util.List;

import lombok.Data;

@Data
public class OrderDetailResponse {
	private Integer id;
	private String name;
	private String phone;
	private String address;
	private String message;
	private Long totalPrice;
	private Long feeShip;
	private String status;
	private String createAt;
	
	private List<ItemOrderResponse> items;
}
