package com.newproj.report.eduinst.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mchange.lang.StringUtils;
import com.newproj.core.beans.MapParam;
import com.newproj.core.encrypt.CryptoUtil;
import com.newproj.core.exception.BusinessException;
import com.newproj.core.rest.support.AbstractBaseService;
import com.newproj.core.util.NumberUtil;
import com.newproj.core.util.TimeUtil;
import com.newproj.report.eduinst.EduinstConstant.EduinstType;
import com.newproj.report.eduinst.dal.dao.EduinstAuditingDao;
import com.newproj.report.eduinst.dal.dao.EduinstCollectionDao;
import com.newproj.report.eduinst.dal.dao.EduinstDao;
import com.newproj.report.eduinst.dto.Eduinst;
import com.newproj.report.eduinst.dto.EduinstParam;
import com.newproj.report.school.dal.dao.SchoolAuditingDao;
import com.newproj.report.sys.constant.RoleConstant.RoleCode;
import com.newproj.report.sys.constant.UserConstant;
import com.newproj.report.sys.constant.UserConstant.InstType;
import com.newproj.report.sys.dal.dao.SysRoleDao;
import com.newproj.report.sys.dal.dao.UserDao;
import com.newproj.report.sys.dto.User;
import com.newproj.report.sys.dto.UserParam;
@Service
public class EduinstService extends AbstractBaseService{

	@Autowired
	private EduinstDao eduinstDao ;
	
	@Autowired
	private UserDao userDao ;
	
	@Autowired
	private EduinstAuditingDao eduinstAuditingDao ;
	
	@Autowired
	private SchoolAuditingDao schoolAuditingDao ;
	
	@Autowired
	private SysRoleDao roleDao ;
	
	@Autowired
	private EduinstCollectionDao eduinstCollectionDao ;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		setMapper( eduinstDao ) ;
	}
	
	@Transactional( propagation=Propagation.REQUIRED , timeout=20000 ) 
	public int createEduinst( EduinstParam param ){
		EduinstType type = EduinstType.parse( param.getType() ) ;
		if( type == null || !Arrays.asList(EduinstType.CITY , 
				EduinstType.COUNTY , EduinstType.PROVINCE ).contains( type ) )
			throw new BusinessException("教育局类型不正确!") ;
		param.setLoginName( param.getCode() );//登陆账号设置为标识码 .
		if( param.getLoginName() == null || param.getLoginName().trim().length() == 0 )
			throw new BusinessException("登录账号不能为空!") ;
		
		verifyCode( param.getCode() , null ) ;
		param.setCreateTime( TimeUtil.now() );
		int eduinstId = NumberUtil.parseInt( eduinstDao.createBean( param , EduinstParam.class  ), 0 ) ;
		
		createEduinstAccount( eduinstId , type , 
				param.getName() , param.getLoginName()  , param.getPasswd() , param.getCreateId() ) ;
		return  eduinstId ;
	}
	
	private void createEduinstAccount( int eduinstId ,EduinstType eduinstType , String realName , 
			String loginName , String passwd , Integer createId ){
		if( userDao.findMapBy("username", loginName, "id") != null )
			throw new BusinessException("登陆账号已存在!") ;
		//添加登陆用户 .
		UserParam user = new UserParam() ;
		user.setInstId( eduinstId );
		user.setUsername( loginName );
		user.setInstType(InstType.EDUINST.name() );
		user.setRealName( realName );
		user.setCreateId(createId);
		user.setCreateTime( TimeUtil.now() );
		user.setPasswd( ( passwd == null || passwd.trim().length() == 0 )
				? CryptoUtil.sha256Encode( UserConstant.INIT_PASSWD ) : CryptoUtil.sha256Encode( passwd.trim() ) );
		
		RoleCode roleCode = eduinstType == EduinstType.CITY ? 
				RoleCode.CITY : eduinstType == EduinstType.COUNTY ? RoleCode.COUNTY : RoleCode.PROVINCE ;
		
		user.setRoleCode( roleCode.name() );
		userDao.createBean( user , UserParam.class ) ;
	}
	
	private void verifyCode( String code , Integer id ){
		Eduinst eduinst = eduinstDao.findBeanBy("code", code, Eduinst.class ) ;
		if( (eduinst != null && id == null) || 
				( eduinst != null && id != null && !eduinst.getId().equals( id ) ) )
			throw new BusinessException("标识码["+code+"]已存在!") ;
	}
	
	@Transactional( propagation=Propagation.REQUIRED , timeout=20000 ) 
	public void updateEduinst( int id , EduinstParam param ){
		if( eduinstDao.findMapBy("id" , id ) == null )
			throw new BusinessException("数据不存在,更新失败!") ;
		verifyCode( param.getCode() , id ) ;
		param.setUpdateTime( TimeUtil.now() );
		EduinstType type = EduinstType.parse( param.getType() ) ;
		if( type == null || !Arrays.asList(EduinstType.CITY , EduinstType.COUNTY , EduinstType.PROVINCE ).contains( type ) )
			throw new BusinessException("教育局类型不正确!") ;
		
		if( type == EduinstType.PROVINCE ){
			param.setCityId(0);
			param.setCountyId(0);
		}else if(type == EduinstType.CITY ){
			param.setCountyId(0);
		}
		eduinstDao.updateBean("id", id, param ) ;
		
		//更新教育局账号信息
		Eduinst eduinst = eduinstDao.findBeanBy("id",  id , Eduinst.class ) ;
		updateUser( id  , eduinst.getCode() , eduinst.getName() ) ;
	}
	
	private void updateUser( int eduinstId , String loginName , String realName ){
		User user = userDao.findBean( new MapParam<String,Object>()
				.push("instId" , eduinstId )
				.push("roleCode" , Arrays.asList( RoleCode.CITY.name() ,
						RoleCode.COUNTY.name() , RoleCode.PROVINCE.name() ) ), User.class ) ;
		if( user == null ) return ;
		userDao.updateMap("id", user.getId(), new MapParam<String,Object>()
				.push("username" , loginName  )
				.push("realName" , realName ) ) ;
	}
	
	public void deleteEduinstSubAccount( int subaccId , int instId  ){
		User user = null ;
		if( ( user = userDao.findBean( new MapParam<String,Object>()
				.push("instType" , InstType.EDUINST  )
				.push("id" , subaccId )
				.push( "instId" , instId ), User.class ) ) == null )
			throw new BusinessException("账号不存在!") ;
		if( eduinstAuditingDao.findMapBy("userId", subaccId, "userId") != null
				|| schoolAuditingDao.findMapBy("userId", subaccId , "id") != null )
			throw new BusinessException("账号存在审核记录,无法删除!") ;
		
		userDao.delete( new MapParam<String,Object>().push("id" , user.getId() ) ) ;
	}
	
	@Transactional( propagation = Propagation.REQUIRED )
	public void deleteEduinst( int eduinstId ){
		if( eduinstDao.findMapBy("id", eduinstId, "id") == null )
			throw new BusinessException("数据已删除!") ;
		if( eduinstCollectionDao.findMapBy("instId", eduinstId) != null )
			throw new BusinessException("当前教育局存在填报数据,无法删除!") ;
		//删除教育局
		eduinstDao.delete( new MapParam<String ,Object>().push("id" , eduinstId ) ) ;
		//删除教育局账号
		userDao.delete(  new MapParam<String ,Object>()
				.push("instId" , eduinstId ).push("instType" , InstType.EDUINST.name() ) ) ;
	}

}
