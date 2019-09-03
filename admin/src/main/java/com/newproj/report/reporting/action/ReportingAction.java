package com.newproj.report.reporting.action;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.newproj.core.exception.BusinessException;
import com.newproj.core.page.RemotePage;
import com.newproj.core.rest.annotation.Api;
import com.newproj.core.rest.annotation.Get;
import com.newproj.core.rest.annotation.Post;
import com.newproj.core.rest.annotation.Put;
import com.newproj.core.rest.support.ParamModal;
import com.newproj.core.rest.support.RestActionSupporter;
import com.newproj.core.util.TimeUtil;
import com.newproj.core.validation.groups.CGroup;
import com.newproj.core.validation.groups.UGroup;
import com.newproj.report.context.Subject;
import com.newproj.report.context.trans.TransContext;
import com.newproj.report.reporting.dto.Reporting;
import com.newproj.report.reporting.dto.ReportingParam;
import com.newproj.report.reporting.service.ReportingService;

@Api("/reporting")
public class ReportingAction extends RestActionSupporter{

	@Autowired
	private ReportingService reportingService ;
	
	@Autowired
	private TransContext trans ;
	
	/**
	 * 获取填报列表
	 * 
	 * @param modal
	 * @return
	 */
	@Get("")
	public String findList( ParamModal modal ){
		Map<String,Object> param = modal.getParam("title") ;
		vagueField(param, "title");//模糊检索
		RemotePage<Reporting> pageData = reportingService .findList( param , modal.getPageParam() , Reporting.class ) ;
		if( pageData == null || pageData.isEmpty() )
			return success( null ) ;
		trans.transUser(pageData, Arrays.asList("createId","updateId"), Arrays.asList("createName" , "updateName") ) ;
		return success( pageData , pageData.getTotal() ) ;
	}
	
	/**
	 * 创建填报
	 * 
	 * @param param
	 * @return
	 */
	@Post("")
	public String createReporting( @RequestBody @Validated(CGroup.class) ReportingParam param ){
		param.setCreateId( Subject.getUserId() );
		return success( reportingService.createReporting( param ) ) ;
	}
	
	/**
	 * 获取填报详情
	 * 
	 * @param reportingId
	 * @return
	 */
	@Get("/{id}")
	public String getReporting( @PathVariable("id") int reportingId ){
		return success( reportingService.findBy("id", reportingId, Reporting.class  ) ) ;
	}
	
	/**
	 * 修改填报记录
	 * 
	 * @param reportingId
	 * @param param
	 * @return
	 */
	@Put("/{id}")
	public String updateReport(  @PathVariable("id") int reportingId ,
			@RequestBody @Validated(UGroup.class) ReportingParam param ){
		param.setUpdateId( Subject.getUserId() );
		reportingService.updateReporting(reportingId, param);
		return success( null ) ;
	}
	
	/**
	 * 开始填报
	 * 
	 * @param reportingId
	 * @param param
	 * @return
	 */
	@Put("/{id}/start")
	public String startReporting(  @PathVariable("id") int reportingId , 
			@RequestBody ReportingParam param ){
		String expireTime = param.getExpireTime() ;
		if( expireTime == null )
			throw new BusinessException("填报结束日期不能为空!") ;
		Date expireDate = TimeUtil.str2date( expireTime ) ;
		if( expireDate == null || expireDate.getTime() <= new Date().getTime() )
			throw new BusinessException("填报结束日期不能小于当前日期!") ;
		
		param.setUpdateId( Subject.getUserId() );
		param.setStartTime( TimeUtil.now() );
		reportingService.updateReporting(reportingId, param, "updateId" , "updateTime" , "startTime" , "expireTime");
		return success( null ) ;
	}
	
	/**
	 * 结束填报
	 * 
	 * @return
	 */
	@Put("/{id}/stop")
	public String stopReporting( ){
		return success( null ) ;
	}
}
