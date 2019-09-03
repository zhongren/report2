package com.newproj.report.sys.security;

import javax.servlet.http.HttpSession;

public interface SessionDao {
	HttpSession createSession( String token ) ;
	boolean deleteSession() ;
	HttpSession getSession( ) ;
	HttpSession flush( HttpSession session ) ;
}
