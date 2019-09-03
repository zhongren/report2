package com.newproj.report.school.dal.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.newproj.core.beans.BeanUtil;
import com.newproj.core.mybatis.AbstractBaseMapper;
import com.newproj.core.mybatis.Table;

@Repository
@Table("tb_school_auditing")
public class SchoolAuditingDao extends AbstractBaseMapper{
	public void init() {
		
	}
	
	public <T> List<T> findSubaccAuditing( Map<String,Object> param , Class<T> clazz ){
		if( param.get("reportingId") == null || param.get("userId") == null )
			return null ;
		
		List<Map<String,Object> > dataList = sqlTemplate.selectList("SchoolAuditingMapper.findSubaccAuditing" , param ) ;
		if( dataList == null || dataList.isEmpty() )
			return null ;
		handleHumpKey(dataList);
		return BeanUtil.convertMapList(dataList, clazz) ;
	}
	
	public <T> List<T> findAuditing( Map<String,Object> param , Class<T> clazz ){
		if( param.get("reportingId") == null )
			return null ;
		
		List<Map<String,Object> > dataList = sqlTemplate.selectList("SchoolAuditingMapper.findAuditing" , param ) ;
		if( dataList == null || dataList.isEmpty() )
			return null ;
		handleHumpKey(dataList);
		return BeanUtil.convertMapList(dataList, clazz) ;
	}
	
	public <T> List<T> findReporting( Map<String,Object> param , Class<T> clazz ){
		if( param.get("reportingId") == null  )
			return null ;
		
		List<Map<String,Object> > dataList = sqlTemplate.selectList("SchoolAuditingMapper.findReporting" , param ) ;
		if( dataList == null || dataList.isEmpty() )
			return null ;
		handleHumpKey(dataList);
		return BeanUtil.convertMapList(dataList, clazz) ;
	}
	
	public <T> List<T> exportReportingData( int schoolId , int schoolType , int reportingId , Class<T> clazz ){
		Map<String,Object> param = new HashMap<String,Object>() ;
		param.put("schoolId", schoolId ) ;
		param.put("reportingId", reportingId ) ;
		param.put("schoolType", schoolType ) ;
		List<Map<String,Object> > dataList = sqlTemplate.selectList("SchoolAuditingMapper.findReportingData" , param ) ;
		if( dataList == null || dataList.isEmpty() )
			return null ;
		handleHumpKey(dataList);
		return BeanUtil.convertMapList(dataList, clazz) ;
	}
}
