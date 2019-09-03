package com.newproj.report.user.action;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.newproj.core.beans.MapParam;
import com.newproj.core.encrypt.CryptoUtil;
import com.newproj.core.exception.BusinessException;
import com.newproj.core.page.PageParam;
import com.newproj.core.page.RemotePage;
import com.newproj.core.rest.annotation.Api;
import com.newproj.core.rest.annotation.Delete;
import com.newproj.core.rest.annotation.Get;
import com.newproj.core.rest.annotation.Post;
import com.newproj.core.rest.annotation.Put;
import com.newproj.core.rest.support.ParamModal;
import com.newproj.core.rest.support.RestActionSupporter;
import com.newproj.report.context.Subject;
import com.newproj.report.context.trans.TransContext;
import com.newproj.report.eduinst.service.EduinstService;
import com.newproj.report.quota.constant.QuotaConstant.QuotaType;
import com.newproj.report.quota.dto.Quota;
import com.newproj.report.quota.dto.SubaccountQuotaParam;
import com.newproj.report.quota.service.QuotaService;
import com.newproj.report.sys.constant.RoleConstant.RoleCode;
import com.newproj.report.sys.constant.UserConstant;
import com.newproj.report.sys.dto.SysRole;
import com.newproj.report.sys.dto.User;
import com.newproj.report.sys.dto.UserParam;
import com.newproj.report.sys.service.SysRoleService;
import com.newproj.report.sys.service.UserService;
@Api("/subaccount")
public class SubAccountAction extends RestActionSupporter{
	
	@Autowired
	private UserService userService ;
	
	@Autowired
	private SysRoleService roleService ;
	
	@Autowired
	private QuotaService quotaService ;
	
	@Autowired
	private TransContext trans ;
	
	@Autowired
	private EduinstService eduinstService ;
	
	/**
	 * 查询子账号列表
	 * 
	 * @param modal
	 * @return
	 */
	@Get("")
	public String findSubAccount( ParamModal modal ){
		RoleCode roleCode = RoleCode.parse( Subject.getUser().getRoleCode() ) ;
		if( roleCode == null || !Arrays.asList( RoleCode.CITY , RoleCode.COUNTY , RoleCode.PROVINCE ).contains( roleCode ) )
			return success( null ) ;
		RoleCode subRole = RoleCode.subRole( roleCode ) ;
		
		if( subRole == null )
			return success( null ) ;
		
		Map<String,Object> param = modal.getParam( "?uername" , "?realName" ) ;
		param.put("roleCode", subRole.name() ) ;
		param.put("instId", Subject.getUser().getInstId() ) ;
		
		RemotePage<User> pageData = userService.findList( param , modal.getPageParam() , User.class ) ;
		if( pageData == null || pageData.isEmpty() )
			return success( null ) ;
		
		trans.transUser(pageData, Arrays.asList("createId"), Arrays.asList("createName") )
		.transDict("ACCOUNT_STATUS", pageData, "status", "statusName");
		return success( pageData , pageData.getTotal() ) ;
	}
	
	/**
	 * 添加子账号
	 * 
	 * @param param
	 * @return
	 */
	@Post("")
	public String createSubAccount( @RequestBody @Validated UserParam param ){
		RoleCode roleCode = RoleCode.parse( Subject.getUser().getRoleCode() ) , subRole = null ;
		SysRole role =  roleService.findBy("code", roleCode , SysRole.class ) ;
		if( role == null || (subRole = RoleCode.subRole( roleCode ) ) == null ) {
			throw new BusinessException("当前用户无法创建子账号!") ;
		}
		param.setInstType( role.getType() );
		param.setRoleCode( subRole.name() );
		param.setCreateId( Subject.getUserId() );
		param.setInstId( Subject.getUser().getInstId() );
		param.setPasswd( CryptoUtil.sha256Encode( UserConstant.INIT_PASSWD ) );
		return success( userService.createUser( param ) ) ;
	}
	
	/**
	 * 获取子账号详情
	 * 
	 * @param userId
	 * @return
	 */
	@Get("/{id}")
	public String getSubAccount( @PathVariable("id") int userId ){
		User user = userService.findBy("id", userId , User.class ) ;
		if( user == null || RoleCode.subRole( RoleCode.parse( 
				Subject.getUser().getRoleCode() ) ) != RoleCode.parse( user.getRoleCode() ) )
			throw new BusinessException("子账号不存在!") ;
		return success( user ) ;
	}
	
	/**
	 * 更新子账号信息
	 * 
	 * @param userId
	 * @param param
	 * @return
	 */
	@Put("/{id}")
	public String updateSubAccount( @PathVariable("id") int userId , 
			@RequestBody @Validated UserParam param ){
	/*	SysRole subaccRole = roleService.getSubaccRole( Subject.getUser().getRoleId() ) ;
		if( subaccRole == null )
			return success( null ) ;
		
		User user = userService.findOne(new MapParam<String,Object>()
				.push("id" , userId ).push("roleId" , subaccRole.getId() ), User.class ) ;
		if( user == null )
			throw new BusinessException("用户不存在!") ;
		
		userService.updateUser(userId, param);*/
		return success( null ) ;
	}
	
	/**
	 * 删除子账号
	 * 
	 * @param userId
	 * @return
	 */
	@Delete("/{id}")
	public String deleteSubAccount( @PathVariable("id") int userId ){
		eduinstService.deleteEduinstSubAccount( userId , Subject.getUser().getInstId() );
		return success( null ) ;
	}
	
	/**
	 * 冻结账号
	 * 
	 * @param userId
	 * @return
	 */
	@Put("/{id}/disabled")
	public String disabledSubAccount( @PathVariable("id") int userId ){
		User user = null ;
		if( ( user =  userService.findBy("id", userId , User.class ) ) == null )
			throw new BusinessException("账号不存在!") ;
		userService.update("userId", userId ,
				new MapParam<String,Object>().push("status" , user.getStatus() == 1 ? 0 : 1 ) ) ;
		return success( null ) ;
	}
	
	/**
	 * 子账号分配二级指标查询,市教育局查询县教育局指标,县教育局查询学校指标 .
	 * 
	 * @param userId
	 * @return
	 */
	@Get("/{id}/quotas")
	public String findAccountQuota( @PathVariable("id") int userId ){
		SysRole role = roleService.findBy("code", Subject.getUser().getRoleCode() , SysRole.class ) ;
		RoleCode code = null ;
		if( role == null || (code = RoleCode.parse( role.getCode() ) ) == null 
				|| !Arrays.asList( RoleCode.COUNTY , RoleCode.COUNTY_SUBACC ,
						RoleCode.CITY , RoleCode.CITY_SUBACC ) .contains( code ) )
			return success( null ) ;
		QuotaType quotaType = null ;
		if( code == RoleCode.COUNTY || code == RoleCode.COUNTY_SUBACC )
			quotaType = QuotaType.SCHOOL ;
		else
			quotaType = QuotaType.EDUINST ;
		
		List<Quota> quota2List = quotaService.findList( new MapParam<String,Object>()
				.push("level" , 2 ).push("type" , quotaType.name() ), new PageParam().orderBy("rank", "desc") , Quota.class ) ;
		if( quota2List == null || quota2List.isEmpty() )
			return success( null ) ;
		
		disabledQuotaCheck( userId , quota2List ) ;
		checkedQuota( userId , quota2List ) ;
		
		return success( quotaService.structSubQuota( quota2List ) ) ;
	}
	
	private void checkedQuota( int userId , List<Quota> quota2List  ){
		List<Integer> subaccQuotasId = quotaService.findSubaccQuota( userId ) ;
		if( subaccQuotasId != null && ! subaccQuotasId.isEmpty() ){
			for(Quota quota2 : quota2List ){
				if(!subaccQuotasId.contains( quota2.getId() ) )
					continue ;
				quota2.setSubaccChecked( true );
				quota2.setDisabledCheck( false );
			}
		}
	}
	
	private void disabledQuotaCheck( int userId , List<Quota> quota2List ){
		User user = userService.findBy( "id" , userId , User.class ) ;
		if( user == null )
			throw new BusinessException("子账号不存在!") ;
		//查询其他子账号已分配指标
		List<User> users = userService.findList( new MapParam<String,Object>()
				.push("instId" , user.getInstId() )
				.push("instType" , user.getInstType() ) , null , User.class , "id") ;
		int index = -1 ;
		for( int i = 0 ; i < users.size() ; i ++  ){
			if( users.get(i).getId() != userId )
				continue ;
			index = i ;
			break ;
		}
		if( index != -1 )
			users.remove( index ) ;
		
		Integer[] usersId = new Integer[ users.size() ] ;
		if( usersId.length == 0 )
			return ;
		
		for( int i = 0 ; i < usersId.length ; i ++ ){
			usersId[i] = users.get(i).getId() ;
		}
		
		List<Integer> otherSubaccQuotaId = quotaService.findSubaccQuota( usersId ) ;
		if( otherSubaccQuotaId != null && ! otherSubaccQuotaId.isEmpty() ){
			for(Quota quota2 : quota2List ){
				if(otherSubaccQuotaId.contains( quota2.getId() ) )
					quota2.setDisabledCheck(true);
			}
		}
	}
	
	/**
	 * 子账号分配二级指标 .
	 * 
	 * @param userId
	 * @return
	 */
	@Put("/{id}/quotas")
	public String updateSubaccQuota( @PathVariable("id") int userId ,
			@RequestBody List<SubaccountQuotaParam> params  ){
		if( params != null && !params.isEmpty() )
			for( SubaccountQuotaParam param : params )
				param.setCreateId( Subject.getUserId() );
		quotaService.updateSubaccQuota( userId , params ) ;
		return success( null ) ;
	}
}
