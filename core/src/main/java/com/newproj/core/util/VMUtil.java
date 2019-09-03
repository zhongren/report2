package com.newproj.core.util;

import org.springframework.util.StringUtils;

public class VMUtil {
	public String escapHtml( String input ){
		if( StringUtils.isEmpty( input ) )
			return "" ;
		return input.replaceAll("<", "&lt;").replaceAll(">", "&gt;") ;
	}
	
	public boolean isNull( Object object ){
		return object == null ;
	}
}
