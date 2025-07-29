package com.quang.app.JavaWeb_cdquang.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemOrderResponse {
	private String name;
	private String description;
	private String image;
	private Long price;
	private Integer quantity;
}
