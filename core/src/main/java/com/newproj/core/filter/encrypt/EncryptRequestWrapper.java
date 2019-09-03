package com.newproj.core.filter.encrypt;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.newproj.core.encrypt.CryptoUtil;
import com.newproj.core.json.JsonSerializer;

public class EncryptRequestWrapper extends HttpServletRequestWrapper{
	
	private final String DECRY_KEY = "www.lebaoedu.com" ;

	private Logger logger = LoggerFactory.getLogger( getClass() ) ;
	
	public EncryptRequestWrapper(ServletRequest request) {
		super((HttpServletRequest)request);
	}
	
	public ServletInputStream getInputStream() throws IOException{
		//对application/json Request body 进行解码 .
		String contentType = super.getHeader("content-type") ;
		if( StringUtils.isEmpty( contentType ) || !contentType.contains("application/json") ){
			return super.getInputStream() ;
		}
		String stringBody = null ;
		try{
			stringBody =  IOUtils.toString( super.getInputStream() , "UTF-8")  ;
		}catch(Exception e){
			logger.error("[Encrypt] Read request body error ." , e );
		} 
		try{
			Map<String,String> json = new JsonSerializer().jsonToMap( stringBody , String.class ) ; 
			//允许非加密请求处理 .
			/*if( !json.containsKey( "encrypt" ) ){
				return getInputStream( stringBody ) ;
			}*/
			stringBody = CryptoUtil.xorDecrypt(  json.get("encrypt") ,  DECRY_KEY ) ;
			if( stringBody == null ){
				throw new Exception() ;
			}
		}catch(Exception e){
			logger.error("[Encrypt] Decrypt request body error ." , e );
			throw new IllegalAccessError( "非法的数据请求格式!" ) ;
		}
		
		return getInputStream( stringBody ) ;
	}
	
	public ServletInputStream getInputStream( String stringBody ){
		ByteArrayInputStream input = new ByteArrayInputStream( stringBody.getBytes() ) ;
		return new EncryptServletInputStream( input ) ;
	}
	
	
	class EncryptServletInputStream extends ServletInputStream{
		private ByteArrayInputStream input ;
		public EncryptServletInputStream( ByteArrayInputStream input ){
			this.input = input ;
		}
		public boolean isFinished() {
			return true;
		}

		public boolean isReady() {
			return true;
		}

		public void setReadListener(ReadListener readListener) {
			
		}

		public int read() throws IOException {
			return input.read();
		}
		
	}

}
