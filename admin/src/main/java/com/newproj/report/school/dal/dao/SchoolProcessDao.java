package com.newproj.report.school.dal.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.newproj.core.beans.BeanUtil;
import com.newproj.core.mybatis.AbstractBaseMapper;
import com.newproj.core.mybatis.Table;

@Repository
@Table("tb_school_process")
public class SchoolProcessDao extends AbstractBaseMapper{

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
	
	public List<Map<Integer, Integer>> findCollections(int sid , int reportId , String schoolType ){
		Map<String, Object> param = new HashMap<>();
		param.put("school_id", sid);
		param.put("report_id", reportId);
		param.put("school_type", schoolType);
		List<Map<Integer, Integer>>  maps = sqlTemplate.selectList("SchoolProcessMapper.findAllCollectionByType" , param ) ;
		return maps;
	}
	

}
