package com.newproj.core.mybatis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UpdateCriteria implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Object id ;
	private String tableName ;
	private List<Condition> conditions ;
	private List<UpdateValue> updateValues ;
	
	public void addUpdateValue( UpdateValue updateValue ){
		if( updateValue == null ) {
			return ;
		}
		if( updateValues == null ){
			updateValues = new ArrayList< UpdateValue >() ;
		}
		updateValues.add( updateValue ) ;
	}
	public void addCondition( Condition ... conditions ){
		if( conditions == null || conditions .length == 0 ){
			return ;
		}
		if( this.conditions == null ){
			this.conditions = new ArrayList<Condition> () ;
		}
		for( Condition condition : conditions ){
			this.conditions.add( condition ) ;
		}
	}
	public UpdateCriteria( String tableName ){
		this.tableName = tableName ;
	}
	public Object getId() {
		return id;
	}
	public void setId(Object id) {
		this.id = id;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public List<Condition> getConditions() {
		return conditions;
	}
	public void setConditions(List<Condition> conditions) {
		if( conditions == null || conditions.isEmpty() ){
			return ;
		}
		if( this.conditions == null ){
			this.conditions = new ArrayList<Condition>() ;
		}
		this.conditions.addAll( conditions ) ;
	}
	public List<UpdateValue> getUpdateValues() {
		return updateValues;
	}
	public void setUpdateValues(List<UpdateValue> updateValues) {
		this.updateValues = updateValues;
	}
	
}
