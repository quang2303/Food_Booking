package com.quang.app.JavaWeb_cdquang.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InfoDasboardResponse {
	private Long todaySale;
	private Integer totalOrder;
	private Integer numShipping;
	private Integer numCompleted;
	private Integer numCanceled;
}
