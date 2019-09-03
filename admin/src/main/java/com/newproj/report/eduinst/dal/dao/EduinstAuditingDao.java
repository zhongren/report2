package com.newproj.report.eduinst.dal.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.newproj.core.beans.BeanUtil;
import com.newproj.core.mybatis.AbstractBaseMapper;
import com.newproj.core.mybatis.Table;

@Repository
@Table("tb_eduinst_auditing")
public class EduinstAuditingDao extends AbstractBaseMapper{

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
	
	public <T> List<T> findAuditing( Map<String,Object> param , Class<T> clazz ){
		if( param.get("reportingId") == null || param.get("cityId") == null )
			return null ;
		
		List<Map<String,Object> > dataList = sqlTemplate.selectList("EduinstAuditingMapper.findAuditing" , param ) ;
		if( dataList == null || dataList.isEmpty() )
			return null ;
		handleHumpKey(dataList);
		return BeanUtil.convertMapList(dataList, clazz) ;
	}
	
	public <T> List<T> findSubaccAuditing( Map<String,Object> param , Class<T> clazz ){
		if( param.get("reportingId") == null || param.get("userId") == null
				|| param.get("cityId") == null )
			return null ;
		
		List<Map<String,Object> > dataList = sqlTemplate.selectList("EduinstAuditingMapper.findSubaccAuditing" , param ) ;
		if( dataList == null || dataList.isEmpty() )
			return null ;
		handleHumpKey(dataList);
		return BeanUtil.convertMapList(dataList, clazz) ;
	}
	
	public <T> List<T> findReporting( Map<String,Object> param , Class<T> clazz ){
		if( param.get("reportingId") == null  )
			return null ;
		
		List<Map<String,Object> > dataList = sqlTemplate.selectList("EduinstAuditingMapper.findReporting" , param ) ;
		if( dataList == null || dataList.isEmpty() )
			return null ;
		handleHumpKey(dataList);
		return BeanUtil.convertMapList(dataList, clazz) ;
	}
	
	public <T> List<T> exportReportingData( int eduinstId , int reportingId , Class<T> clazz ){
		Map<String,Object> param = new HashMap<String,Object>() ;
		param.put("eduinstId", eduinstId ) ;
		param.put("reportingId", reportingId ) ;
		List<Map<String,Object> > dataList = sqlTemplate.selectList("EduinstAuditingMapper.findReportingData" , param ) ;
		if( dataList == null || dataList.isEmpty() )
			return null ;
		handleHumpKey(dataList);
		return BeanUtil.convertMapList(dataList, clazz) ;
	}

}
