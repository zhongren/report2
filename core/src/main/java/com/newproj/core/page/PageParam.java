package com.newproj.core.page;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.util.StringUtils;

import com.github.pagehelper.StringUtil;
 
public class PageParam {
	private int pageNum = 1 ;
	private int pageSize = 10 ;
	private boolean isCount  = false;
	private Map<String,ORDER_TYPE> orderMap = new LinkedHashMap<String, ORDER_TYPE>() ;
	public enum ORDER_TYPE{
		DESC("DESC") ,
		ASC("ASC") ;
		private String type ;
		ORDER_TYPE( String type ){
			this.type = type ;
		}
		public static ORDER_TYPE parse( String type ){
			ORDER_TYPE [] ntypes = ORDER_TYPE.values() ;
			for( ORDER_TYPE ntype : ntypes ){
				if( ntype.type.equalsIgnoreCase( type ) ){
					return ntype ;
				}
			}
			return null ;
		}
		public String getType(){
			return this.type ;
		}
	}
	
	public PageParam(){
		this( 0 , 0 , false ) ;
	}
	public PageParam( int pageNum , int pageSize , boolean isCount ){
		setPageNum( pageNum ); 
		setPageSize( pageSize ) ;
		this.isCount = isCount ;
	}
	
	public PageParam orderBy( String field , String type ){
		if( StringUtil.isEmpty( field ) ){
			return this ;
		}
		orderMap.put(field, ORDER_TYPE.parse( type ) ) ;
		return this ;
	}
	
	public PageParam orderBy( Object field , Object type ){
		if( field == null || type == null ){
			return this;
		}
		return orderBy( field.toString() , type.toString() ) ;
	}
	
	public String orderBy(){
		if( orderMap.isEmpty() ){
			return null ;
		}
		StringBuffer stringOrderType = new StringBuffer() ;
		for( Entry<String,ORDER_TYPE> item : orderMap.entrySet() ){
			if( StringUtils.isEmpty( item.getKey() ) || item.getValue() == null ){
				continue ;
			}
			stringOrderType.append( String.format(
					" %s %s ,", prop2Colmn( item.getKey() ) , item.getValue().getType() ) ) ;
		}
		String tempstr = stringOrderType.toString().trim() ;
		if( tempstr.endsWith( "," ) ){
			tempstr = tempstr.substring( 0 , tempstr.length() - 1 ) ;
		}
		return tempstr ;
	}
	
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		if( pageNum < 0 ){
			return ;
		}
		this.pageNum = pageNum;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		if( pageSize < 0 ){
			return ;
		}
		this.pageSize = pageSize;
	}
	public boolean isCount() {
		return isCount;
	}
	public void setCount(boolean isCount) {
		this.isCount = isCount;
	}
	
	private String prop2Colmn( String property ){
		if( StringUtil.isEmpty( property ) ) {
			return null ;
		}
		String column = "".trim() ;
		for( int i = 0 ; i < property.length() ; i ++ ){
			char c = property.charAt( i ) ;
			if( !Character.isUpperCase( c ) ){
				column += c+"".trim() ;
				continue ;
			}
			column += "_"+( c + "".trim() ).toLowerCase() ;
		}
		return column ;
	}
	
}
