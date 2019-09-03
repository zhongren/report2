package com.newproj.report.sys.security;

import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

public class RemoteSession implements HttpSession{
	
	private Map<String,Object> attribute = new HashMap<String,Object>() ;
	private String id ;
	private long creationTime ;
	private long lastAccessedTime ;
	
	public RemoteSession(){
		
	}

	public RemoteSession( String id ){
		this.id = id ;
		this.creationTime = new Date().getTime() ;
	}
	
	
	public Map<String, Object> getAttribute() {
		return attribute;
	}

	public void setAttribute(Map<String, Object> attribute) {
		this.attribute = attribute;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	public void setLastAccessedTime(long lastAccessedTime) {
		this.lastAccessedTime = lastAccessedTime;
	}

	@Override
	public long getCreationTime() {
		// TODO Auto-generated method stub
		return creationTime;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public long getLastAccessedTime() {
		// TODO Auto-generated method stub
		return lastAccessedTime;
	}

	@Override
	public ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMaxInactiveInterval(int interval) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getMaxInactiveInterval() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public HttpSessionContext getSessionContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getAttribute(String name) {
		return attribute.get( name);
	}

	@Override
	public Object getValue(String name) {
		// TODO Auto-generated method stub
		return attribute.get( name);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		// TODO Auto-generated method stub
		return null ;
	}

	@Override
	public String[] getValueNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAttribute(String name, Object value) {
		// TODO Auto-generated method stub
		attribute.put(name, value);
	}

	@Override
	public void putValue(String name, Object value) {
		// TODO Auto-generated method stub
		attribute.put(name, value);
	}

	@Override
	public void removeAttribute(String name) {
		// TODO Auto-generated method stub
		attribute.remove(name);
	}

	@Override
	public void removeValue(String name) {
		// TODO Auto-generated method stub
		attribute.remove(name);
	}

	@Override
	public void invalidate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isNew() {
		// TODO Auto-generated method stub
		return false;
	}

}
