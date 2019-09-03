package com.newproj.report.quota.dal.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.newproj.core.beans.BeanUtil;
import com.newproj.core.mybatis.AbstractBaseMapper;
import com.newproj.core.mybatis.Table;

@Repository
@Table("tb_quota")
public class QuotaDao extends AbstractBaseMapper{

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
	
	public <T> List<T> findSchoolQuota( Integer schoolType , int reportId , Class<T> clazz ){
		Map<String,Object> param = new HashMap<String,Object>() ;
		param.put("schoolType", schoolType );
		List<Map<String,Object>> dataList = sqlTemplate.selectList("QuotaMapper.findSchoolQuota" , param ) ;
		if( dataList == null || dataList.isEmpty() ){
			return null ;
		}
		handleHumpKey(dataList); //处理驼峰规则映射 .
		return BeanUtil.convertMapList( dataList , clazz) ;
	}
	
	public <T> List<T> findEduinstQuota( String eduinstType , int reportId , Class<T> clazz ){
		Map<String,Object> param = new HashMap<String,Object>() ;
		param.put("instType", eduinstType );
		param.put("reportId", reportId ) ;
		List<Map<String,Object>> dataList = sqlTemplate.selectList("QuotaMapper.findEduinstQuota" , param ) ;
		if( dataList == null || dataList.isEmpty() ){
			return null ;
		}
		handleHumpKey(dataList); //处理驼峰规则映射 .
		return BeanUtil.convertMapList( dataList , clazz) ;
	}

}
