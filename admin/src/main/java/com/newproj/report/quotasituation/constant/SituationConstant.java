package com.newproj.report.quotasituation.constant;

public class SituationConstant {

	public enum InstType{
		EDUINST , SCHOOL   , COUNTY_EDUINST , CITY_EDUINST , PROVINCE_EDUINST ;
		public InstType parse( String code ){
			if( code == null )
				return null ;
			InstType [] values = InstType.values() ;
			for( InstType value : values ){
				if( code.equalsIgnoreCase( value.name() ) )
					return value ;
			}
			return null ;
		}
	}
	
	public enum SchoolTypeEnum {
		SCHOOL , PRIMARY , MIDDLE ;
		public SchoolTypeEnum parse( String code ){
			if( code == null )
				return null ;
			SchoolTypeEnum [] values = SchoolTypeEnum.values() ;
			for( SchoolTypeEnum value : values ){
				if( code.equalsIgnoreCase( value.name() ) )
					return value ;
			}
			return null ;
		}
	}
}
