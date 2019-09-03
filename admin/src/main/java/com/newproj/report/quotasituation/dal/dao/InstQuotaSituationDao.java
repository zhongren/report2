package com.newproj.report.quotasituation.dal.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.newproj.core.beans.BeanUtil;
import com.newproj.core.mybatis.AbstractBaseMapper;
import com.newproj.core.mybatis.Table;

@Repository
@Table("tb_inst_quota_situation")
public class InstQuotaSituationDao extends AbstractBaseMapper{

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 根据采集项名称查询学校常态数据表 .
	 * 
	 * @param param 选填:schoolId , countyId , cityId 
	 * @param param 必填:reportingId , collectionId
	 * @param clazz
	 * @return
	 */
	public <T> List<T> findSchoolCollectionReportWithCollection( Map<String,Object> param , Class<T> clazz ){
		if( param.get("reportingId") == null || param.get("collectionId") == null )
			return null ;
		List<Map<String,Object>> mapList = sqlTemplate.selectList(
				"InstQuotaSituationMapper.findSchoolCollectionReportWithCollection", param ) ;
		handleHumpKey(mapList);
		return BeanUtil.convertMapList(mapList, clazz) ;
	}
	
	/**
	 * 标准化建设监测达成情况表 .
	 * 
	 * @param param 必填:reportingId , instType , instId
	 * @param clazz
	 * @return
	 */
	public <T> List<T> findInstSituationReport( Map<String,Object> param , Class<T> clazz ){
		if( param.get("reportingId") == null || param.get("instType") == null 
				|| param.get("instId") == null || param.get("schoolType") == null )
			return null ;
		List<Map<String,Object>> mapList = sqlTemplate.selectList(
				"InstQuotaSituationMapper.findInstSituationReport", param ) ;
		handleHumpKey(mapList);
		return BeanUtil.convertMapList(mapList, clazz) ;
	}
	
	/**
	 * 教育局学校基本达成情况
	 * 
	 * @param param
	 * @param clazz
	 * @return
	 */
	public <T> List<T> findEduinstSchoolSituation( Map<String,Object> param , Class<T> clazz ){
		if( param.get("countyId") == null || param.get("reportingId")  == null )
			return null ;
		List<Map<String,Object>> dataList = sqlTemplate.selectList("InstQuotaSituationMapper.findEduinstSchoolSituation" , param ) ;
		return bean( dataList , clazz ) ;
	}
	
	/**
	 * 统计市教育局学校基本达成情况
	 * 
	 * @param param
	 * @param clazz
	 * @return
	 */
	public <T> List<T> findCityEduinstSchoolSituation(Map<String,Object> param , Class<T> clazz ){
		if( param.get("cityId") == null || param.get("reportingId")  == null )
			return null ;
		List<Map<String,Object>> dataList = sqlTemplate.selectList("InstQuotaSituationMapper.findCityEduinstSchoolSituation" , param ) ;
		return bean( dataList , clazz ) ;
	}
	
	/**
	 * 查询全省所有通过的学生名单
	 * 
	 * @param param
	 * @param clazz
	 * @return
	 */
	public <T> List<T> findAllPassSchool( Map<String,Object> param , Class<T> clazz ){
		List<Map<String,Object>> dataList = sqlTemplate.selectList("InstQuotaSituationMapper.findAllPassSchool" , param ) ;
		return bean( dataList , clazz ) ;
	}
	
}
