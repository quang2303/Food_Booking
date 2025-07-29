package com.quang.app.JavaWeb_cdquang.dto;

import org.springframework.web.multipart.MultipartFile;

import com.quang.app.JavaWeb_cdquang.validation.ValidImage;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateItemRequest {
	@NotNull(message = "Name item must not be null")
	@Size(min = 1, max = 100, message = "Name item must be between 1 and 100 characters long")
	private String name;
	
	@Size(max = 500, message = "Description item must be less than 500 characters long")
	private String description;
	
	@NotNull(message = "Price item must not be null")
	@Min(value = 0, message = "Price should be greater than equal 0")
	@Max(value = 999999999, message = "The price must be less than or equal to 10 digits")
	private Long price;
	
	@ValidImage
	private MultipartFile image;
	
	@NotNull(message = "Type item must not be null")
	@NotBlank(message = "Type item must not be empty")
	private String type;
}
