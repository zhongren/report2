package com.newproj.core.page;

import org.springframework.util.StringUtils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

public class PageUtil {

	public  static <T> Page<T> startPage( PageParam pageParam ){
		if( pageParam == null  ){
			return null ;
		}
		Page<T> page = null ;
		if( pageParam.getPageNum() > 0 ){
			page = PageHelper.startPage(
					pageParam.getPageNum() , pageParam.getPageSize() , pageParam.isCount() ) ;
		}
		if( !StringUtils.isEmpty( pageParam.orderBy() ) ){
			PageHelper.orderBy( pageParam.orderBy() );
		}
		return page ;
	}
}
