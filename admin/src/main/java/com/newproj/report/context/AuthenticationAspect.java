package com.newproj.report.context;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.newproj.core.exception.AuthInvalidException;
import com.newproj.core.json.JsonSerializer;
import com.newproj.core.oauth.OAuth;
import com.newproj.core.rest.support.ResultModal;
import com.newproj.core.rest.support.StatusCode;
import com.newproj.core.util.AnnotationUtils;

@Component
@Aspect
public class AuthenticationAspect {
	private JsonSerializer serializer = new JsonSerializer().convertNullValuesToEmpty() ;

	@Pointcut("execution(public * com.newproj..action.*Action.*(..))")
	public void verifyTokenPoint(){ }
	
	@Around("verifyTokenPoint()")
	public Object tokenVerifyAdvice( ProceedingJoinPoint pjp ) throws Throwable{
		//验证权限 .
		if( !verifyPermission( (MethodSignature) pjp.getSignature() ) ) {
			throw new AuthInvalidException() ;
		}
		
		long timestamp = System.currentTimeMillis() ;
		Object result = pjp.proceed() ;
		if( result == null )
			return null ;
		
		ResultModal modal = serializer.fromJson(result.toString(), ResultModal.class) ;
		modal.setTimestamp( System.currentTimeMillis() - timestamp );
		
		return serializer.toJson( modal ) ;
	}
	
	
	private boolean verifyPermission( MethodSignature signature ){
		OAuth type = AnnotationUtils.getAnnotation(  signature.getMethod() , OAuth.class ) ;
		String [] perms = type == null || type.value() == null || type.value().length == 0 ?
				new String[]{"login"} : type.value();
		return  Subject.hasPerm( perms );
	}
	
	private ResultModal failVerifyModal(){
		ResultModal result = new ResultModal( StatusCode.UNAUTH.getCode()  , StatusCode.UNAUTH.getMessage() ) ;
		return result ;
	}
	
	
}
