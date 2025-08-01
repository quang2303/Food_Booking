package com.quang.app.JavaWeb_cdquang.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopItemSoldResponse {
	private String name;
	private String image;
	private Long price;
	private Integer totalSold;
}
