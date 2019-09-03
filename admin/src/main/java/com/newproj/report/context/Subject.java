package com.newproj.report.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.newproj.core.beans.BeanUtil;
import com.newproj.report.sys.dto.User;
import com.newproj.report.sys.security.Security;
@Component
public class Subject {
	
	private static Security security ;
	
	@Autowired
	public Subject( Security security ){
		Subject.security = security ;
	}
	
	public static Object getSessionAttribute( String key ){
		return security.getSessionAttribute( key ) ;
	}
	
	public static void setSessionAttribute( String key ,Object value ){
		security.setSessionAttribute(key, value) ;
	}
	
	public static User login( String username , String passwd  ){
		if( username == null || passwd == null )
			return null ;
		return security.doLogin(username, passwd) ;
	}
	
	public static boolean logout(){
		security.doLogout();
		return true ;
	}
	
	public static User getUser(){
		return security.getLoginUser();
	}
	
	public static int getUserId( ){
		return getUser().getId() ;
	}
	
	public static String getUser( String key , String ...def ){
		try{
			return BeanUtil.getProperty( getUser() , key ) ;
		}catch(Exception e){
			return def == null || def.length == 0 ? null : def[0] ;
		}
	}
	
	public static boolean hasPerm( String ... perms ){
		return security.hasPerm( perms ) ;
	}
	
}
