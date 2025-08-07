package com.quang.app.JavaWeb_cdquang.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponse {
	private Integer id;
	private String name;
	private String description;
	private Long price;
	private String image;
	private String type;
}
