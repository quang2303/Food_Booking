package com.quang.app.JavaWeb_cdquang.dto;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OrderRequest {
	@NotNull(message = "Name user must not be null")
	@Size(min = 1, max = 100, message = "Name user must be between 1 and 100 characters long")
	private String name;

	@NotNull(message = "Phone order must not be null")
	@Size(min = 1, max = 11, message = "Phone user must be between 1 and 11 characters long")
	private String phone;

	@NotNull(message = "Address order must not be null")
	@Size(min = 10, max = 200, message = "Address user must be between 10 and 100 characters long")
	private String address;

	@NotNull(message = "Email must not be null")
	@Email(message = "Email is not valid")
	private String email;

	@Size(max = 500, message = "Message order must be less than 500 characters long")
	private String message;

	@NotNull(message = "Total price order must not be null")
	@Min(value = 0, message = "Total price must be greater then equal 0")
	private Long totalPrice;

	@NotNull(message = "Fee ship order must not be null")
	@Min(value = 0, message = "Fee ship must be greater then equal 0")
	private Long feeShip;

	@NotNull(message = "List items order must not be null")
	List<ItemsOrderRequest> items;
}
