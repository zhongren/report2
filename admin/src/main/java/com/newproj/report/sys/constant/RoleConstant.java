package com.newproj.report.sys.constant;

import java.util.HashMap;
import java.util.Map;

public class RoleConstant {
	public enum RoleType{
		SCHOOL,EDUINST,SYS,SUBACC ;
		
		public static RoleType parse( String code ){
			if( code == null ) 
				return null ;
			RoleType [] types = RoleType.values() ;
			for( RoleType type : types ){
				if( type.name().equalsIgnoreCase( code ) )
					return type ;
			}
			return null ;
		}
	}
	
	public enum RoleCode{
		SCHOOL , SYS ,COUNTY , COUNTY_SUBACC , CITY , CITY_SUBACC , PROVINCE , PROVINCE_SUBACC ;
		private static Map<RoleCode,RoleCode> subMap = new HashMap<RoleCode,RoleCode>() ;
		static{
			subMap.put(PROVINCE, PROVINCE_SUBACC);
			subMap.put(CITY, CITY_SUBACC);
			subMap.put(COUNTY, COUNTY_SUBACC);
		}
		public static RoleCode parse( String code ){
			if( code == null ) 
				return null ;
			RoleCode [] types = RoleCode.values() ;
			for( RoleCode type : types ){
				if( type.name().equalsIgnoreCase( code ) )
					return type ;
			}
			return null ;
		}
		
		public static RoleCode subRole( RoleCode role ){
			if( role == null )
				return null ;
			return subMap.get( role ) ;
		}
	}
}
