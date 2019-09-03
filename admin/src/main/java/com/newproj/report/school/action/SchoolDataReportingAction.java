package com.newproj.report.school.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;

import com.newproj.core.rest.annotation.Api;
import com.newproj.core.rest.annotation.Get;
import com.newproj.core.rest.support.RestActionSupporter;
import com.newproj.report.context.Subject;
import com.newproj.report.context.trans.TransContext;
import com.newproj.report.eduinst.EduinstConstant.EduinstType;
import com.newproj.report.eduinst.dto.Eduinst;
import com.newproj.report.eduinst.service.EduinstService;
import com.newproj.report.reporting.dto.Reporting;
import com.newproj.report.reporting.service.ReportingService;
import com.newproj.report.school.dto.SchoolDataReporting;
import com.newproj.report.school.service.SchoolDataReportingService;
import com.newproj.report.sys.constant.RoleConstant.RoleCode;
import com.newproj.report.sys.constant.UserConstant.InstType;
import com.newproj.report.sys.dto.City;
import com.newproj.report.sys.service.CityService;

@Api("/schoolDataReporting")
public class SchoolDataReportingAction extends RestActionSupporter{

	@Autowired
	private SchoolDataReportingService dataReportingService ;
	
	@Autowired
	private TransContext trans ;
	
	@Autowired
	private ReportingService reportingService ;
	
	@Autowired
	private EduinstService eduinstService ;
	
	@Autowired
	private CityService cityService ;
	
	/**
	 * 获取填报整体进度 .
	 * 
	 * @return
	 */
	@Get("/process")
	public String findProcessReporting( ){
		Map<String,Object> param = new HashMap<String,Object>() ;
		Reporting reporting = reportingService.getPresent() ;
		if( reporting == null )
			return success( null ) ;
		Eduinst eduinst = null ;
		if( InstType.parse(Subject.getUser().getInstType() ) != InstType.EDUINST
				|| ( eduinst = eduinstService.findBy("id", Subject.getUser().getInstId() , Eduinst.class ) ) == null
				||  EduinstType.parse( eduinst.getType() ) != EduinstType.COUNTY )
			return success( null ) ;
		param.put("reportingId", reporting.getId() ) ;
		param.put("countyId", eduinst.getCountyId() ) ;
		param.put("cityId", eduinst.getCityId() ) ;
		List<SchoolDataReporting> dataList = dataReportingService.findProcessReporting( param ) ;
		return success( dataList ) ;
	}
	
	@Get("/groupProcessCity")
	public String groupProcessCitys(){
		InstType instType = InstType.parse( Subject.getUser().getInstType() ) ;
		Integer parentId = null ;
		if( instType == InstType.EDUINST ){
			Eduinst eduinst = eduinstService.findBy("id", Subject.getUser().getInstId() , Eduinst.class ) ;
			if( eduinst == null )
				return success( null ) ;
			EduinstType eduinstType = EduinstType.parse( eduinst.getType() ) ;
			if( eduinstType == EduinstType.PROVINCE )
				parentId = 0 ;
			else if( eduinstType == EduinstType.CITY )
				parentId = eduinst.getCityId() ;
			else if( eduinstType == EduinstType.COUNTY )
				parentId = eduinst.getCountyId() ;
		}else if( instType == InstType.SYS ){
			parentId = 0 ;
		}
		List<City> citys = cityService.findCityByParent(parentId, true ) ;
		City parentCiyt = cityService.findBy("id", parentId, City.class ) ;
		if( parentCiyt != null ){
			parentCiyt.setSubCitys( citys );
			citys = Arrays.asList( parentCiyt ) ;
		}
		return success( citys ) ;
	}
	
	@Get("/cityGroupProcess")
	public String cityGroupProcess(
			@RequestParam( value = "parentId" , required = false ) Integer parentId ){
		List<City> citys = new ArrayList<City>() ;
		City self = cityService.findBy("id", parentId, City.class ) ;
		if( self != null ) citys.add(0 , self);
		List<City> subCitys = cityService.findListBy("parentId", parentId, City.class ) ;
		if( subCitys != null && !subCitys.isEmpty() )
			citys.addAll( subCitys ) ;
		
		Reporting reporting = null ;
		if( citys == null || citys.isEmpty() || 
				( reporting = reportingService.getPresent() ) == null )
			return success( null ) ;
		
		List<Map<String, Object>> resultList = new ArrayList< Map<String,Object> >() ;
		for( City city : citys ){
			Map<String , Object> result = new HashMap<String,Object>() ;
			Map<String,Object> param = new HashMap<String,Object>() ;
			param.put("reportingId", reporting.getId() ) ;
			if( city.getLevel() == 1 )
				param.put("cityId", city.getId() ) ;
			else if( city.getLevel() == 2 )
				param.put("countyId", city.getId() ) ;
			else continue ;
			result.put("name", city.getName() ) ;
			result.put("cityId", city.getId() ) ;
			result.put("data", dataReportingService.findProcessReporting( param ) ) ;
			resultList.add( result ) ;
		}
		return success( resultList ) ;
	}
}
