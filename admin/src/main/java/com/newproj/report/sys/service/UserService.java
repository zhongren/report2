package com.newproj.report.sys.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newproj.core.beans.MapParam;
import com.newproj.core.encrypt.CryptoUtil;
import com.newproj.core.exception.BusinessException;
import com.newproj.core.rest.support.AbstractBaseService;
import com.newproj.core.util.NumberUtil;
import com.newproj.core.util.StringUtil;
import com.newproj.core.util.TimeUtil;
import com.newproj.report.sys.constant.UserConstant.InstType;
import com.newproj.report.sys.dal.dao.UserDao;
import com.newproj.report.sys.dto.User;
import com.newproj.report.sys.dto.UserParam;

@Service
public class UserService extends AbstractBaseService{
	
	@Autowired
	private UserDao userDao ;
	
	public void init() {
		setMapper( userDao ) ;
	}
	
	public int createUser( UserParam param ){
		if(StringUtil.isEmpty(param.getUsername())) {
			throw new BusinessException("登陆账号不能为空");
		}
		validateUname(param.getUsername());
		param.setCreateTime( TimeUtil.format( new Date() , "yyyy-MM-dd HH:mm:ss") );
		return NumberUtil.parseInt( userDao.createBean( param , UserParam.class ) , 0 ) ;
	}
	
	private void validateUname(String uname) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("username", uname);
		User user = userDao.findBean(params, User.class);
		if(null!=user) {
			throw new BusinessException(String.format("用户名%s已存在", uname));
		}
	}
	
	public void updateUser( int userId , UserParam param ){
		param.setUpdateTime( TimeUtil.now() ) ;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("id", userId);
		User user = userDao.findBean(params, User.class);
		if(null==user) {
			throw new BusinessException("更新记录不存在");
		}
	    if(!user.getUsername().equals(param.getUsername())) {
	    	validateUname(param.getUsername());
	    }
		userDao.updateBean("id", userId , param ) ;
	}
	
	/**
	 * 用户登录 .
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public User findByUsernameAndPassWord( String username , String password ){
		if( username == null || password == null )
			return null ;
		return userDao.findBean( new MapParam<String,Object>()
				.push("username" , username ).push("passwd" , password ), User.class ) ;
	}
	
	public void updatePasswd( int userId , String passwd ){
		if( userDao.findMapBy("id", userId, "id") == null )
			throw new BusinessException("用户不存在!") ;
		userDao.updateMap("id", userId , new MapParam<String,Object>().push("passwd" , 
				CryptoUtil.sha256Encode(passwd) ) ) ;
	}

}
