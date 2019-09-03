package com.newproj.report.school.action;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.newproj.core.beans.MapParam;
import com.newproj.core.exception.BusinessException;
import com.newproj.core.oauth.OAuth;
import com.newproj.core.page.RemotePage;
import com.newproj.core.rest.annotation.Api;
import com.newproj.core.rest.annotation.Delete;
import com.newproj.core.rest.annotation.Get;
import com.newproj.core.rest.annotation.Post;
import com.newproj.core.rest.annotation.Put;
import com.newproj.core.rest.support.ParamModal;
import com.newproj.core.rest.support.RestActionSupporter;
import com.newproj.core.util.JoinUtil;
import com.newproj.core.validation.groups.CGroup;
import com.newproj.core.validation.groups.UGroup;
import com.newproj.report.context.Subject;
import com.newproj.report.context.trans.TransContext;
import com.newproj.report.eduinst.dto.Eduinst;
import com.newproj.report.eduinst.service.EduinstService;
import com.newproj.report.school.dto.School;
import com.newproj.report.school.dto.SchoolConnector;
import com.newproj.report.school.dto.SchoolConnectorParam;
import com.newproj.report.school.dto.SchoolParam;
import com.newproj.report.school.dto.SchoolType;
import com.newproj.report.school.dto.SchoolTypeParam;
import com.newproj.report.school.service.SchoolService;
import com.newproj.report.sys.constant.RoleConstant.RoleType;
import com.newproj.report.sys.constant.UserConstant;
import com.newproj.report.sys.constant.UserConstant.InstType;
import com.newproj.report.sys.dto.PasswdForgetParam;
import com.newproj.report.sys.dto.User;
import com.newproj.report.sys.service.UserService;

@Api("/school")
public class SchoolAction extends RestActionSupporter{

	@Autowired
	private SchoolService schoolService ;
	
	@Autowired
	private TransContext  trans ;
	
	@Autowired
	private EduinstService eduinstService ;
	
	@Autowired
	private UserService userService ;

	
	
	/**
	 * 查询学校列表
	 * 
	 * @param modal
	 * @return
	 */
	@Get("")
	public String findSchool( ParamModal modal ){
		Map<String,Object> param = modal.getParam("?name" , "type" ,"code") ;
		InstType instType = InstType.parse( Subject.getUser().getInstType() ) ;
		if( instType == null || instType == InstType.SCHOOL )
			return success( null  ) ;
		else if( instType == InstType.EDUINST ){
			Eduinst eduinst = eduinstService.findBy("id", Subject.getUser().getInstId() , Eduinst.class ) ;
			if( eduinst == null )
				return success( null ) ;
			
			if(eduinst.getCountyId()>0) {
				param.put("county", eduinst.getCountyId() ) ;
			}
			if(eduinst.getCityId() > 0) {
				param.put("city", eduinst.getCityId() ) ;
			}
			
		}
		
		RemotePage<School> pageData = schoolService.findList( param , modal.getPageParam() , School.class ) ;
		if( pageData == null || pageData.isEmpty() )
			return success( null ) ;
		
		/*JoinUtil.join(pageData, new String[]{"loginName","accountId","accountStatus"}, "id" ,
				userService.findList(new MapParam<String,Object>() 
						.push("instId" , JoinUtil.fieldsValue( pageData, "id") )
						.push("instType" , InstType.SCHOOL.name() ), null , User.class ) , 
				new String[]{"username","userId","status"}, "instId" );*/
		
		JoinUtil.join( pageData , new String[]{"eduinstName"}, "eduinstId",
				eduinstService.findListBy("id", JoinUtil.fieldsValue(pageData, "eduinstId"), Eduinst.class , "name" , "id"),
				new String[]{"name"}, "id");
		
		JoinUtil.join(pageData, new String[]{"typeName"}, "type", 
				schoolService.findSchoolType( new MapParam<String,Object>().push("id" , JoinUtil.fieldsValue(pageData, "type" ) ), null ) 
				, new String[]{"name"}, "id");
		
		trans.transCity( pageData , Arrays.asList("city","county"), Arrays.asList("city","county") ) 
			.transDict("REGION_TYPE", pageData, "regionType", "regionType")
			.transDict("ACCOUNT_STATUS", pageData, "accountStatus", "accountStatusName") 
			.transUser( pageData, Arrays.asList("createId","updateId"), Arrays.asList("createName" , "updateName"));
		return success( pageData , pageData == null ? 0 : pageData.getTotal() );
	}
	
	@Get("/option")
	public String findSchoolOption( ParamModal modal ){
		Map<String,Object> param = modal.getParam("?name" , "eduinstId" , "city" , "county") ;
		InstType instType = InstType.parse( Subject.getUser().getInstType() ) ;
		if( instType == InstType.SCHOOL ){
			param.put("id", Subject.getUser().getInstId() ) ;
		}else if( instType == InstType.EDUINST ){
			Eduinst eduinst = eduinstService.findBy("id", Subject.getUser().getInstId() , Eduinst.class ) ;
			if( eduinst == null ) return success( null ) ;
			if( eduinst.getCityId() != null && eduinst.getCityId() > 0 )
				param.put("city", eduinst.getCityId() ) ;
			if( eduinst.getCountyId() != null && eduinst.getCountyId() > 0 )
				param.put("county", eduinst.getCountyId() ) ;
		}
		return success( schoolService.findList( param , modal.getPageParam() , School.class , "id" , "code" , "name") ) ;
	}
	
	/**
	 * 创建学校
	 * 
	 * @param param
	 * @return
	 */
	@Post("")
	public String createSchool( @RequestBody @Validated(CGroup.class) SchoolParam param ){
		param.setCreateId( Subject.getUserId() );
		param.setLoginName(param.getCode());
		return success( schoolService.createSchool( param ) ) ;
	}
	
	/**
	 * 更新学校信息
	 * 
	 * @param schoolId
	 * @param param
	 * @return
	 */
	@Put("/{id}")
	public String updateSchool( @PathVariable("id") int schoolId , 
			@RequestBody @Validated(UGroup.class) SchoolParam param ){
		param.setUpdateId( Subject.getUserId() );
		schoolService.updateSchool(schoolId, param);
		return success( null ) ;
	}
	
	/**
	 * 获取学校详情
	 * 
	 * @param schoolId
	 * @return
	 */
	@Get("/{id}")
	public String getSchool( @PathVariable("id") int schoolId ){
		return success( schoolService.findBy("id", schoolId, School.class ) ) ;
	}
	
	/**
	 * 删除学校数据
	 * 
	 * @param schoolId
	 * @return
	 */
	@Delete("/{id}")
	public String deleteSchool(  @PathVariable("id") int schoolId ){
		return success( schoolService.deleteSchool(schoolId) ) ;
	}
	
	/**
	 * 获取学下类型列表
	 * 
	 * @param modal
	 * @return
	 */
	@Get("/type")
	public String findSchoolType( ParamModal modal ){
		RemotePage<SchoolType> pageData = schoolService.findSchoolType(
				modal.getParam("id" , "?name"), modal.getPageParam() ) ;
		if( pageData == null || pageData.isEmpty() )
			return success( null ) ;
		trans.transUser( pageData, Arrays.asList("createId" , "updateId"),
				Arrays.asList("createName" , "updateName")) ;
		return success( pageData , pageData == null ? 0 : pageData.getTotal()  ) ;
	}
	
	/**
	 * 创建学校类型
	 * 
	 * @param param
	 * @return
	 */
	@Post("/type")
	public String createSchoolType( @RequestBody @Validated(CGroup.class) SchoolTypeParam param ){
		param.setCreateId( Subject.getUserId() );
		return success( schoolService.createSchoolType(param) ) ;
	}
	
	/**
	 * 获取学校类型详情
	 * 
	 * @param typeId
	 * @return
	 */
	@Get("/type/{id}")
	public String getSchoolType( @PathVariable("id") int typeId ){
		return success( schoolService.getSchoolType(typeId) ) ;
	}
	
	/**
	 * 更新学校类型
	 * 
	 * @param typeId
	 * @param param
	 * @return
	 */
	@Put("/type/{id}")
	public String updateSchoolType( @PathVariable("id") int typeId , 
			 @RequestBody @Validated(UGroup.class) SchoolTypeParam param  ){
		param.setUpdateId( Subject.getUserId() );
		schoolService.updateSchoolType(typeId, param);
		return success( null ) ;
	}
	
	@Delete("/type/{id}")
	public String deleteSchoolType(  @PathVariable("id") int typeId  ){
		schoolService.deleteSchoolType( typeId ) ;
		return success( null ) ;
	}
	
	/**
	 * 获取学校联系人
	 * 
	 * @param schoolId
	 * @return
	 */
	@Get("/connector")
	public String getSchoolUser( @RequestParam( value="schoolId" , required = false ) Integer schoolId ){
		if( RoleType.SCHOOL == RoleType.parse( Subject.getUser().getInstType() ) ){
			schoolId = Subject.getUser().getInstId() ;
		}else if( schoolId == null )
			throw new BusinessException("请选择学校!") ;
		return success( schoolService.findBy("id", schoolId , SchoolConnector.class ) ) ;
	}
	
	/**
	 * 更新学校联系人
	 * 
	 * @param param
	 * @return
	 */
	@Put("/connector")
	public String updateSchoolUser( @RequestBody SchoolConnectorParam param ){
		int schoolId = 0 ;
		if( RoleType.SCHOOL != RoleType.parse( Subject.getUser().getInstType() ) )
			throw new BusinessException("当前用户无法更新学校联系人信息!") ;
		schoolId = Subject.getUser().getInstId() ;
		if( schoolService.findBy("id", schoolId , School.class , "id") == null )
			throw new BusinessException("学校不存在!") ;
		schoolService.update("id", schoolId , param ) ;
		return success( null ) ;
	}
	
	/**
	 * 忘记密码找回 .
	 * 
	 * @param param
	 * @return
	 */
	@Put("/forgetPasswd")
	@OAuth("anon")
	public String forgetPasswd( @RequestBody @Validated PasswdForgetParam param ){
		//校验验证码 .
		Object verifyCode = Subject.getSessionAttribute("verifyCode") ;
		if( verifyCode == null || param.getVerifyCode() == null || 
				!param.getVerifyCode().equalsIgnoreCase( verifyCode.toString() ) )
			throw new BusinessException("验证码不正确!") ;
		Subject.setSessionAttribute("verifyCode",  null ); //验证通过,清空验证码
		
		User user = userService.findBy("username", param.getAccount() , User.class ) ;
		if( user == null || InstType.parse( user.getInstType() ) != InstType.SCHOOL )
			throw new BusinessException("用户不存在!") ;
		SchoolConnector connector = schoolService.findBy("id", user.getInstId(), SchoolConnector.class ) ;
		if( connector == null )
			throw new BusinessException("学校不存在!") ;
		boolean validPhone = ( connector.getMasterPhone() != null && connector.getMasterPhone().equals( param.getPhone() ) )
				|| (connector.getVicePhone() != null && connector.getViceName().equals( param.getPhone() ) )
				|| (connector.getReportPhone() != null && connector.getReportPhone().equals( param.getPhone() ) )  ;
		if( !validPhone )
			throw new BusinessException("手机号不正确!") ;
		userService.updatePasswd(user.getId() , UserConstant.INIT_PASSWD );
		return success( null ) ;
	}

}
