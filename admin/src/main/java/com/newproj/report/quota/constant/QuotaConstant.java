package com.newproj.report.quota.constant;


public class QuotaConstant {
	public enum QuotaType{
		SCHOOL , EDUINST ;
		public static QuotaType parse( String code ){
			if( code == null ) 
				return null ;
			QuotaType [] types = QuotaType.values() ;
			for( QuotaType type : types ){
				if( type.name().equalsIgnoreCase( code ) )
					return type ;
			}
			return null ;
		}
	}
}
