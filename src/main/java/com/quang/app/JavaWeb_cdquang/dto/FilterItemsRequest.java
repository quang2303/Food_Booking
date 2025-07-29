package com.quang.app.JavaWeb_cdquang.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FilterItemsRequest {
	private String name;
	
	@NotNull(message = "Type item must be not null")
	private String type;
	private String sortByPrice;
	
	@NotNull(message = "Page item must be not null")
	private Integer page;
	
	@NotNull(message = "Size item must be not null")
	private Integer size;
}
