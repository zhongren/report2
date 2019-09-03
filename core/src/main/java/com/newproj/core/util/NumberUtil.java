package com.newproj.core.util;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class NumberUtil {

	public static int parseInt( Object sint , int defvalue ){
		try{
			return Integer.parseInt( sint.toString().trim() ) ;
		}catch(Exception e){
			return defvalue ;
		}
	}
	
	public static BigDecimal parseNumber( Object number , Object defvalue ){
		try{
			return new BigDecimal( number.toString() ) ;
		}catch(Exception e){
			return defvalue == null ? null : new BigDecimal( defvalue.toString() ) ;
		}
	}
	
	public static Object parsePossible( Object number , int ... defvalue ){
		if( number == null ) return null ;
		Object result = defvalue == null || defvalue.length == 0 ? number : defvalue[0] ;
		if( Pattern.matches("^[1-9][0-9]{0,8}$", number.toString() ) ){
			try{
				return Integer.parseInt( number.toString()  ) ;
			}catch(Exception e){
				return result ;
			}
		}else if( Pattern.matches("^[1-9][0-9]{0,8}\\.[0-9]+$", number.toString() ) ){
			try{
				return Float.parseFloat( number.toString()  ) ;
			}catch(Exception e){
				return result ;
			}
		}
		return result ;
	}
	
	public static Float parseFloat( Object number , float defvalue ){
		try{
			return Float.parseFloat( number.toString().trim() ) ;
		}catch(Exception e){
			return defvalue ;
		}
	}
	
}
