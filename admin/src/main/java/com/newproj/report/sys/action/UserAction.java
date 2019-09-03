package com.newproj.report.sys.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.newproj.core.beans.MapParam;
import com.newproj.core.encrypt.CryptoUtil;
import com.newproj.core.exception.BusinessException;
import com.newproj.core.oauth.OAuth;
import com.newproj.core.page.RemotePage;
import com.newproj.core.rest.annotation.Api;
import com.newproj.core.rest.annotation.Get;
import com.newproj.core.rest.annotation.Post;
import com.newproj.core.rest.annotation.Put;
import com.newproj.core.rest.support.ParamModal;
import com.newproj.core.rest.support.RestActionSupporter;
import com.newproj.report.context.Subject;
import com.newproj.report.sys.constant.UserConstant;
import com.newproj.report.sys.dto.PasswdForgetParam;
import com.newproj.report.sys.dto.User;
import com.newproj.report.sys.dto.UserParam;
import com.newproj.report.sys.dto.UserPasswdParam;
import com.newproj.report.sys.dto.UsernameAndPassword;
import com.newproj.report.sys.service.UserService;

/**
 * 用户模块相关API .
 * @author 10147
 *	
 */
@Api("/user")
public class UserAction extends RestActionSupporter{
	
	@Autowired
	private UserService userService ;
	
	/**
	 * 获取用用户列表
	 * 
	 * @param modal
	 * @return
	 */
	@Get("")
	public String users( ParamModal modal ){
		RemotePage<User> pageData = userService.findList(
				modal.getParam("id") , modal.getPageParam() , User.class ) ;
		return success( pageData , pageData != null ? pageData.getTotal() : 0 ) ;
	}
	
	public String createUser( @RequestBody UserParam param ){
		
		return success( null ) ;
	}
	
	@Put("/resetPasswd")
	public String resetPasswd( @RequestBody @Validated UserPasswdParam param ){
		if( userService.findOne( new MapParam<String,Object>()
				.push("id" , Subject.getUserId() )
				.push("passwd" , CryptoUtil.sha256Encode( param.getPasswd()  ) ), User.class , "id") == null )
			throw new BusinessException("原密码不正确!") ;
		userService.updatePasswd(Subject.getUserId() , param.getNewPasswd() );
		return success( null ) ;
	}
	
	/**
	 * 初始化用户密码 .
	 * 
	 * @param userId
	 * @return
	 */
	@Put("/{username}/resetPasswd")
	public String initPasswd( @PathVariable("username") String username ){
		User user = userService.findBy("username", username, User.class ) ;
		if( user == null )
			throw new BusinessException("账号不存在!") ;
		userService.updatePasswd( user.getId() ,  UserConstant.INIT_PASSWD );
		return success( null ) ;
	}
	
	@Post("/login")
	@OAuth("anon")
	public String login( @RequestBody UsernameAndPassword usrAndPasswdToken ){
		return success( Subject.login( 
				usrAndPasswdToken.getUsername() , usrAndPasswdToken.getPassword()  ) ) ;
	}
	
	
	@Get("/logout")
	public String logout( ){
		Subject.logout() ;
		return success( null ) ;
	}
	
	@Get("/principal")
	public String getLoginUserPrincipal(){
		return success( Subject.getUser() ) ;
	}
	
	
}
