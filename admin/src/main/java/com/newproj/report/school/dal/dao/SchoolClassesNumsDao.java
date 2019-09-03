package com.newproj.report.school.dal.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.newproj.core.mybatis.AbstractBaseMapper;
import com.newproj.core.mybatis.Table;

@Repository
@Table("tb_school_class")
public class SchoolClassesNumsDao extends AbstractBaseMapper{
	
	
	public int getClassesNums(Map<String, Object> params) {
		return sqlTemplate.selectOne("SchoolClassesMapper.getClassesNums",params);
	}

	public int getSchoolNums(Map<String, Object> params) {
		return sqlTemplate.selectOne("SchoolClassesMapper.getSchoolNums",params);
	}
	
	public int getNumsMax(Map<String, Object> parmas)  {
		return  sqlTemplate.selectOne("SchoolClassesMapper.getNumsMax", parmas);
	}
	
	public int getAllClassesNums(Map<String, Object> parmas)  {
		return  sqlTemplate.selectOne("SchoolClassesMapper.getAllClassesNums", parmas);
	}
	public int getAllSchoolNums(Map<String, Object> parmas)  {
		return  sqlTemplate.selectOne("SchoolClassesMapper.getAllSchoolNums", parmas);
	}
	
	public int getClassSize(Map<String, Object> parmas)  {
		return  sqlTemplate.selectOne("SchoolClassesMapper.getClassSize", parmas);
	}
	
	
	
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

}
