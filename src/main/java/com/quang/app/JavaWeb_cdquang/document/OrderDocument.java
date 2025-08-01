package com.quang.app.JavaWeb_cdquang.document;

import java.util.List;

import lombok.Data;

@Data
public class OrderDocument {
	private Integer id;
	private String name;
	private String phone;
	private String address;
	private String message;
	private Long totalPrice;
	private Long feeShip;
	private String status;
	private String createAt;
	private List<OrderItemDocument> items;
}
