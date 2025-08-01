package com.quang.app.JavaWeb_cdquang.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDocument {
	private String id;
	private String name;
	private String description;
	private String image;
	private Long price;
	private Integer quantity;
}
