package com.newproj.report.collection.dal.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.newproj.core.beans.BeanUtil;
import com.newproj.core.beans.MapParam;
import com.newproj.core.mybatis.AbstractBaseMapper;
import com.newproj.core.mybatis.Table;

@Repository
@Table("tb_collection")
public class CollectionDao extends AbstractBaseMapper{

	public <T> List<T> findSchoolCollectionWithQuota(
			int reportId , int schoolId ,  int schoolType ,  int quotaId , Class<T> clazz ){
		List<Map<String,Object>> dataList = sqlTemplate.selectList("CollectionMapper.findSchoolCollectionWithQuota" , 
				new MapParam<String,Object>().push("reportingId" , reportId ) 
				.push("quotaId" , quotaId ) 
				.push("schoolType" , schoolType )
				.push("schoolId" , schoolId ) ) ;
		if( dataList == null || dataList.isEmpty() )
			return null ;
		handleHumpKey(dataList);
		return BeanUtil.convertMapList(dataList, clazz) ;
	}
	
	public <T> List<T> findSchoolCollection( int reportId , Integer schoolType , Class<T> clazz ){
		List<Map<String,Object>> dataList = sqlTemplate.selectList("CollectionMapper.findSchoolCollection" , 
				new MapParam<String,Object>().push("reportId" , reportId ) 
				.push("schoolType" , schoolType ) ) ;
		if( dataList == null || dataList.isEmpty() )
			return null ;
		handleHumpKey(dataList);
		return BeanUtil.convertMapList(dataList, clazz) ;
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
	
	public <T> List<T> findCollectionBySchoolType(int type ,Class<T> clazz ){
		List<Map<String,Object>> dataList = sqlTemplate.selectList("CollectionMapper.findCollectionBySchoolType" , 
				new MapParam<String,Object>().push("type" , type ) ) ;
		if( dataList == null || dataList.isEmpty() )
			return null ;
		handleHumpKey(dataList);
		return BeanUtil.convertMapList(dataList, clazz) ;
	}

}
