package com.newproj.report.school.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;

import com.newproj.core.rest.annotation.Api;
import com.newproj.core.rest.annotation.Get;
import com.newproj.core.rest.support.ParamModal;
import com.newproj.core.rest.support.RestActionSupporter;
import com.newproj.report.export.bean.SchoolClassesNumBean;
import com.newproj.report.reporting.dto.Reporting;
import com.newproj.report.reporting.service.ReportingService;
import com.newproj.report.school.service.SchoolClassesNumsService;
/**
 * 学校班额 .
 * 
 * @author 10147
 *
 */
@Api("schoolClass")
public class SchoolClassAction  extends RestActionSupporter{
	
	@Autowired
	private ReportingService reportingService ;
	
	@Autowired
	private SchoolClassesNumsService classNumService ;
	
	@Get("/{schoolId}/classes")
	public String findClasses( ParamModal modal , @PathVariable("schoolId") int schoolId ){
		Reporting reporting = reportingService.getPresent() ;
		if( reporting == null )
			return success( null ) ;
		modal.put("schoolId", schoolId ) ;
		modal.put("year", reporting.getId() ) ;
		modal.put("page", 0) ;
		return success( classNumService.findList( modal.getParam("schoolId" , "year"), 
				modal.getPageParam().orderBy("name", "ASC").orderBy("classes", "ASC") , SchoolClassesNumBean.class ) );
	}
	
}
