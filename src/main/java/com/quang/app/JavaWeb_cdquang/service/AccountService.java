package com.quang.app.JavaWeb_cdquang.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quang.app.JavaWeb_cdquang.dto.UpdatePasswordRequest;
import com.quang.app.JavaWeb_cdquang.exception.OperationFailedException;
import com.quang.app.JavaWeb_cdquang.mappers.AccountMapper;

@Service
public class AccountService {
	@Autowired
	AccountMapper accountMapper;
	
	@Transactional
	public void updatePassword(UpdatePasswordRequest updateInfo) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		
		if(!accountMapper.checkPassword(updateInfo.getOldPassword(), username)) {
			throw new OperationFailedException("Password incorection");
		}
		
		try {
			accountMapper.updatePassword(updateInfo.getNewPassword(), username);
		} catch (Exception e) {
			throw new RuntimeException("Failure to change password");
		}
	}
}
