package com.newproj.report.sys.dto;

import org.hibernate.validator.constraints.NotEmpty;

public class UsernameAndPassword {
	@NotEmpty(message="登陆账号不能为空!")
	private String username ;
	@NotEmpty(message="登陆密码不能为空!")
	private String password ;
	private String verifyCode ;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getVerifyCode() {
		return verifyCode;
	}
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
	
}
