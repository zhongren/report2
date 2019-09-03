package com.newproj.report.sys.constant;

public class UserConstant {
	
	public static final String INIT_PASSWD = "123456" ;

	public enum UserStatus {
		ENABLED(0) , DISABLED(1) ;
		private Integer code ;
		UserStatus( Integer code ){
			this.code = code ;
		}
		
		public static UserStatus parse( Integer code ){
			if( code == null ) return null ;
			UserStatus [] status = UserStatus.values() ;
			for( UserStatus state : status ){
				if( code .equals( state.code ) ){
					return state ;
				}
			}
			return null ;
		}

		public Integer getCode() {
			return code;
		}
		
	}
	
	public enum InstType{
		SCHOOL , EDUINST , SYS ;
		public static InstType parse( String name ){
			if( name == null ) return null ;
			InstType [] types = InstType.values() ;
			for( InstType type : types ){
				if( type.name().equalsIgnoreCase( name ) )
					return type ;
			}
			return null ;
		}
	}
}
