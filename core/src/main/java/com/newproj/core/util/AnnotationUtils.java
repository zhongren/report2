package com.newproj.core.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class AnnotationUtils {

	@SuppressWarnings("unchecked")
	public static <T>  T getAnnotation( Object obj , Class<T> annoClazz ){
		Annotation[] annos = null ;
		if( obj instanceof Class ){
			annos = ( (Class<?>)obj ).getAnnotations() ;
		}
		if( obj instanceof Field ){
			annos = ( ( Field ) obj ).getAnnotations() ;
		}
		if( obj instanceof Method ){
			annos = ( ( Method ) obj ).getAnnotations() ;
		}
		if( annos == null || annos.length == 0 ){
			return null ;
		}
		for( Annotation anno : annos ){
			if( anno.annotationType().equals( annoClazz ) ){
				return (T)anno ;
			}
		}
		return null ;
	}
	
	public static <T> boolean isAnnotated( Object obj , Class<T> annoClazz ){
		return getAnnotation( obj , annoClazz ) != null ;
	}
	
}
