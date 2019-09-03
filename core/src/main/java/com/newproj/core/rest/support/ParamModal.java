package com.newproj.core.rest.support;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.newproj.core.page.PageParam;
import com.newproj.core.util.NumberUtil;

public class ParamModal {

	private Integer pageNum = 0 ;
	private Integer pageSize = 0 ;
	private String sortField;
	private String sortType;
	
	private Map<String,Object> searchParam = new HashMap<String,Object>() ;

	public PageParam getPageParam() {
		PageParam pageParam = new PageParam(pageNum, pageSize, true);
		pageParam.orderBy(sortField, sortType);
		return pageParam;
	}
	
	public Object put( String key , Object value ){
		if( key == null || StringUtils.isEmpty( key ) || StringUtils.isEmpty( value ) ){
			return null ;
		}
		if( key.trim().equalsIgnoreCase("page") ){
			pageNum = NumberUtil.parseInt( value , 1 ) ;
			return pageNum ;
		}else if( key.trim().equalsIgnoreCase("size") ){
			pageSize = NumberUtil.parseInt( value , 20 ) ;
			return pageSize ;
		}else if( key.trim().equalsIgnoreCase("orderField") && value != null ){
			sortField = value.toString() ;
			return sortField;
		}else if( key.trim().equalsIgnoreCase("orderType") && value != null ){
			sortType = value.toString() ;
			return sortType;
		}
		return searchParam.put(key, value) ;
	}
	
	public boolean hasValue( String ... keys ){
		if( keys == null || keys.length == 0 ){
			return !searchParam.isEmpty() ;
		}
		for( String key : keys ){
			if( !searchParam.containsKey( key ) ){
				return false ;
			}
		}
		return true ;
	}
	
	public Map<String,Object> getParam( String ... keys ){
		if( keys == null || keys.length == 0 ){
			return searchParam ;
		}
		Map<String,Object> tempParam = new HashMap<String,Object>() ;
		for( String key : keys ){
			String pkey = key ;
			if( key.startsWith("?") )
				pkey = pkey.substring(1) ;
			
			if( !searchParam.containsKey( pkey ) ){
				continue ;
			}
			tempParam.put(key, searchParam.get( pkey ) ) ;
		}
		return tempParam ;
	}
	
	public void remove( String ... keys ){
		if( keys == null || keys.length == 0 ){
			return ;
		}
		for( String key : keys ){
			if( searchParam.containsKey( key ) ){
				searchParam.remove( key ) ;
			}
		}
	}
	
	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}
}
