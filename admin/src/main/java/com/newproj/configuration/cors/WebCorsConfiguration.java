package com.newproj.configuration.cors;

import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域请求配置类 .
 * 
 * @author Lain.Cheng
 *
 */
public class WebCorsConfiguration {

	private CorsConfiguration getCorsConfiguration(){
		CorsConfiguration corsConfig = new CorsConfiguration() ;
		corsConfig.addAllowedHeader("*");
		corsConfig.addAllowedMethod("*");
		corsConfig.addAllowedOrigin("*");
		corsConfig.setAllowCredentials( true );
		return corsConfig ;
	}
	
	@Bean
	public CorsFilter corsFilter(){
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource() ;
		source.registerCorsConfiguration("/**", getCorsConfiguration() );
		return new CorsFilter( source ) ;
	}
}
