package com.newproj.core.exception;

public class AuthInvalidException extends AbstractBussinessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AuthInvalidException(){
		
	}
	
	public AuthInvalidException( String message ){
		super( GLOBAL_ERROR_CODE , message) ;
	}
	
	public AuthInvalidException( int code , String message) {
		super( code , message) ;
	}
	

}
