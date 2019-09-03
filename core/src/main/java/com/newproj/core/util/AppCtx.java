package com.newproj.core.util;

import java.lang.annotation.Annotation;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
@Component
public class AppCtx implements ApplicationContextAware{

	private ApplicationContext applicationContext ;
	
	@Override
	public void setApplicationContext( ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext ;
	}
	
	public  <T> T getBean( Class<T> clazz ){
		return applicationContext.getBean( clazz ) ;
	}
	
	public  Map<String,Object> getBeansWithAnnotation( Class<? extends Annotation > annotationType ){
		return applicationContext.getBeansWithAnnotation(annotationType) ;
	}

}
