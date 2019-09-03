package com.newproj.report.eduinst;

public class EduinstConstant {

	public enum EduinstType{
		CITY , COUNTY , PROVINCE ;
		public static EduinstType parse( String code ){
			if( code == null )
				return null ;
			EduinstType [] types = EduinstType.values() ;
			for( EduinstType type : types ){
				if( type.name().equalsIgnoreCase( code ) ) 
					return type ;
			}
			return null ;
		}
	}
}
