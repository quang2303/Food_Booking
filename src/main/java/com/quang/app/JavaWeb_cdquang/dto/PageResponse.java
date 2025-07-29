package com.quang.app.JavaWeb_cdquang.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageResponse<T> {
	private List<T> data;
	private Integer totalElements;
	private Integer totalPages;
}
