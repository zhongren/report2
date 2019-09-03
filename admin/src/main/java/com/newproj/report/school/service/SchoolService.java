package com.newproj.report.school.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.newproj.core.beans.MapParam;
import com.newproj.core.encrypt.CryptoUtil;
import com.newproj.core.exception.BusinessException;
import com.newproj.core.page.PageParam;
import com.newproj.core.page.RemotePage;
import com.newproj.core.rest.support.AbstractBaseService;
import com.newproj.core.util.NumberUtil;
import com.newproj.core.util.StringUtil;
import com.newproj.core.util.TimeUtil;
import com.newproj.report.school.dal.dao.SchoolCollectionDao;
import com.newproj.report.school.dal.dao.SchoolDao;
import com.newproj.report.school.dal.dao.SchoolTypeDao;
import com.newproj.report.school.dto.School;
import com.newproj.report.school.dto.SchoolCollection;
import com.newproj.report.school.dto.SchoolParam;
import com.newproj.report.school.dto.SchoolType;
import com.newproj.report.school.dto.SchoolTypeParam;
import com.newproj.report.sys.constant.RoleConstant.RoleCode;
import com.newproj.report.sys.constant.UserConstant;
import com.newproj.report.sys.constant.UserConstant.InstType;
import com.newproj.report.sys.dal.dao.SysRoleDao;
import com.newproj.report.sys.dal.dao.UserDao;
import com.newproj.report.sys.dto.User;
import com.newproj.report.sys.dto.UserParam;
@Service
public class SchoolService extends AbstractBaseService{

	@Autowired
	private SchoolDao schoolDao ;
	
	@Autowired
	private SchoolCollectionDao schoolCollectionDao ;
	
	@Autowired
	private SchoolTypeDao schoolTypeDao ;
	
	@Autowired
	private SysRoleDao roleDao ;
	
	@Autowired
	private UserDao userDao ;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		setMapper( schoolDao ) ;
	}
	
	/**
	 * 添加学校 .
	 * 
	 * @param param
	 * @return
	 */
	@Transactional( propagation = Propagation.REQUIRED )
	public int createSchool( SchoolParam param ){
		if( StringUtil.isEmpty( param.getLoginName()  ) )
			throw new BusinessException("登陆账号名称不能为空!") ;
		//创建学校 .
		validSchoolCode( param.getCode() , null ) ;
		param.setCreateTime( TimeUtil.now() );
		int schoolId = NumberUtil.parseInt( schoolDao.createBean( param , SchoolParam.class  ), 0 ) ;
		//创建学校登陆账号 .
		createSchoolUser( schoolId , param.getName() , param.getLoginName() , param.getPassword(),param.getCreateId() ) ;
		return schoolId ;
	}
	
	private void createSchoolUser( int schoolId , String realName , String loginName ,String password , Integer createId ){
		if( userDao.findMapBy("username", loginName, "id") != null )
			throw new BusinessException("登陆账号名称已存在!") ;
		//添加登陆用户 .
		UserParam user = new UserParam() ;
		user.setInstId( schoolId );
		user.setUsername( loginName );
		user.setInstType(InstType.SCHOOL.name() );
		user.setRealName( realName );
		user.setCreateId(createId);
		user.setCreateTime( TimeUtil.now() );
		if(StringUtil.isEmpty(password)) {
			user.setPasswd( CryptoUtil.sha256Encode( UserConstant.INIT_PASSWD ) );
		}else {
			user.setPasswd(CryptoUtil.sha256Encode( password ));
		}
		user.setRoleCode( RoleCode.SCHOOL.name() );
		userDao.createBean( user , UserParam.class ) ;
	}
	
	private void updateSchoolUser( int schoolId , String loginName , String userName ){
		User user = userDao.findBean( new MapParam<String,Object>()
				.push("instId" , schoolId )
				.push("roleCode" , RoleCode.SCHOOL.name() ), User.class ) ;
		if( user == null ) return ;
		userDao.updateMap("id", user.getId(), new MapParam<String,Object>()
				.push("username" , loginName ) 
				.push("realName" , userName ) ) ;
	}
	
	/**
	 * 批量添加学校
	 * 
	 * @param paramList
	 * @return
	 */
	@Transactional( propagation=Propagation.REQUIRED , timeout=20000 ) 
	public List<Integer> createSchool( List<SchoolParam> paramList ){
		List<Integer> ids = new ArrayList<Integer>() ;
		for( SchoolParam param : paramList ){
			ids.add( createSchool( param ) ) ;
		}
		return ids ;
	}
	
	/**
	 * 更新学校
	 * 
	 * @param schoolId
	 * @param param
	 */
	@Transactional( propagation=Propagation.REQUIRED , timeout=20000 ) 
	public void updateSchool( int schoolId , SchoolParam param ){
		School school = null ;
		if( (school = schoolDao.findBeanBy("id", schoolId, School.class , "id" ) ) == null ){
			throw new BusinessException("学校不存在!") ;
		}
		validSchoolCode( param.getCode() , school.getId() ) ;
		param.setUpdateTime( TimeUtil.now() );
		schoolDao.updateBean("id", school.getId() , param ) ;
		
		//更新登陆账号
		school = schoolDao.findBeanBy("id", schoolId, School.class  ) ;
		updateSchoolUser( schoolId , school.getCode() , school.getName() ) ;
	}
	
	private boolean validSchoolCode( String code , Integer schoolId ){
		if( code == null || code.trim().length() == 0 )
			throw new BusinessException("学校标识码不能为空!") ;
		School school = schoolDao.findBeanBy("code", code, School.class ) ;
		if( school != null ){
			if( schoolId == null || !school.getId().equals( schoolId ) )
				throw new BusinessException("学校标识码已存在!") ;
		}
		return true ;	
	}
	
	/**
	 * 删除学校
	 * 
	 * @param schoolId
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED )
	public int deleteSchool( int schoolId ){
		if( schoolDao.findBeanBy("id", schoolId, School.class, "id") == null )
			return 0 ;
		/*SchoolCollection collection = schoolCollectionDao.findBeanBy("schoolId", schoolId, SchoolCollection.class ) ;
		if( collection != null )
			throw new BusinessException("当前学校存在填报记录,无法删除!") ;*/
		schoolDao.delete(new MapParam<String,Object>().push("id" , schoolId ) ) ;
		//删除学校用户
		userDao.delete( new MapParam<String,Object>()
				.push("instId" , schoolId ) .push("instType" , InstType.SCHOOL.name() ) ) ;
		return schoolId ;
	}
	
	/**
	 * 创建学校类型
	 * 
	 * @param param
	 * @return
	 */
	public int createSchoolType( SchoolTypeParam param ){
		param.setCreateTime( TimeUtil.now() );
		//validateSchoolTypeRate( param.getPrimaryRate() , param.getMiddleRate() , param.getHighRate() ) ;
		return NumberUtil.parseInt( 
				schoolTypeDao.createBean( param , SchoolTypeParam.class ) , 0 );
	}
	
	/*private void validateSchoolTypeRate( BigDecimal prate , BigDecimal mrate , BigDecimal hrate ){
		if( prate == null )
			prate = new BigDecimal(0) ;
		if( mrate == null )
			mrate = new BigDecimal(0) ;
		if( hrate == null )
			hrate = new BigDecimal(0) ;
		if( prate.add( mrate ).add( hrate ).doubleValue() != 100 )
			throw new BusinessException("学校类型人数占比必须为100%") ;
	}*/
	
	public RemotePage<SchoolType> findSchoolType( Map<String,Object> params , PageParam pageParam ){
		return withMapper( schoolTypeDao ).findList(params, pageParam, SchoolType.class ) ;
	}
	
	/**
	 * 获取学校类型详情
	 * 
	 * @param typeId
	 * @return
	 */
	public SchoolType getSchoolType( int typeId ){
		return schoolTypeDao.findBeanBy("id", typeId, SchoolType.class ) ;
	}
	
	/**
	 * 更新学校类型
	 * 
	 * @param typeId
	 * @param param
	 */
	public void updateSchoolType( int typeId , SchoolTypeParam param  ){
		if( getSchoolType( typeId ) == null )
			throw new BusinessException("数据不存在,更新失败!") ;
		//validateSchoolTypeRate( param.getPrimaryRate() , param.getMiddleRate() , param.getHighRate() ) ;
		param.setUpdateTime( TimeUtil.now() );
		schoolTypeDao.updateBean("id", typeId, param  ) ;
	}
	
	public void deleteSchoolType( int typeId ){
		if( schoolDao.findBeanBy("type", typeId , School.class, "id") != null )
			throw new BusinessException("当前已有学校使用该类型,无法删除!") ;
		schoolTypeDao.delete(new MapParam<String,Object>().push("id" , typeId ) ) ;
	}
}
