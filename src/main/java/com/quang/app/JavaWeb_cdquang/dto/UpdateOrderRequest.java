package com.quang.app.JavaWeb_cdquang.dto;

import com.quang.app.JavaWeb_cdquang.entity.OrderStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderRequest {
	@NotNull(message = "Order id update must be not null")
	private Integer orderId;
	
	@NotNull(message = "Order Status update must be not null")
	private OrderStatus status;
}
