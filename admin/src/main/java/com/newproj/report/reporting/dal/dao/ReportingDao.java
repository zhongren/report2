package com.newproj.report.reporting.dal.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.newproj.core.beans.BeanUtil;
import com.newproj.core.mybatis.AbstractBaseMapper;
import com.newproj.core.mybatis.Table;

@Repository
@Table("tb_report")
public class ReportingDao extends AbstractBaseMapper{

	public <T> T getPresent( Class<T> clazz ){
		Map<String,Object> data = sqlTemplate.selectOne("ReportingMapper.getPresent") ;
		if( data == null || data.isEmpty() ){
			return null ;
		}
		handleHumpKey(data);
		return BeanUtil.convertMap( data , clazz) ;
	}
	
	public <T> T getReportingByTimeRange( String startTime , String expireTime , Class<T> clazz ){
		if( startTime == null || expireTime == null )
			throw new RuntimeException("开始日期或者过期日期为空") ;
		Map<String,Object> param = new HashMap<String,Object>() ;
		param.put("startTime", startTime ) ;
		param.put("expireTime", expireTime ) ;
		Map<String,Object> data = sqlTemplate.selectOne("ReportingMapper.getReportingByTimeRange" , param ) ;
		if( data == null || data.isEmpty() ){
			return null ;
		}
		handleHumpKey(data);
		return BeanUtil.convertMap(data, clazz) ;
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
	}

}
