package com.newproj.core.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

public class StringUtil {

	public static String getClassName( Class<?> clazz ){
		if( clazz == null ) {
			return null ;
		}
		String fullName = clazz.getName() ;
		if( fullName.lastIndexOf(".") == -1 ){
			return fullName ;
		}
		return fullName.substring( fullName.lastIndexOf(".") + 1 ) ;
	}
	
	public static String genGet( String field ){
		if( isEmpty( field ) ) {
			return null ;
		}
		String method = "get" + (field.charAt(0)+"").toUpperCase() + field.substring( 1 ) ;
		return method ;
	}
	
	public static boolean isEmpty( String str ){
		return str == null || str.trim() .equals("") ;
	}
	public static void main(String[] args ){
		System.out.println( getClassName( StringUtil.class ) ) ;
	}
	
	public static String [] explode( String source , String spec ){
		String [] properties = null ;
		if( source == null || source.trim().equals("") ){
			return null ;
		}
		String [] fields = source.split(spec) ;
		List<String> fieldList = new ArrayList<String>() ;
		for( String field : fields ){
			String temp = field.trim() ;
			if( !temp.equals("") ){
				fieldList.add( temp ) ;
			}
		}
		properties = new String [ fieldList.size() ] ;
		for( int i = 0 ; i < properties.length ; i ++ ){
			properties[ i ] = fieldList.get( i ) ;
		}
		return properties ;
	}
	
	public static String join(String[] ss){
		StringBuilder sb = new StringBuilder();
		for(String s:ss){
			sb.append(s);
			sb.append(',');
		}
		return sb.substring(0, sb.length()-1);
	}
	
	public static String join ( List<?> objects , String separator ){
		if( objects == null || objects.isEmpty() ){
			return null ;
		}
		if( separator == null ) separator = "" ;
		StringBuffer concat = new StringBuffer() ;
		for( Object object : objects ){
			if( object == null ) continue ;
			concat.append( object.toString() + separator ) ;
		}
		if( concat.length() > 0 ){
			concat = new StringBuffer( concat.substring(0, concat.length() - separator.length() ) ) ;
		}
		return concat.toString() ;
	}
	
	public static String toColumnWithHump( String property ){
		if( StringUtils.isEmpty( property ) ) {
			return null ;
		}
		StringBuffer hump = new StringBuffer() ;
		for( int i = 0 ; i < property.length() ; i ++ ){
			if( Character.isUpperCase( property.charAt( i ) ) ){
				hump.append('_') ;
			}
			hump.append( property.charAt( i ) ) ;
		}
		return hump.toString().toUpperCase() ;
	}
	
	public static String toPropertyWithHump( String column ){
		if( StringUtils.isEmpty( column ) ){
			return null ;
		}
		StringBuffer hump = new StringBuffer() ;
		boolean nextUpper = false ;
		for( int i = 0 ; i < column.length() ; i ++ ){
			if( column.charAt( i ) == '_' ){
				nextUpper = true ;
				continue ;
			}
			if( nextUpper ){
				hump.append(  (column.charAt( i ) + "").toUpperCase()  ) ;
			}else{
				hump.append(  (column.charAt( i ) + "").toLowerCase()  ) ;
			}
			nextUpper = false ;
		}
		return hump.toString() ;
	}
	
	public static int getInt( Object obj , Integer ... def ){
		try{
			return Integer.parseInt( obj.toString() ) ;
		}catch(Exception e){
			if( def == null || def.length == 0 ){
				return getInt( 0 ) ;
			}
			return getInt( def[0] , 0 ) ;
		}
	}
	
	public static boolean isEmpty( List<?> list ){
		System.out.println("----------------------------"); 
		return list == null || list.isEmpty() ;
	}
	
	public static String escapHtml( String input ){
		if( StringUtils.isEmpty( input ) )
			return "" ;
		return input.replaceAll("<", "&lt;").replaceAll(">", "&gt;") ;
	}
}
