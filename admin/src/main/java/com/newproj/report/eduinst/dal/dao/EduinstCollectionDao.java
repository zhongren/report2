package com.newproj.report.eduinst.dal.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.newproj.core.beans.BeanUtil;
import com.newproj.core.mybatis.AbstractBaseMapper;
import com.newproj.core.mybatis.Table;
import com.newproj.report.export.bean.ClssSizeBean;

@Repository
@Table("tb_eduinst_collection")
public class EduinstCollectionDao extends AbstractBaseMapper{

	@Override
	public void init() {
		// TODO Auto-generated method stub
	}
	
	public <T> List<T> findEduinstCollection( Integer quotaId ,int eduinstId , String eduinstType , int reportId , Class<T> clazz ){
		Map<String,Object> param = new HashMap<String,Object>() ;
		param.put("reportId", reportId );
		param.put("eduinstType", eduinstType ) ;
		param.put("eduinstId", eduinstId ) ;
		param.put("quotaId", quotaId ) ;
		List<Map<String,Object>> dataList = sqlTemplate.selectList("EduinstCollectionMapper.findEduinstCollection" , param ) ;
		if( dataList == null || dataList.isEmpty() )
			return null ;
		handleHumpKey( dataList );
		return BeanUtil.convertMapList( dataList, clazz) ;
	}
	
	public List<Map<Integer,Integer>>  findEduinstIsEnd( int eduinstId , String eduinstType , int reportId  ){
		Map<String,Object> param = new HashMap<String,Object>() ;
		param.put("reportId", reportId );
		param.put("eduinstType", eduinstType ) ;
		param.put("eduinstId", eduinstId ) ;
		List<Map<Integer,Integer>> dataList = sqlTemplate.selectList("EduinstCollectionMapper.findEduinstIsEnd" , param ) ;
		return dataList;
	}
	
	public List<Map<String, Object>> findEduCollection(int reportId){
		Map<String,Object> param = new HashMap<>();
		param.put("reportId", reportId);
		List<Map<String, Object>> dataList = sqlTemplate.selectList("EduinstCollectionMapper.findEduCollection" , param ) ;
		return dataList;
	}
	
	public List<String> finaEduAllCollection(){
		List<String> dataList = sqlTemplate.selectList("EduinstCollectionMapper.finAllCollections" , null ) ;
		return dataList;
	}
	
	public List<ClssSizeBean> findPrimaryClassSize(){
		List<ClssSizeBean> dataList = sqlTemplate.selectList("EduinstCollectionMapper.findPrimaryClssSize" , null ) ;
		return dataList;
	}
	
	public List<ClssSizeBean> findMiddleClassSize(){
		List<ClssSizeBean> dataList = sqlTemplate.selectList("EduinstCollectionMapper.findMiddleClssSize" , null ) ;
		return dataList;
	}
}
