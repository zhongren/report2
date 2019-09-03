package com.newproj.report.sys.dto;

import org.hibernate.validator.constraints.NotEmpty;

public class PasswdForgetParam {
	@NotEmpty(message="账号不能为空")
	private String account ;
	@NotEmpty(message="手机号不能为空")
	private String phone ;
	private String verifyCode ;
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getVerifyCode() {
		return verifyCode;
	}
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
}
