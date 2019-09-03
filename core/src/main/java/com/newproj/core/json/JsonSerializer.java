package com.newproj.core.json;



import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;

public class JsonSerializer {
	private JsonMapper jsonMapper = new JsonMapper() ;
	public JsonSerializer(){
		
	}
	public  String toJson( Object object ){
		if( object == null ){
			object = new Object() ;
		}
		String json = null ;
		try {
			json = jsonMapper.writeValueAsString( object ) ;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json ;
	}
	public <T> List<T> jsonToList( String json ,Class<T> clazz ){
		try{
			JavaType valueType = jsonMapper
					.getTypeFactory().constructParametricType( List.class , clazz ) ;
			return jsonMapper.readValue( json , valueType ) ;
		}catch(Exception e){
			//Ignore error 
		}
		return null ;
	}
	
	public <T> JsonSerializer exclude( Class<T> clazz ,  String ... properties ){
		jsonMapper.filterSerializationProperties( properties , false , clazz ) ;
		return this ;
	}
	public <T> JsonSerializer include( Class<T> clazz , String ... properties ){
		jsonMapper.filterSerializationProperties( properties , true , clazz ) ;
		return this ;
	}
	public JsonSerializer filterNullValues(){
		jsonMapper.excludeNullValues() ;
		return this ;
	}
	public JsonSerializer convertNullValuesToEmpty(){
		jsonMapper.convertNullValueProperties() ;
		return this ;
	}
	public <T>  T fromJson( String json , Class<T> clazz ){
		if(json == null || clazz == null ){
			return null ;
		}
		T obj = null ;
		try {
			obj = jsonMapper.readValue(json, clazz ) ;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return obj ;
	}
	
	public <T> Map<String,T> jsonToMap( String json , Class<T> clazz ){
		try{
			JavaType valueType = jsonMapper.getTypeFactory()
					.constructParametricType( Map.class , String.class , clazz ) ;
			return jsonMapper.readValue( json , valueType );
		}catch(Exception e){
			//Ignore error .
		}
		return null ;
	}
	
	public <T> List<Map<String,T>> jsonToMapList( String json , Class<T> clazz ){
		TypeReference<List<Map<String,T>>> valueType = new TypeReference<List<Map<String,T>>>(){};
		try{
			return jsonMapper.readValue( json  , valueType ) ;
		}catch(Exception e){
			//Ignore error .
		}
		return null ;
	}
}
