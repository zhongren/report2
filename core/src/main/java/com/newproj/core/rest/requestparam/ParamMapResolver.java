package com.newproj.core.rest.requestparam;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.newproj.core.rest.support.ParamModal;

public class ParamMapResolver implements HandlerMethodArgumentResolver{

	public boolean supportsParameter(MethodParameter parameter) {
		if( parameter.getParameterType().isAssignableFrom( ParamModal.class )){
			return true ;
		}
		return false;
	}

	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		Map<String,String[]> paramMap = webRequest.getParameterMap() ;
		ParamModal paramModal = new ParamModal() ;
		if( paramMap == null || paramMap.isEmpty() ){
			return paramModal ;
		}
		for( Entry<String,String[]> param : paramMap.entrySet() ){
			if( StringUtils.isEmpty( param.getKey() ) 
					|| param.getValue() == null || param.getValue().length == 0 ){
				continue ;
			}
			if( param.getValue().length == 1 ){
				paramModal.put( param.getKey().trim() , param.getValue()[0].trim() ) ;
				continue ;
			}
			paramModal.put( param.getKey().trim() , Arrays.asList( param.getValue() ) ) ;
		}
		return paramModal;
	}

}
