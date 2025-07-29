package com.quang.app.JavaWeb_cdquang.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.quang.app.JavaWeb_cdquang.entity.Account;

@Mapper
public interface AccountMapper {
	Account findByUsername(String username);
	
	void updatePassword(@Param("newPassword") String newPassword, @Param("username") String username);
	
	boolean checkPassword(@Param("oldPassword") String oldPassword, @Param("username") String username);
}
