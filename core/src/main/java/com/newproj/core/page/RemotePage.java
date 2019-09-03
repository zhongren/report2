package com.newproj.core.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.github.pagehelper.Page;

public class RemotePage<T> extends ArrayList<T> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public RemotePage(){}
	public RemotePage( List<T> data , Page<?> pageInfo ){
		if( data != null ) {
			addAll( data ) ;
		}
		setPage( pageInfo ) ;
	} 
	
	public void setPage( Page<?> pageInfo ){
		if( pageInfo == null ) {
			return ;
		}
		setPageNum( pageInfo.getPageNum() ) ;
		setPageSize( pageInfo.getPageSize() ) ;
		setTotal( pageInfo.getTotal() ) ;
	}
	
	/**
     * 页码，从1开始
     */
    private int pageNum;
    /**
     * 页面大小
     */
    private int pageSize;
    /**
     * 总数
     */
    private long total;
    /**
     * 总页数
     */
    private int pages;
	
	public List<T> getData() {
		return this;
	}
	public void setData(List<T> data) {
		//统一空值数据
		if( data == null ) return ; 
		addAll( data ) ;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		//合理化当前页
		if( pageNum <= 0 ){
			pageNum = 1 ;
		}
		this.pageNum = pageNum;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		//合理化每页数量
		if( pageSize <= 0 ){
			pageSize = 10 ;
		}
		this.pageSize = pageSize;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal( long total ){
		this.total = total;
	    if (total <= 0 ) {
	        pages = 1;
	        return;
	    }
    	pages = (int) (total / pageSize + ((total % pageSize == 0) ? 0 : 1));
	}
	public int getPages() {
		
		return pages;
	}
	
}
