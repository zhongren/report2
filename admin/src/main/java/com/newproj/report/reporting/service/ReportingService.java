package com.newproj.report.reporting.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newproj.core.exception.BusinessException;
import com.newproj.core.rest.support.AbstractBaseService;
import com.newproj.core.util.NumberUtil;
import com.newproj.core.util.TimeUtil;
import com.newproj.report.reporting.dal.dao.ReportingDao;
import com.newproj.report.reporting.dto.Reporting;
import com.newproj.report.reporting.dto.ReportingParam;
@Service
public class ReportingService extends AbstractBaseService{

	@Autowired
	private ReportingDao reportingDao ;
	
	@Override
	public void init() {
		setMapper( reportingDao ) ;
	}

	public Reporting getPresent(){
		return reportingDao.getPresent( Reporting.class ) ;
	}
	
	/**
	 * 创建填报记录
	 * 
	 * @param param
	 * @return
	 */
	public int createReporting( ReportingParam param ){
		validateReportingTimeRange( param.getStartTime() , param.getExpireTime() , null ) ;
		param.setCreateTime( TimeUtil.now() ) ;
		return NumberUtil.parseInt( reportingDao.createBean( param , ReportingParam.class ), 0 ) ;
	}
	
	private void validateReportingTimeRange( String startTime , String expireTime , Integer reportingId ){
		if( startTime == null || expireTime == null )
			throw new BusinessException("开始日期和结束日期不能为空!") ;
		Reporting reporting = reportingDao.getReportingByTimeRange(startTime, expireTime, Reporting.class ) ;
		if( reporting == null || ( reportingId != null && reporting.getId().equals( reportingId ) )  )
			return ;
		throw new BusinessException("当前日期范围已存在,请重新选择日期范围!") ;
	}
	
	/**
	 * 修改填报记录
	 * 
	 * @param reportId
	 * @param param
	 */
	public void updateReporting( int reportId , ReportingParam param , String ... properties ){
		if( reportingDao.findBeanBy("id", reportId, Reporting.class , "id") == null )
			throw new BusinessException("数据不存在,更新失败!") ;
		validateReportingTimeRange( param.getStartTime() , param.getExpireTime() , reportId ) ;
		param.setUpdateTime(TimeUtil.now() );
		reportingDao.updateBean("id", reportId, param , properties ) ;
	}
	
}
