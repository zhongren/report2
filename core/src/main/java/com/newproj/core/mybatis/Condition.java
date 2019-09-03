package com.newproj.core.mybatis;

import java.io.Serializable;

public class Condition implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String column ;
	private Object value ;
	private SYMBOL symbol ;
	private String jdbcType = "VARCHAR";
	public Condition(){
		
	}
	public Condition( String column , SYMBOL symbol ){
		this.column = column ;
		this.symbol = symbol ;
	}
	public Condition( String column , Object value , SYMBOL symbol ){
		this.column = column ;
		this.value = value ;
		this.symbol = symbol ;
	}
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public Object getValue() {
		return value;
	}
	public String getJdbcType() {
		return jdbcType;
	}
	public void setJdbcType(String jdbcType) {
		this.jdbcType = jdbcType;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public String getSymbol() {
		return symbol.getSymbol();
	}
	public void setSymbol(SYMBOL symbol) {
		this.symbol = symbol;
	}

	public enum SYMBOL{
		EQ("=") ,
		IS_NULL("IS") ,
		LIKE("LIKE") ,
		NOT_EQ("!="),
		LT("<") ,
		ELT("<=") ,
		GT(">") ,
		EGT(">=") ;
		
		private String symbol ;
		SYMBOL( String symbol ){
			this.symbol = symbol ;
		}
		public String getSymbol() {
			return symbol;
		}
		
		public static SYMBOL parse( String symbol ){
			SYMBOL [] symbols = SYMBOL.values() ;
			for( SYMBOL s : symbols ){
				if( symbol.trim().equalsIgnoreCase( s.getSymbol() ) ){
					return s ;
				}
			}
			return null ;
		}
		
	}
}
