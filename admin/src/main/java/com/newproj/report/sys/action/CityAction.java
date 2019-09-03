package com.newproj.report.sys.action;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.newproj.core.page.PageParam;
import com.newproj.core.page.RemotePage;
import com.newproj.core.rest.annotation.Api;
import com.newproj.core.rest.annotation.Delete;
import com.newproj.core.rest.annotation.Get;
import com.newproj.core.rest.annotation.Post;
import com.newproj.core.rest.annotation.Put;
import com.newproj.core.rest.support.ParamModal;
import com.newproj.core.rest.support.RestActionSupporter;
import com.newproj.core.validation.groups.CGroup;
import com.newproj.core.validation.groups.UGroup;
import com.newproj.report.context.Subject;
import com.newproj.report.context.trans.TransContext;
import com.newproj.report.eduinst.dto.Eduinst;
import com.newproj.report.eduinst.service.EduinstService;
import com.newproj.report.school.dto.School;
import com.newproj.report.school.service.SchoolService;
import com.newproj.report.sys.constant.UserConstant.InstType;
import com.newproj.report.sys.dto.City;
import com.newproj.report.sys.dto.CityParam;
import com.newproj.report.sys.service.CityService;
@Api("/city")
public class CityAction extends RestActionSupporter{
	
	@Autowired
	private CityService cityService ;
	
	@Autowired
	private TransContext trans ;
	
	@Autowired
	private EduinstService eduinstService ;
	
	@Autowired
	private SchoolService schoolService ;
	
	@Get("")
	public String findCity( ParamModal modal ){
		RemotePage<City> pageData =cityService.findList( modal.getParam("?name","parentId","id","level"), 
				modal.getPageParam() , City.class ) ;
		if(null!=pageData && null!=pageData.getData() && !pageData.getData().isEmpty()) {
			List<City> city = pageData.getData();
			for(City c : city ) {
				if(c.getParentId()>0) {
					Map<String, Object>  parmas = new HashMap<>();
					parmas.put("id", c.getParentId());
					City pct = cityService.findOne(parmas, City.class,"name");
					c.setParentName(pct.getName());
				}else {
					c.setParentName("江苏省");
				}
			}
		}
		
		trans.transDict("CITY_TYPE", pageData, "level", "levelName" )
		.transUser(pageData, Arrays.asList("createId","updateId"), Arrays.asList("createName","updateName") );
		return success( pageData , pageData == null ? 0 : pageData.getTotal() ) ;
	}
	
	@Get("/optionCity")
	public String findOptionCity( ParamModal modal ){
		Map<String,Object> param = modal.getParam("parentId") ;
		param.put("level", 1 ) ;
		InstType instType = InstType.parse( Subject.getUser().getInstType() ) ;
		int eduinstId = 0 ;
		if( instType == InstType.SCHOOL ){
			School school = schoolService.findBy("id", Subject.getUser().getInstId() , School.class ) ;
			if( school == null ) 
				return success( null ) ;
			eduinstId = school.getEduinstId() ;
		} else if( instType == InstType.EDUINST ){
			eduinstId =  Subject.getUser().getInstId() ;
		}
		if( eduinstId != 0 ){
			Eduinst eduinst = eduinstService.findBy("id", Subject.getUser().getInstId() , Eduinst.class ) ;
			if( eduinst == null )
				return success( null ) ;
			if( eduinst.getCityId() != null && eduinst.getCityId() != 0 )
				param.put("id", eduinst.getCityId() ) ;
		}
		return success( cityService.findList( param , new PageParam().orderBy("rank" , "ASC") , City.class ) ) ;
	}
	
	@Get("/optionCounty")
	public String findOptionCounty( ParamModal modal ){
		Map<String,Object> param = modal.getParam("parentId") ;
		param.put("level", 2 ) ;
		InstType instType = InstType.parse( Subject.getUser().getInstType() ) ;
		int eduinstId = 0 ;
		if( instType == InstType.SCHOOL ){
			School school = schoolService.findBy("id", Subject.getUser().getInstId() , School.class ) ;
			if( school == null ) 
				return success( null ) ;
			eduinstId = school.getEduinstId() ;
		} else if( instType == InstType.EDUINST ){
			eduinstId =  Subject.getUser().getInstId() ;
		}
		if( eduinstId != 0 ){
			Eduinst eduinst = eduinstService.findBy("id", Subject.getUser().getInstId() , Eduinst.class ) ;
			if( eduinst == null )
				return success( null ) ;
			if( eduinst.getCountyId() != null && eduinst.getCountyId() != 0 )
				param.put("id", eduinst.getCountyId() ) ;
		}
		return success( cityService.findList( param , new PageParam().orderBy("rank" , "ASC") , City.class ) ) ;
	}
	
	@Post("")
	public String createCity( @RequestBody @Validated(CGroup.class) CityParam param ){
		param.setCreateId( Subject.getUserId() );
		return success( cityService.createCity( param ) ) ;
	}
	
	@Get("/{id}")
	public String getCity( @PathVariable("id") int id ){
		include(City.class , "id","name","parentId","level") ;
		return success( cityService.findBy("id", id , City.class  ) ) ;
	}
	
	@Put("/{id}")
	public String updateCity( @PathVariable("id") int id , 
			@RequestBody @Validated(UGroup.class) CityParam param	){
		param.setUpdateId(Subject.getUserId() );
		cityService.updateCity(id, param);
		return success( null ) ;
	}
	
	@Delete("/{id}")
	public String deleteCity( @PathVariable("id") int id ){
		cityService.deleteCity(id);
		return success( null ) ;
	}
	
}
