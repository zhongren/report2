package com.newproj.core.cache.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties( prefix="spring.app.redis" , ignoreUnknownFields = true , ignoreInvalidFields = true )
public class RedisConfigurationProperties {

	private int maxActive = 800 ;
	private int maxIdle  = 100 ;
	private long maxWait = 1000l;
	private String host = "127.0.0.1";
	private int port = 6379;
	
	public int getMaxActive() {
		return maxActive;
	}
	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}
	public int getMaxIdle() {
		return maxIdle;
	}
	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}
	public long getMaxWait() {
		return maxWait;
	}
	public void setMaxWait(long maxWait) {
		this.maxWait = maxWait;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
	
}
