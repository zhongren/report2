package com.newproj.report.sys.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties( prefix="spring.app.security" ,  ignoreUnknownFields = true , ignoreInvalidFields=true)
public class SecurityProperties {
	
	private String sessionType = "local" ;
	private String prefix = "qa." ;

	public String getSessionType() {
		return sessionType;
	}

	public void setSessionType(String sessionType) {
		this.sessionType = sessionType;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
}
