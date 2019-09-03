package com.newproj.report.sys.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;

import com.newproj.core.beans.MapParam;
import com.newproj.core.page.PageParam;
import com.newproj.core.rest.annotation.Api;
import com.newproj.core.rest.annotation.Get;
import com.newproj.core.rest.support.ParamModal;
import com.newproj.core.rest.support.RestActionSupporter;
import com.newproj.report.context.Subject;
import com.newproj.report.eduinst.dto.Eduinst;
import com.newproj.report.eduinst.service.EduinstService;
import com.newproj.report.sys.constant.RoleConstant.RoleCode;
import com.newproj.report.sys.constant.UserConstant.InstType;
import com.newproj.report.sys.dto.Dict;
import com.newproj.report.sys.service.DictService;

@Api("dict")
public class DictAction extends RestActionSupporter{

	@Autowired
	private DictService dictService ;
	@Autowired
	private EduinstService eduinstService ;
	
	@Get("/{type}")
	public String findDict( @PathVariable("type") String type , ParamModal modal ){
		MapParam<String,Object>	param  = new MapParam<String,Object>();
		param.push("type" , type );
		param.push( modal.getParam("id","parentId","level","code"));
		InstType instType = InstType.parse( Subject.getUser().getInstType() ) ;
		if(instType == InstType.EDUINST && type.equals("INST_TYPE")) {
			Eduinst eduinst = eduinstService.findBy("id", Subject.getUser().getInstId() , Eduinst.class ) ;
			if(eduinst.getCountyId() == 0 && eduinst.getCityId() > 0) {
				param.push("code", RoleCode.COUNTY.name() ) ;
			}
		}
		return success( dictService.findList(param, new PageParam().orderBy("rank", "ASC"), Dict.class ) ) ;
	}
	
	
}
