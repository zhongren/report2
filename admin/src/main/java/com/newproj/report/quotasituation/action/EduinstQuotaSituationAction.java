package com.newproj.report.quotasituation.action;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.newproj.core.exception.BusinessException;
import com.newproj.core.rest.annotation.Api;
import com.newproj.core.rest.annotation.Get;
import com.newproj.core.rest.support.RestActionSupporter;
import com.newproj.core.util.NumberUtil;
import com.newproj.report.quotasituation.service.EduinstQuotaSituationService;
import com.newproj.report.reporting.dto.Reporting;
import com.newproj.report.reporting.service.ReportingService;

@Api("eduinstQuotaSituation")
public class EduinstQuotaSituationAction extends RestActionSupporter{
	
	@Autowired
	private EduinstQuotaSituationService eduinstSituationService ;
	
	@Autowired
	private ReportingService reportingService ;

	@Get("calculate/{eduinstId}")
	public String calculate( @PathVariable("eduinstId") String eduinstId ){
		Reporting reporting = reportingService.getPresent() ;
		if( reporting == null )
			throw new BusinessException("填报已关闭!") ;
		eduinstSituationService.calculate(reporting.getId(), NumberUtil.parseInt( eduinstId , 0 ) );
		return success( null ) ;
	}
	
	@Get("calculate")
	public String calculateBatch( @RequestParam( required = false , value = "type" ) String type ,
			@RequestParam( required = false , value = "eduinstId" ) Integer eduinstId ){
		if( !Arrays.asList("all","city" , "county" , "province").contains( type.toLowerCase() ) )
			throw new BusinessException("无效的类型!") ;
		
		Reporting reporting = reportingService.getPresent() ;
		if( reporting == null )
			throw new BusinessException("填报已关闭!") ;
		eduinstSituationService.calculate(reporting.getId(), NumberUtil.parseInt( eduinstId , 0 ) );
		return success( null ) ;
	}
	
}
