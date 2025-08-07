package com.quang.app.JavaWeb_cdquang.entity;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Order {
	private Integer id;
	private String address;
	private String phone;
	private String name;
	private String email;
	private String message;
	private Long totalPrice;
	private LocalDateTime createAt;
	private OrderStatus status;
	private Long feeShip;
	
	private List<Item> items;
	
	private Item item;
	private Integer itemCount;
}
