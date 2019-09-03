package com.newproj.core.rest.support;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newproj.core.json.JsonSerializer;

/**
 * 
 *
 */
public class RestActionSupporter{

	protected Logger logger = LoggerFactory.getLogger( getClass() ) ;
	private ThreadLocal<JsonSerializer> jsonSerializer = new ThreadLocal<JsonSerializer>() ;
	
	protected String response( int code , String message , Object data , Object extra ) {
		if( jsonSerializer.get() == null ){
			jsonSerializer.set( new JsonSerializer().filterNullValues() );
		}
		ResultModal result = new ResultModal( code , message , data , extra ) ;
		String response  = jsonSerializer.get().toJson( result ) ;
		response = handleDateText( response ) ;
		jsonSerializer.remove();
		return response ;
	};
	
	protected void  output( HttpServletResponse response , String output ){
		PrintWriter writer = null ;
		try{
			writer = response.getWriter() ;
			writer.write( output );
		}catch(Exception e){}
		finally{
			writer.flush() ;
			writer.close() ;
		}
	}
	
	private String handleDateText( String input ){
		Pattern pattern = Pattern.compile("([0-9]{4}-[0-9]{2}-[0-9]{2}\\s[0-9]{2}:[0-9]{2}:[0-9]{2})(\\.0)") ;
		Matcher matcher = pattern.matcher( input ) ;
		while( matcher.find() ){
			input = input.replace( matcher.group(0), matcher.group(1) ) ;
		}
		return input ;
	}
	
	protected String success( Object data , Object extra ){
		return response( StatusCode.SUCCESS.getCode() , StatusCode.SUCCESS.getMessage() , data , extra ) ;
	}
	
	protected String success( Object data ){
		return success( data , null ) ;
	}
	
	protected String error( StatusCode errorCode  ){
		return response( errorCode.getCode() , errorCode.getMessage() , null , null ) ;
	}
	
	protected String error(int code ,  String message ){
		return response( code , message , null , null ) ;
	}
	
	
	protected RestActionSupporter include( Class<?> clazz , String ... fields ){
		if( clazz == null || fields == null || fields.length == 0 ){
			return this ;
		}
		if( jsonSerializer.get() == null ){
			jsonSerializer.set( new JsonSerializer().filterNullValues());
		}
		jsonSerializer.get().include( clazz , fields ) ;
		return this ;
	}
	
	protected RestActionSupporter exclude( Class<?> clazz , String ... fields ){
		if( clazz == null || fields == null || fields.length == 0 ){
			return this ;
		}
		if( jsonSerializer.get() == null ){
			jsonSerializer.set( new JsonSerializer() );
		}
		jsonSerializer.get().exclude( clazz , fields ) ;
		return this ;
	}
	
	protected void vagueField( Map<String,Object> param , String ... fields ){
		if( param == null || param.isEmpty() || fields == null || fields.length == 0 )
			return ;
		Map<String,Object> searchParam = new HashMap<String,Object>() ;
		List<String> temp = Arrays.asList( fields ) ;
		for( Map.Entry<String, Object> entry : param.entrySet() ){
			if( !temp.contains( entry.getKey() ) ){
				searchParam.put( entry.getKey() , entry.getValue() ) ;
				continue ;
			}
			String key = String.format("?%s", entry.getKey() ) ;
			searchParam.put(key, entry.getValue() ) ; 
		}
		param.clear(); 
		param.putAll( searchParam );
	}
	
}
