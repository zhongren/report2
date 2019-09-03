package com.newproj.core.mybatis;

public class UpdateValue {
	private String column ;
	private Object value ;
	private String jdbcType ;
	private Object defaultVal ;
	private boolean isNull ;
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public Object getDefaultVal() {
		return defaultVal;
	}
	public String getJdbcType() {
		return jdbcType;
	}
	public void setJdbcType(String jdbcType) {
		this.jdbcType = jdbcType;
	}
	public void setDefaultVal(Object defaultVal) {
		this.defaultVal = defaultVal;
	}
	public boolean isNull() {
		return isNull;
	}
	public void setNull(boolean isNull) {
		this.isNull = isNull;
	}
}
