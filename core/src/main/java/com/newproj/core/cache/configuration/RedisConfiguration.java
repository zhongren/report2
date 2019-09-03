package com.newproj.core.cache.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.newproj.core.cache.Cache;
import com.newproj.core.cache.RedisCache;

@EnableConfigurationProperties( RedisConfigurationProperties.class )
public class RedisConfiguration {

	@Autowired
	private RedisConfigurationProperties properties ;
	
	@Bean("cache")
	public Cache getCache(){
		Cache cache = new RedisCache( properties.getMaxActive() , 
				properties.getMaxIdle() , properties.getMaxWait() , properties.getHost() , properties.getPort() ) ;
		return cache ;
	}
	
}
