package com.newproj.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
	
	private static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss" ;

	public static long getTimeSeconds( Date date ){
		return date.getTime() / 1000 ;
	}
	
	public static String now( String format ){
		return new SimpleDateFormat( format ).format( new Date() ) ;
	}
	
	public static String now(){
		return now(  DATE_TIME) ;
	}
	
	public static Date dateTime( String time  , String format ){
		try{
			return new SimpleDateFormat( format ).parse( time ) ;
		}catch(Exception e){
			return null ;
		}
	}
	
	public static long getTimeSeconds(){
		return getTimeSeconds( new Date() ) ;
	}
	
	public static Date str2date( String source ){
		Date date = null ;
		try{
			date = new SimpleDateFormat( DATE_TIME ).parse( source ) ;
		}catch(Exception e){
			e.printStackTrace();
		}
		return date ;
	}
	
	public static String format( Date date , String pattern ){
		if( date == null || pattern == null ){
			return null ;
		}
		return new SimpleDateFormat( pattern ).format( date ) ;
	}
	
	public static String formatFromSeconds( Long seconds , String pattern ){
		if( seconds == null ) return null ;
		Date date = new Date() ;
		date.setTime( seconds * 1000 );
		return format( date , pattern ) ;
	}
}
