package com.quang.app.JavaWeb_cdquang.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
	private String status;
	private String message;
	private T data;
}
