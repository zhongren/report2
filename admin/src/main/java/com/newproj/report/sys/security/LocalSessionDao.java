package com.newproj.report.sys.security;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

import com.newproj.core.util.WebUtil;
@Component
public class LocalSessionDao implements SessionDao {

	@Override
	public HttpSession createSession( String token ) {
		return WebUtil.resolveRequest().getSession();
	}

	@Override
	public boolean deleteSession() {
		HttpSession session = WebUtil.resolveRequest().getSession();
		session.invalidate();
		return true ;
	}

	@Override
	public HttpSession getSession() {
		return WebUtil.resolveRequest().getSession();
	}

	@Override
	public HttpSession flush( HttpSession session ) {
		return WebUtil.resolveRequest().getSession();
	}

}
