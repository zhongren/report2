package com.newproj.core.filter.encrypt;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.newproj.core.filter.FilterConfigurationProperties;

public class EncryptFilter implements Filter{

	private Logger logger = LoggerFactory.getLogger( getClass() ) ;
	
	private Set<String> excludes = new HashSet<String>() ;
	private Set<String> contentTypes = new HashSet<String>() ;
	private Map<String,String> contentTypeMap = new HashMap<String,String>() ;
	
	@Autowired
	private FilterConfigurationProperties propertyConfig;
	
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	public Set<String> getExcludes() {
		return excludes;
	}

	public void setExcludes(Set<String> excludes) {
		this.excludes = excludes;
	}
	
	public Set<String> getContentTypes() {
		return contentTypes;
	}

	public void setContentTypes(Set<String> contentTypes) {
		this.contentTypes = contentTypes;
	}

	public Map<String, String> getContentTypeMap() {
		return contentTypeMap;
	}

	public void setContentTypeMap(Map<String, String> contentTypeMap) {
		this.contentTypeMap = contentTypeMap;
	}

	public boolean but( HttpServletRequest request ){
		//仅仅对get,post,delete,put请求响应进行加密 .
		if( !"get,post,delete,put".contains( request.getMethod().toLowerCase() )  ){
			return true ;
		}
		String url = request.getRequestURI() ;
		if( StringUtils.isEmpty( url ) ) return true ;
		for( String exclude : excludes ){
			if( StringUtils.isEmpty( exclude ) ) {
				continue ;
			}
			if( isMatched( url , exclude ) ) return true ;
		}
		String contentType = request.getHeader("content-type") ;
		if( StringUtils.isEmpty( contentType) || contentTypes.isEmpty() ){
			return false ;
		}
		
		for( String ctype : contentTypes ){
			if( contentType.toLowerCase().contains( ctype.toLowerCase() ) ){
				return true ;
			}
		}
		
		return false ;
	}
	
	private void resetResponseContentType( HttpServletRequest request , HttpServletResponse response ){
		String contentType = request.getHeader("content-type") ;
		if( contentTypeMap.isEmpty() || StringUtils.isEmpty( contentType ) ){
			return ;
		}
		for( Entry<String,String> item : contentTypeMap.entrySet() ){
			if( contentType.toLowerCase().contains( item.getKey().toLowerCase() ) ){
				response.setContentType( item.getValue() );
				logger.debug("reset content type " + item.getValue() );
				break ;
			}
		}
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		if( !propertyConfig.isEnabledEncrypt() ||  but(  (HttpServletRequest) request ) ) {
			chain.doFilter(request, response);
			resetResponseContentType( (HttpServletRequest) request , (HttpServletResponse) response ) ;
			return ;
		}
		
		EncryptRequestWrapper decriptRequest  = new EncryptRequestWrapper( request ) ;
		EncryptResponseWrapper encryptResponse =  new EncryptResponseWrapper( response ) ;
		try{
			chain.doFilter( decriptRequest , encryptResponse );
		}catch(Exception e){
			if( ! e.getCause().getClass().getSimpleName().equalsIgnoreCase("UnknownSessionException") ) throw e ;
		}
		encryptResponse.flushBuffer();
		resetResponseContentType( (HttpServletRequest) request , (HttpServletResponse) response ) ;
		
	}
	private static boolean isMatched( String text , String pattern ){
		if( pattern.indexOf("**") != -1 ){
			pattern = pattern.replace("**", "?[0-9a-zA-Z_\\-\\.\\?=&]*") ;
		}else if( pattern.indexOf("*") != -1 ){
			pattern = pattern.replace("*", "?[0-9a-zA-Z_\\-\\.\\?=&]*$") ;
		}
		Matcher matcher = Pattern.compile( pattern ).matcher( text ) ;
		return matcher.find() ;
	}
	
	public void destroy() {
		
	}

}
