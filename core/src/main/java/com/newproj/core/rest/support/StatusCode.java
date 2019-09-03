package com.newproj.core.rest.support;

public enum StatusCode {
	SUCCESS( 0 , "OK" ) ,
	ERROR( 1 , "ERROR" ) ,
	BAD_REQ( 2 , "Bad Request") ,
	UNAUTH(100000,"Unauthorized!") , 
	NOTEXCEL(1,"不是Excel文件"),
	NULLEXCEL(1,"无效Excel或Excel模板不正确");
	private int code ;
	private String message ;
	StatusCode( int code , String message ){
		this.code = code ;
		this.message = message ;
	}
	public int getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
}
