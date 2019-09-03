package com.newproj.report.eduinst.action;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.newproj.core.beans.MapParam;
import com.newproj.core.page.PageParam;
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
import com.newproj.report.eduinst.EduinstConstant.EduinstType;
import com.newproj.report.eduinst.dto.Eduinst;
import com.newproj.report.eduinst.dto.EduinstParam;
import com.newproj.report.eduinst.service.EduinstService;
import com.newproj.report.sys.constant.RoleConstant.RoleCode;
import com.newproj.report.sys.constant.UserConstant.InstType;
import com.newproj.report.sys.dto.User;
import com.newproj.report.sys.service.UserService;

@Api("eduinst")
public class EduinstAction extends RestActionSupporter{

	@Autowired
	private EduinstService instService ;
	
	@Autowired
	private TransContext trans ;
	
	@Autowired
	private UserService userService ;
	
	@Get("")
	public String findEduinst( ParamModal modal ){
		Map<String,Object> param =  modal.getParam("?name","type","code") ;
		if( InstType.parse( Subject.getUser().getInstType() ) == InstType.EDUINST ){
			Eduinst eduinst = instService.findBy("id", Subject.getUser().getInstId() , Eduinst.class ) ;
			EduinstType type = EduinstType.parse( eduinst.getType() ) ;
			if( EduinstType.CITY == type ) {
				param.put("cityId", eduinst.getCityId() ) ;
				param.put("type", EduinstType.COUNTY ) ;
			}else if( EduinstType.COUNTY ==  type )
				param.put("countyId", eduinst.getCountyId() ) ;
		}
			
		RemotePage<Eduinst> pageData = instService.findList( param , modal.getPageParam() , Eduinst.class ) ;
		/*JoinUtil.join( pageData , new String[]{"loginName","accountStatus"}, "id", 
				userService.findList( new MapParam<String,Object>() 
						.push("instId" , JoinUtil.fieldsValue( pageData, "id") )
						.push("roleCode" , Arrays.asList( RoleCode.PROVINCE , RoleCode.COUNTY , RoleCode.CITY ) ) 
						, null , User.class ), new String[]{"username" , "status"}, "instId");*/
		trans.transCity( pageData, Arrays.asList("cityId" , "countyId"),
				Arrays.asList("cityName" , "countyName") )
		.transDict("INST_TYPE", pageData ,"type", "typeName" )
		.transDict("ACCOUNT_STATUS", pageData, "accountStatus", "accountStatusName" )
		.transUser(pageData, Arrays.asList("createId","updateId"), Arrays.asList("createName" , "updateName") );
		return success( pageData , pageData == null ? 0 : pageData.getTotal() ) ; 
	}
	
	@Get("/option")
	public String findEduinstForOption( ParamModal modal ){
		Map<String,Object> param = modal.getParam("?name","type","cityId" , "countyId") ;
		include(Eduinst.class, "id","name") ;
		return success( instService.findList( param , new PageParam().orderBy("name" , "ASC") , Eduinst.class ) ) ;
	}
	
	@Post("")
	public String createEduinst( @RequestBody @Validated(CGroup.class) EduinstParam param ){
		param.setCreateId( Subject.getUserId() );
		return success( instService.createEduinst( param ) ) ;
	}
	
	@Get("/{id}")
	public String getEduinst( @PathVariable("id") int id ){
		return success( instService.findBy("id", id , Eduinst.class ) ) ;
	}
	
	@Delete("/{id}")
	public String deleteEduinst( @PathVariable("id") int id ){
		instService.deleteEduinst( id ) ;
		return success(null) ;
	}
	
	@Put("/{id}")
	public String updateEduinst( @PathVariable("id") int id ,
			@RequestBody @Validated(UGroup.class) EduinstParam param ){
		param.setUpdateId( Subject.getUserId() );
		instService.updateEduinst(id, param);
		return success( null ) ;
	}
}
