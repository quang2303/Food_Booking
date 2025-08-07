package com.quang.app.JavaWeb_cdquang.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
	private List<T> data;
	private Integer totalElements;
	private Integer totalPages;
}
