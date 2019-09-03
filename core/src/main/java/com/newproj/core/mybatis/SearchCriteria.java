package com.newproj.core.mybatis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchCriteria {
	private String tableName ;
	private List<String> columns ;
	private List<Condition> conditions ;
	
	public void addConditions( Condition ... conditions ){
		if( conditions == null || conditions.length == 0 ){
			return ;
		}
		if( this.conditions == null ){
			this.conditions = new ArrayList<Condition>() ;
		}
		this.conditions.addAll( Arrays.asList( conditions ) ) ;
	}
	
	public void addColumns( String ... columns ){
		if( columns == null || columns.length == 0 ){
			return ;
		}
		if( this.columns == null ){
			this.columns = new ArrayList<String>() ;
		}
		this.columns.addAll( Arrays.asList( columns ) ) ;
	}
	public SearchCriteria( String tableName , List<String> columns ){
		this.tableName = tableName ;
	}
	
	public SearchCriteria( String tableName ){
		this( tableName , null ) ;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	public List<Condition> getConditions() {
		return conditions;
	}

	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}
	

}
