package com.newproj.report.school.dal.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.newproj.core.beans.BeanUtil;
import com.newproj.core.mybatis.AbstractBaseMapper;
@Repository
public class SchoolDataReportingDao extends AbstractBaseMapper{

	@Override
	public void init() {
		// TODO Auto-generated method stub
	}
	
	public <T> List<T> findProcessReporting( Map<String,Object> param , Class<T> clazz ){
		if( !param.containsKey("reportingId") )
			return null ;
		List<Map<String,Object>> dataList = sqlTemplate.selectList("SchoolDataReportingMapper.findProcessReporting" , param );
		if( dataList == null || dataList.isEmpty() )
			return null ;
		handleHumpKey(dataList);
		return BeanUtil.convertMapList( dataList , clazz) ;
	}

}
