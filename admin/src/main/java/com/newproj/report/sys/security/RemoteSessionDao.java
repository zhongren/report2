package com.newproj.report.sys.security;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.newproj.core.cache.Cache;
import com.newproj.core.encrypt.CryptoUtil;
import com.newproj.core.util.WebUtil;

@Component
public class RemoteSessionDao implements SessionDao {

	@Autowired
	private Cache cache ;
	
	private static final long SESS_TIME_OUT = 1 * 3600 * 1000l  ;
	private static final String TOKEN_CIPHER = "reporting.token.cipher" ;
	private static final String TOKEN_NAME = "Access-Token"  ;
	
	public HttpSession createSession( String token ) {
		if( token == null )
			token = "token#"+System.currentTimeMillis() ;
		RemoteSession session = new RemoteSession( token ) ;
		WebUtil.addCookie( WebUtil.resolveResponse() , TOKEN_NAME ,
				CryptoUtil.xorEncrypt( session.getId() ,  TOKEN_CIPHER ) , "/",  (int)SESS_TIME_OUT );
		return cache.put( token , session , SESS_TIME_OUT ) ;
	}
	
	private static String getToken( ){
		String token = null ;
		try{
			if( ( token = WebUtil.getCookieValue( WebUtil.resolveRequest() , TOKEN_NAME ) ) != null ){
				return CryptoUtil.xorDecrypt(token, TOKEN_CIPHER) ;
			}
			if( ( token = WebUtil.getHeader( WebUtil.resolveRequest()  , TOKEN_NAME ) ) != null ){
				return CryptoUtil.xorDecrypt(token, TOKEN_CIPHER) ;
			}
			if( ( token = WebUtil.getParameter( TOKEN_NAME ) ) != null ){
				return CryptoUtil.xorDecrypt(token, TOKEN_CIPHER) ;
			}
		}catch(Exception e){
			//Ignore ...
		}
		return null ;
	}
	
	@Override
	public boolean deleteSession() {
		String token = getToken() ;
		if( token == null ) return true ;
		cache.del( token ) ;
		return cache.get( token , RemoteSession.class ) == null ;
	}

	@Override
	public HttpSession getSession() {
		String token = getToken() ;
		HttpSession session = null ;
		if( token != null )
			session = cache.get( token , RemoteSession.class ) ;
		return session == null ? createSession( token ) : session ;
	}

	@Override
	public HttpSession flush( HttpSession session ) {
		cache.del( session.getId() );
		return cache.put( session.getId() , session , SESS_TIME_OUT ) ;
	}

}
