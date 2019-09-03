package com.newproj.report.quotasituation.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;

import com.newproj.core.exception.BusinessException;
import com.newproj.core.rest.annotation.Api;
import com.newproj.core.rest.annotation.Get;
import com.newproj.core.rest.support.RestActionSupporter;
import com.newproj.core.util.NumberUtil;
import com.newproj.report.quotasituation.service.SchoolQuotaSituationService;
import com.newproj.report.reporting.dto.Reporting;
import com.newproj.report.reporting.service.ReportingService;

@Api("schoolQuotaSituation")
public class SchoolQuotaSituationAction extends RestActionSupporter {

	@Autowired
	private SchoolQuotaSituationService schoolSituationService ;
	
	@Autowired
	private ReportingService reportingService ;
	
	@Get("calculate/{schoolId}")
	public String calculate( @PathVariable("schoolId") String schoolId ){
		Reporting reporting = reportingService.getPresent() ;
		if( reporting == null )
			throw new BusinessException("填报已关闭!") ;
		schoolSituationService.calculate(reporting.getId(), NumberUtil.parseInt( schoolId , 0 ) );
		return success( null ) ;
	}
}
