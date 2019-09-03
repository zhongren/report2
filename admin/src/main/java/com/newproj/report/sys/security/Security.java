package com.newproj.report.sys.security;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.newproj.core.beans.MapParam;
import com.newproj.core.encrypt.CryptoUtil;
import com.newproj.core.exception.AuthInvalidException;
import com.newproj.core.exception.BusinessException;
import com.newproj.core.json.JsonSerializer;
import com.newproj.core.util.AppCtx;
import com.newproj.core.util.TimeUtil;
import com.newproj.report.sys.constant.UserConstant;
import com.newproj.report.sys.dto.SysPermission;
import com.newproj.report.sys.dto.SysRole;
import com.newproj.report.sys.dto.User;
import com.newproj.report.sys.service.SysRoleService;
import com.newproj.report.sys.service.UserService;
@EnableConfigurationProperties(SecurityProperties.class)
public class Security {
	
	@Autowired
	private UserService userService ;
	
	@Autowired
	private SysRoleService roleService ;
	
	private SessionDao sessionDao ;
	
	@Autowired
	private AppCtx appCtx ; 
	
	@Autowired
	private SecurityProperties property ;
	
	@PostConstruct
	public void init(){
		if( property.getSessionType().equalsIgnoreCase("remote") )
			sessionDao = appCtx.getBean(RemoteSessionDao.class) ;
		else
			sessionDao = appCtx.getBean(LocalSessionDao.class) ;
	}
	
	public User doLogin( String username , String passwd ) throws AuthInvalidException{
		User user = userService.findByUsernameAndPassWord(username, CryptoUtil.sha256Encode( passwd ) ) ;
		if( user == null || user.getStatus() == UserConstant.UserStatus.DISABLED.getCode() )
			throw new BusinessException("用户名或密码错误!") ;
		doLogout() ;
		//设置用户登录信息
		HttpSession session = sessionDao.createSession( 
				createToken( String.valueOf(user.getId() ) , user.getRoleCode() ) );
		
		session.setAttribute("user", user);
		//设置用户权限信息
		SysRole role = roleService.findBy("code", user.getRoleCode() , SysRole.class , "id")  ; 
		if( role != null )
			session.setAttribute("perms", loadUserRolePerm( role.getId() , session ) ) ;
		//更新最后登录日期
		userService.update("id", user.getId() , new MapParam<String,Object>()
				.push("tokenTime", TimeUtil.now() ) ) ;
		sessionDao.flush( session ) ;
		return user  ; 
	}
	
	public void doLogout(){
		sessionDao.deleteSession() ;
	}
	
	public boolean hasPerm( String ... perms ){
		if( perms == null || perms.length == 0 )
			return false ;
		List<String> permList = Arrays.asList( perms ) ;
		if( permList.contains("anon") )
			return true ;
		if( permList.contains("login") && isLogin() )
			return true ;
		Object operms = sessionDao.getSession().getAttribute("perms") ;
		Set<String> uperms = null ;
		if( operms == null || ( (uperms = (Set<String>) operms).isEmpty() ) ){
			return false ;
		}
		for( String perm : permList )
			if( uperms.contains( perm ) )
				return true ;
			
		return false ;
	}
	
	public User getLoginUser(){
		Object principle = null ;
		try{
			principle = sessionDao.getSession().getAttribute("user") ;
		}catch(Exception e){
			//Ignore ...
		}
		if( principle == null )
			throw new AuthInvalidException("用户登录超时!") ;
		JsonSerializer jsonse = new JsonSerializer() ;
		return jsonse.fromJson( jsonse.toJson( principle ) , User.class)  ;
	}
	
	private boolean isLogin( ){
		try{
			getLoginUser() ;
		}catch(Exception e){
			return false ;
		}
		return true ;
	}
	
	private Set<String> loadUserRolePerm( Integer roleId , HttpSession session ){
		Set<String> setPerms = new HashSet<String>() ;
		if( roleId == null )
			return setPerms;
		SysRole role = roleService.findOne(new MapParam<String,Object>()
				.push("status" , 0 ).push("id" , roleId ), SysRole.class ) ;
		if( role == null )
			return setPerms;
		session.setAttribute("role", role );
		
		List<SysPermission> perms = roleService.findRolePermission(roleId) ;
		if( perms != null && !perms.isEmpty() ){
			for( SysPermission perm : perms )
				setPerms.add( perm.getPattern() ) ;
		}
		return setPerms ;
	}
	
	public Object getSessionAttribute( String name ){
		return sessionDao.getSession().getAttribute(name) ;
	}
	
	public void setSessionAttribute( String name , Object value ){
		HttpSession session = sessionDao.getSession() ;
		session.setAttribute(name, value);
		sessionDao.flush(session) ;
	}
	
	public String createToken( String principal , String group ){
		//生成session id (TOKEN-ROLE-principal-MD5(PRINCIPAL+TIMESTAMP))
		String token = CryptoUtil.md5Encode( principal + System.currentTimeMillis() ) ;
		token = String.format("%stoken#%s#%s#%s", 
				property.getPrefix() , group , principal , token ) ;
		return token ;
	}
	
}
