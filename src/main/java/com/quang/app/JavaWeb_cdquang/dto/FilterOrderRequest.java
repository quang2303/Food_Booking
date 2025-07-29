package com.quang.app.JavaWeb_cdquang.dto;

import java.util.List;

import com.quang.app.JavaWeb_cdquang.entity.OrderStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FilterOrderRequest {
	@NotNull(message = "Name search must be not null")
	private String nameSearch;
	
	@NotNull(message = "List status must be not null")
	private List<OrderStatus> listStatus;
	
	private String time;
	
	@NotNull(message = "Page search must be not null")
	private Integer page;
	
	@NotNull(message = "Size search must be not null")
	private Integer size;
}
