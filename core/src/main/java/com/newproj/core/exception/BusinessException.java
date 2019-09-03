package com.newproj.core.exception;

/**
 * 全局业务异常处理.
 * 异常code,message配置方式:
 * 1、code-message 枚举配置;
 * 2、配置文件properties配置异常信息,格式key=code;message;
 * 
 * @author chenglong
 *
 */
public class BusinessException extends AbstractBussinessException {

	private static final long serialVersionUID = 1L;

	public BusinessException( String message ){
		super( GLOBAL_ERROR_CODE , message ) ;
	}
	public BusinessException( int code,String message ){
		super( code , message ) ;
	}
	public BusinessException( String message , Exception e ){
		super( GLOBAL_ERROR_CODE , message , e ) ;
	}
	public BusinessException( GLOBAL_EXP gloalExp ){
		super( gloalExp.getCode() , gloalExp.getMessage() ) ;
	}
	public BusinessException( GLOBAL_EXP gloalExp , Exception e){
		super( gloalExp.getCode() , gloalExp.getMessage() ,e) ;
	}
	
	public enum GLOBAL_EXP{
		GLOBAL_SERVER_ERROR( 500 , "服务器内部错误!") ;
		private int code ;
		private String message ;
		GLOBAL_EXP( int code , String message ){
			this.code = code ;
			this.message = message ;
		}
		public int getCode() {
			return code;
		}
		public void setCode(int code) {
			this.code = code;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
	}
}
