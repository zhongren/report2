package com.newproj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(basePackages="com.newproj")
@EnableAutoConfiguration
@Configuration
public class ApplicationMain {

	public static void main( String [] args ){
		SpringApplication.run( ApplicationMain.class, args ) ;
	}
}
