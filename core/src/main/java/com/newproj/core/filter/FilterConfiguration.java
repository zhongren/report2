package com.newproj.core.filter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import com.newproj.core.filter.encrypt.EncryptFilter;

/**
 * 过滤器配置类 .
 * 
 * @author Lain.Cheng
 *
 */
@EnableConfigurationProperties( FilterConfigurationProperties.class )
public class FilterConfiguration {

	@Bean("encryptFilter")
	public EncryptFilter encryptFilter(){
		return new EncryptFilter() ;
	}
	
	/**
	 * 配置请求，响应数据加密 .
	 * 
	 * @return
	 */
	@Bean
	@Autowired
	public FilterRegistrationBean getEncryptFilter( EncryptFilter encryptFilter ){
		FilterRegistrationBean filterRegistration = new FilterRegistrationBean() ;
		filterRegistration.setFilter( encryptFilter );
		filterRegistration.setName("encryptFilter");
		List<String> patterns = new ArrayList<String>() ;
		patterns.add("/*") ;
		filterRegistration.setUrlPatterns( patterns );
		filterRegistration.setOrder(1);
		return filterRegistration ;
	}
	
}
