package com.newproj.report.school.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newproj.report.school.dal.dao.SchoolDataReportingDao;
import com.newproj.report.school.dto.School;
import com.newproj.report.school.dto.SchoolDataReporting;

@Service
public class SchoolDataReportingService {
	
	@Autowired
	private SchoolDataReportingDao dataReportingDao ;
	
	public List<SchoolDataReporting> findProcessReporting( Map<String,Object> param ){
		List<School> dataList = dataReportingDao.findProcessReporting(param, School.class ) ;
		if( dataList == null || dataList.isEmpty() )
			return null ;
		
		//Caculate rate ;
		Map<Integer , SchoolDataReporting> result = new HashMap<Integer ,SchoolDataReporting>() ;
		result.put( 0 , new SchoolDataReporting( 0 , "未提交审核" , 0 , 0f ) ) ;
		result.put( 1 , new SchoolDataReporting( 1 , "区县审核中" , 0 , 0f ) ) ;
		result.put( 2 , new SchoolDataReporting( 2 , "审核已结束" , 0 , 0f ) ) ;
		for( School school : dataList ){
			SchoolDataReporting data = null ;
			if( school.getReportStatus() == null || 
					Arrays.asList(0 , 4 ).contains( school.getReportStatus() ) )
				data = result.get( 0 ) ;//未提交审核(未提交,返回修改)
			else if( Arrays.asList( 1 , 2 , 3 ).contains(  school.getReportStatus() ) )
				data = result.get( 1 ) ;//审核中(待审核,审核中,审核完成未提交上级)
			else 
				data = result.get(2) ; //审核中(待审核,审核中,审核完成未提交上级)
			data.setAmount( data.getAmount() + 1 );
		}
		//计算比例
		List<SchoolDataReporting> resultList = new ArrayList<SchoolDataReporting>() ;
		for( Map.Entry<Integer, SchoolDataReporting> entry : result.entrySet() ){
			entry.getValue().setRate( dataList.isEmpty() ? 0f : new BigDecimal( 
					( entry.getValue().getAmount() * 1f ) / dataList.size() * 100 ).setScale(BigDecimal.ROUND_HALF_UP , 2).floatValue() );
			resultList.add( entry.getValue() ) ;
		}
		return resultList ;
	}
}
