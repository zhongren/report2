package com.newproj.report.bulletin.dal.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.newproj.core.beans.BeanUtil;
import com.newproj.core.mybatis.AbstractBaseMapper;
import com.newproj.core.mybatis.Table;

@Repository
@Table("tb_bulletin")
public class BulletinDao extends AbstractBaseMapper{

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
	
	public <T> List<T> findBulletin( Map<String,Object> param , Class<T> clazz ){
		List<Map<String,Object>> dataList = sqlTemplate.selectList("BulletinMapper.findBulletin" , param ) ;
		if( dataList == null || dataList.isEmpty() )
			return null ;
		handleHumpKey(dataList);
		return BeanUtil.convertMapList(dataList, clazz) ;
	}

}
