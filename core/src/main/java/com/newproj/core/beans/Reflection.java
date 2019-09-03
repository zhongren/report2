package com.newproj.core.beans;

import java.util.List;

public class Reflection {

	private static final String SET_PREFIX = "set" ;
	private static final String GET_PREFIX = "get" ;
	
	public static void setFieldValue( Object bean , String fieldName , Object value ){
		if( bean == null || fieldName == null || fieldName.trim().equals("") ){
			throw new IllegalArgumentException("Illegal argument , Empty value ") ;
		}
		Class<?> clazz = bean.getClass() ;
		Class<?> type = null ;
		try{
			type = clazz.getField( fieldName ).getType() ;
			String methodName = getSMethodName( fieldName ) ;
			clazz.getMethod( methodName , type ).invoke( bean , value ) ;
		}catch(Exception e){
			// Ignore ...
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getFieldValue( Object bean , String fieldName ){
		if( bean == null || fieldName == null || fieldName.trim().equals("") ){
			throw new IllegalArgumentException("Illegal argument , Empty value !") ;
		}
		Class<?> clazz = bean.getClass() ;
		T value = null ;
		try{
			value = (T) clazz.getMethod( getGMethodName( fieldName ) ).invoke( bean ) ;
		}catch(Exception e){
			//Ignore ...
		}
		return value ;
	}
	
	public static String getSMethodName( String fieldName ){
		if( fieldName == null || fieldName.trim().equals("") ){
			return null ;
		}
		return SET_PREFIX + (fieldName.charAt(0)+"").toUpperCase() + fieldName.substring( 1 ) ;
	}
	
	public static String getGMethodName( String fieldName ){
		if( fieldName == null || fieldName.trim().equals("") ){
			return null ;
		}
		return GET_PREFIX + (fieldName.charAt(0)+"").toUpperCase() + fieldName.substring( 1 ) ;
	}
	
	public static boolean isList( Object object ){
		if( object != null && object instanceof List ){
			return true ;
		}
		return false ;
	}
}
