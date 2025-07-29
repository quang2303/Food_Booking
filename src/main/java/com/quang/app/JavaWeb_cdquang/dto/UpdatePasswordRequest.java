package com.quang.app.JavaWeb_cdquang.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatePasswordRequest {
	@NotNull(message = "Old password must not be null")
	@Size(min = 1, max = 100, message = "Old password must be between 1 and 100 characters long")
	private String oldPassword;
	
	@NotNull(message = "New password must not be null")
	@Size(min = 1, max = 100, message = "New password must be between 1 and 100 characters long")
	private String newPassword;
}
