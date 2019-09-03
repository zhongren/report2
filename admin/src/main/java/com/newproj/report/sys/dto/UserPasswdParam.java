package com.newproj.report.sys.dto;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

public class UserPasswdParam {
	@NotEmpty(message="原密码不能为空")
	@Pattern(regexp="^[0-9a-zA-Z]{6,20}$" , message = "原密码格式不正确")
	private String passwd ;
	@NotEmpty(message="新密码不能为空")
	@Pattern(regexp="^[0-9a-zA-Z]{6,20}$" , message = "新密码格式不正确")
	private String newPasswd;
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getNewPasswd() {
		return newPasswd;
	}
	public void setNewPasswd(String newPasswd) {
		this.newPasswd = newPasswd;
	}
}
