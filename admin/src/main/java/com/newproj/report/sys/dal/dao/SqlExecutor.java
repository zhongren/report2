package com.newproj.report.sys.dal.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.newproj.core.exception.BusinessException;

@Repository
public class SqlExecutor {

	private static SqlSessionTemplate sessionTemplate ;
	
	@Autowired
	public  SqlExecutor( SqlSessionTemplate sessionTemplate ){
		SqlExecutor.sessionTemplate = sessionTemplate ;
	}
	
	public static List<Map<String,Object>> selectList( String sql , Map<String,Object> param ){
		if( StringUtils.isEmpty( sql ) )
			throw new BusinessException("查询语句不能为空!") ;
		if( !Pattern.matches("^select.*", sql.trim().toLowerCase() ) )
			throw new BusinessException("仅支持查询语句!") ;
		
		if( param == null )
			param = new HashMap<String,Object>() ;
		param.put("sql", sql) ;
		return sessionTemplate.selectList("sqlExecute", param );
	}
	
	public static Map<String,Object> selectOne( String sql , Map<String,Object> param ){
		List<Map<String,Object>> dataList = selectList( sql , param ) ;
		if( dataList == null || dataList.isEmpty() )
			return null ;
		return dataList.get(0) ;
	}
	
	public static Object get( String sql , Map<String,Object> param , String ... column ){
		Map<String,Object> dataMap = selectOne( sql , param ) ;
		if( dataMap == null || dataMap.isEmpty() )
			return null ;
		if( column == null || column.length == 0 ){
			Set<String> keys = dataMap.keySet() ;
			Object value = dataMap ;
			if( keys.size() == 1 ) {
				value = dataMap.get( keys.toArray(new String[]{})[0] ) ; 
				if( value != null )
					value = value.toString() ;
			}
			return value ;
		}else if( column.length == 1 ){
			Object value = dataMap.get(column[0]) ;
			return value == null ? null : value.toString() ;
		}
		Map<String,String> result = new HashMap<String,String>() ;
		for( String item : column ){
			Object value = dataMap.get( column ) ;
			result.put( item ,  value == null ? null : value.toString() );
		}
		return result ;
	}
	
	public static Object get( String sql ){
		return get( sql , null );
	}
	
}
