package com.newproj.core.beans;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class MapParam<V,T> extends HashMap<V,T>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MapParam<V,T> push( V key , T value ){
		super.put(key, value) ;
		return this ;
	}
	
	public MapParam<V,T> push( Map<V,T> map ){
		if( map != null && !map.isEmpty() )
			putAll(map);
		return this ;
	}
	
	public int getInt( String key , int def ){
		try{
			return Integer.parseInt( get(key).toString() ) ;
		}catch(Exception e){
			return def ;
		}
	}
	
	public long getLong( String key , long def ){
		try{
			return Long.parseLong( get(key).toString() ) ;
		}catch(Exception e){
			return def ;
		}
	}
	
	public String getString( String key , String ... def ){
		Object value = get( key ) ;
		return value == null ? 
				def == null || def.length == 0 ? 
						null : def[0] : value.toString() ;
	}
	
	public BigDecimal getDecimal( String key , String ... def ){
		BigDecimal decimal = null ;
		try{
			String value = get( key ) == null ? def == null || 
					def.length == 0 ? null : def[0] : get(key).toString() ;
			decimal = new BigDecimal( value ) ;
		}catch(Exception e){
			
		}
		return decimal ;
	}
	
}
