package com.quang.app.JavaWeb_cdquang.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quang.app.JavaWeb_cdquang.dto.ApiResponse;
import com.quang.app.JavaWeb_cdquang.dto.UpdatePasswordRequest;
import com.quang.app.JavaWeb_cdquang.service.AccountService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AccountController {
	@Autowired
	AccountService accountService;
	
	@PostMapping("/change_password")
	@Secured("ROLE_ADMIN")
	public ResponseEntity<ApiResponse<String>> updatePassword(@RequestBody @Valid UpdatePasswordRequest updateInfo) {
		accountService.updatePassword(updateInfo);
		ApiResponse<String> response = new ApiResponse<>("success","Successfully", null);
		
		return ResponseEntity.status(200).body(response);
	}
}
