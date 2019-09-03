package com.newproj.report.sys.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;

import com.newproj.core.oauth.OAuth;
import com.newproj.core.rest.annotation.Api;
import com.newproj.core.rest.support.RestActionSupporter;
import com.newproj.core.util.VerifyCodeUtils;
import com.newproj.report.context.Subject;
@Api("/verifyCode")
public class VerifyCodeAction extends RestActionSupporter{

	@RequestMapping("/get")
	@OAuth("anon")
	public void getCode( HttpServletResponse response , HttpServletRequest request ){
		try {
			Subject.setSessionAttribute("verifyCode", 
					VerifyCodeUtils.outputVerifyImage(80, 40, response.getOutputStream() , 4 ) );
		} catch (IOException e) { }
	}
}
