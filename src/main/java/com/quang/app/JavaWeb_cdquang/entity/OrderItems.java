package com.quang.app.JavaWeb_cdquang.entity;

import lombok.Data;

@Data 
public class OrderItems {
	private Integer id;
	private Integer orderId;
	private Integer itemId;
	private Integer quantity;
}
