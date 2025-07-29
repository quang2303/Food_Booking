package com.quang.app.JavaWeb_cdquang.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.quang.app.JavaWeb_cdquang.entity.Account;
import com.quang.app.JavaWeb_cdquang.mappers.AccountMapper;

public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	AccountMapper accountMapper;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = accountMapper.findByUsername(username);
		if(account == null) {
			throw new UsernameNotFoundException("Account does not exist");
		}
		
		return User.builder()
				.username(account.getUsername())
				.password(passwordEncoder.encode(account.getPassword()))
				.roles(account.getRole())
				.build();
	}

}
