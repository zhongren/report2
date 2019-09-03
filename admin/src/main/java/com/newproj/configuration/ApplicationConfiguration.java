package com.newproj.configuration;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

import com.newproj.configuration.cors.WebCorsConfiguration;
import com.newproj.configuration.datasource.DataSourceConfiguration;
import com.newproj.core.cache.configuration.RedisConfiguration;
import com.newproj.core.rest.requestparam.ParamMapResolver;
import com.newproj.report.sys.security.Security;

/**
 * Application 配置入口 .
 * 
 * @author Lain.Cheng
 *
 */
@Configuration
@Import( { Security.class , PropertyConfiguration.class,RedisConfiguration.class , 
	DataSourceConfiguration.class , WebCorsConfiguration.class /*, FilterConfiguration .class */} )
public class ApplicationConfiguration extends DelegatingWebMvcConfiguration{

	protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add( new ParamMapResolver() ) ;
	}
	
	protected void addResourceHandlers( ResourceHandlerRegistry registry ){
		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/") ;
	}
	
	protected void addViewControllers( ViewControllerRegistry registry ){
		registry.addRedirectViewController("/", "/login.html#/") ;
	}
}
