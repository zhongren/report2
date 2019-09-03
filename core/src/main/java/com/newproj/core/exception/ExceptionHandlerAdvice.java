package com.newproj.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.newproj.core.json.JsonSerializer;
import com.newproj.core.rest.support.ResultModal;
import com.newproj.core.rest.support.StatusCode;

@ControllerAdvice
public class ExceptionHandlerAdvice {

	private JsonSerializer jsonSerializer = new JsonSerializer().filterNullValues() ;

	@ExceptionHandler( MethodArgumentNotValidException.class )
	public ResponseEntity<String> handleArgumentNotValidException( MethodArgumentNotValidException exp ){
		FieldError error = exp.getBindingResult().getFieldErrors().get(0) ;
		ResultModal result = new ResultModal(
				StatusCode.BAD_REQ.getCode() , error.getDefaultMessage() , error.getField() , null ) ;
		return new ResponseEntity<String>( jsonSerializer.toJson( result ) , HttpStatus.OK  ) ;
	}
	
	@ExceptionHandler( BusinessException.class )
	public ResponseEntity<String> handleBusinessException( BusinessException exp ){
		ResultModal result = new ResultModal( exp.getCode() , exp.getMessage() ) ;
		return  new ResponseEntity<String>( jsonSerializer.toJson( result ) , HttpStatus.OK  ) ;
	}
	
	@ExceptionHandler( AuthInvalidException.class )
	public ResponseEntity<String> handleAuthInvalidException( AuthInvalidException exp ){
		ResultModal result = new ResultModal( exp.getCode() , exp.getMessage() ) ;
		return  new ResponseEntity<String>( jsonSerializer.toJson( result ) , HttpStatus.UNAUTHORIZED  ) ;
	}
	
	public ResponseEntity<String> handleRuntimeException( RuntimeException exp ){
		ResultModal result = new ResultModal( StatusCode.ERROR.getCode() , exp.getMessage() ) ;
		return new ResponseEntity<String>( jsonSerializer.toJson( result) , HttpStatus.INTERNAL_SERVER_ERROR ) ;
	}
}
