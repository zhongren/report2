package com.newproj.core.json;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.newproj.core.util.ClassUtil;

class JsonMapper extends ObjectMapper {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider();

	public JsonMapper(String[] properties, boolean serialization , Class<?> clazz , boolean excludeNullValues, boolean ignoreUnknown,
			boolean convertNullValueProperties) {
		if (ignoreUnknown) {
			ignoreUnknownProperties();
		}
		if (excludeNullValues) {
			excludeNullValues();
		}
		if (convertNullValueProperties) {
			convertNullValueProperties();
		}
		filterSerializationProperties(properties, serialization , clazz );
	}

	public JsonMapper() {
		this(null, false, null , false, true, false);
	}

	public JsonMapper(String[] properties, boolean serialization) {
		this(properties, serialization, null , false, true, false);
	}

	public JsonMapper filterSerializationProperties(String[] properties, boolean serialization , Class<?> clazz ) {
		if (properties == null || properties.length == 0) {
			return this;
		}
		SimpleBeanPropertyFilter filter = serialization ? SimpleBeanPropertyFilter.filterOutAllExcept(properties)
				: SimpleBeanPropertyFilter.serializeAllExcept(properties);
		FilterProvider filterProvider = null ;
		JsonFilter jsonFilter = clazz.getAnnotation(JsonFilter.class) ;
		if( jsonFilter == null ){
			String random = random() ;
			filterProvider = simpleFilterProvider.addFilter( random , filter);
			addMixIn( clazz , ClassUtil.annotate("JsonFilter"+random, JsonFilter.class , random  ) ) ;
		}else{
			filterProvider = simpleFilterProvider.addFilter(jsonFilter.value(), filter);
			addMixIn( clazz , clazz ) ;
		}
		this.setFilterProvider(filterProvider) ;
		return this;
	}
	
	private String random(){
		return Math.round( Math.random() * 99999999 )+"" ;
	}

	public JsonMapper ignoreUnknownProperties() {
		this.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		return this; 
	}

	public JsonMapper convertNullValueProperties() {
		JsonSerializer<Object> jsser = new JsonSerializer<Object>() {
			public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
					throws IOException, JsonProcessingException {
				if( value instanceof Float || value instanceof Double || 
						value instanceof BigDecimal ){
					gen.writeString("0.00");
				}else if( value instanceof Integer || value instanceof Byte ){
					gen.writeNumber(0);
				}else if( value instanceof Boolean ){
					gen.writeBoolean( false );
				}else if( value instanceof String  ){
					gen.writeString("");
				}else if( value instanceof List ){
					gen.writeString("[]");
				}else if( value instanceof Map ){
					gen.writeString("{}");
				}else{
					gen.writeString("");
				}
			}
		};
		this.getSerializerProvider().setNullValueSerializer(jsser);
		return this;
	}

	public JsonMapper excludeNullValues() {
		this.disable(SerializationFeature.WRITE_NULL_MAP_VALUES);
		this.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return this;
	}
}
