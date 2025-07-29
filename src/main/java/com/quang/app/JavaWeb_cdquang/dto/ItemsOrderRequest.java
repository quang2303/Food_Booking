package com.quang.app.JavaWeb_cdquang.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemsOrderRequest {
	@NotNull(message = "Id item must not be null")
	private Integer itemId;
	
	@NotNull(message = "Number of item must not be null")
	private Integer numItem;
}
