package com.newproj.report.sys.dal.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.newproj.core.beans.BeanUtil;
import com.newproj.core.beans.MapParam;
import com.newproj.core.mybatis.AbstractBaseMapper;
import com.newproj.core.mybatis.Table;
@Repository
@Table("tb_sys_menu")
public class SysMenuDao extends AbstractBaseMapper{

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
	
	public <T> List<T> findRoleMenus( Integer roleId , Class<T> clazz ){
		List<Map<String,Object>> dataList = sqlTemplate.selectList("SysMenuMapper.findRoleMenus",
				new MapParam<String,Object>().push("roleId", roleId ) ) ;
		if( dataList == null || dataList.isEmpty() ){
			return null ;
		}
		handleHumpKey(dataList);
		return BeanUtil.convertMapList(dataList, clazz) ;
	}

}
