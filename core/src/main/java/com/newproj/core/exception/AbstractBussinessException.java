package com.newproj.core.exception;

public class AbstractBussinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	protected final static int GLOBAL_ERROR_CODE = 1 ;
	
	private int code ;
	private Throwable cause ;
	
	public AbstractBussinessException( int code , String message ){
		super( message ) ;
		this.code = code ;
	}
	public AbstractBussinessException( int code , String message , Throwable cause ){
		super( message ) ;
		this.code = code ;
		this.cause = cause ;
	}
	public AbstractBussinessException(){
		super() ;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public Throwable getCause() {
		return cause;
	}
	public void setCause(Throwable cause) {
		this.cause = cause;
	}
	
	
}
