package com.quang.app.JavaWeb_cdquang.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
	private Integer id;
	private String name;
	private String description;
	private Long price;
	private String image;
	private Boolean status;
	private String type;
	
	private OrderItems orderItem;
}
