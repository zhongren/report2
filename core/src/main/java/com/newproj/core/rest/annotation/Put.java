package com.newproj.core.rest.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Documented
@Target( ElementType.METHOD )
@Retention( RetentionPolicy.RUNTIME )
@RequestMapping
public @interface Put {
	@AliasFor("path")
	String[] value() default "" ;
	@AliasFor("value")
	String[] path() default "" ;
	RequestMethod[] method() default {RequestMethod.PUT} ;
	String[] produces() default MediaType.APPLICATION_JSON_UTF8_VALUE ;
	String[] consumes() default MediaType.APPLICATION_JSON_UTF8_VALUE ;
}
