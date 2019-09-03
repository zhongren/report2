package com.newproj.core.filter.encrypt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.newproj.core.encrypt.CryptoUtil;
import com.newproj.core.json.JsonSerializer;


public class EncryptResponseWrapper extends HttpServletResponseWrapper{
	
	private final String ENCRY_KEY = "www.lebaoedu.com" ;

	private ByteArrayOutputStream output ;
	private Logger logger = LoggerFactory.getLogger( getClass() ) ;
	
	public EncryptResponseWrapper(ServletResponse response) {
		super((HttpServletResponse)response);
		this.output = new ByteArrayOutputStream() ;
	}
	
	public ServletOutputStream getOutputStream(){
		return new EncryptOutputStream( output ) ;
	}
	
	public void flushBuffer() throws IOException{
		logger.debug("获取加密后响应体.");
		String stringBody = getStringBody() ;
		if( stringBody != null ){
			try{
				getResponse().getWriter().write(  stringBody );
				super.flushBuffer();
			}catch(Exception e){
				logger.error("[Encrypt] Write response error . " , e );
			}
			return ;
		}
		logger.debug("获取响应体(二进制类型)");
		byte[] byteBody = getByteBody() ;
		if( byteBody != null && byteBody.length > 0 ){
			IOUtils.copy( new ByteArrayInputStream( byteBody ) , getResponse().getOutputStream() ) ;
		}
		super.flushBuffer();
	}
	
	public byte[] getByteBody(){
		try{
			return output.toByteArray() ;
		}catch(Exception e){
			return null ;
		}
	}
	
	public String getStringBody() throws IOException {
		logger.debug("判断返回类型：" + super.getResponse().getContentType() );
		if( super.getResponse().getContentType() == null 
				|| !super.getResponse().getContentType().toLowerCase().contains("application/json") ){
			return null ;
		}
		logger.debug("获取返回内容.");
		String stringBody = IOUtils.toString( output.toByteArray() , "UTF-8" ) ;
		if( StringUtils.isEmpty( stringBody ) ){
			return stringBody ;
		}
		logger.debug("加密返回值.");
		try{
			stringBody = CryptoUtil.xorEncrypt( 
					stringBody , ENCRY_KEY ) ;
		}catch(Exception e){
			logger.error("Encrypt Response: Encryp response body error ." , e );
		}
		
		Map<String,String> resultMap = new HashMap<String,String>() ;
		resultMap.put("encrypt", stringBody ) ;
		stringBody = new JsonSerializer().toJson( resultMap ) ;
		setContentLength( stringBody.length() );
		return stringBody ;
	}
	
	public void finalize(){
		try{
			output.close();
		}catch(Exception e){
			
		}
	}
	
	class EncryptOutputStream extends ServletOutputStream{

		private ByteArrayOutputStream output ;
		
		public EncryptOutputStream( ByteArrayOutputStream output ){
			this.output = output ;
		}
		
		public boolean isReady() {
			
			return true;
		}

		public void setWriteListener(WriteListener writeListener) {
			
		}

		public void write(int b ) throws IOException {
			output.write( b );
		}
		
	}
	
}
