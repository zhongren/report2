package com.newproj.report.context.trans;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.newproj.core.beans.BeanUtil;
import com.newproj.core.util.NumberUtil;
import com.newproj.report.sys.dto.City;
import com.newproj.report.sys.dto.Dict;
import com.newproj.report.sys.dto.User;
import com.newproj.report.sys.service.CityService;
import com.newproj.report.sys.service.DictService;
import com.newproj.report.sys.service.UserService;
import org.springframework.util.CollectionUtils;

@Component
public class TransContext {
	private static final Map<String,Dict> CACHED_DICT = new HashMap<String,Dict>() ;
	private static final Map<Integer,User> CACHED_USER = new HashMap<Integer,User>();
	private static final Map<Integer,City> CACHED_CITY = new HashMap<Integer,City>();
	private static final Map<String,List<Dict>> CACHED_DICT_BASE = new HashMap<String,List<Dict>>() ;
	@Autowired
	private UserService userService ;
	@Autowired
	private DictService dictService ;
	@Autowired
	private CityService cityService ;

	private static long lastCachedTimestamp = 0l ;
	private static long expireTimestamp = 10000l ;//30 * 60 * 1000l ;

	public  void refreshDict(){
		List<Dict> dataList = dictService.findList( null , null , Dict.class , "type","name","code") ;
		if( dataList == null || dataList.isEmpty() )
			return ;
		CACHED_DICT.clear();
		CACHED_DICT_BASE.clear();
		for( Dict data : dataList ){
			CACHED_DICT.put(String.format("%s-%s", data.getType() , data.getCode() ) , data ) ;
			if(!CACHED_DICT_BASE.containsKey(data.getType())){
				CACHED_DICT_BASE.put(data.getType(),new ArrayList<>());
			}
			CACHED_DICT_BASE.get(data.getType()).add(data);
		}
	}

	public  void refreshUser(){
		List<User> dataList = userService.findList( null , null , User.class , "id","realName") ;
		if( dataList == null || dataList.isEmpty() )
			return ;
		CACHED_USER.clear();
		for( User data : dataList ){
			CACHED_USER.put( data.getId() , data ) ;
		}
	}

	public void refreshCity(){
		List<City> dataList = cityService.findList( null , null , City.class , "id","name") ;
		if( dataList == null || dataList.isEmpty() )
			return ;
		CACHED_CITY.clear();
		for( City data : dataList ){
			CACHED_CITY.put( data.getId() , data ) ;
		}
	}

	public  void refresh(){
		lastCachedTimestamp = new Date().getTime() + expireTimestamp ;
		refreshDict() ;
		refreshUser() ;
		refreshCity() ;
	}

	public TransContext transDict( String type , List<?> dataList , String keyField , String nameField , String ... defKey ){
		if( lastCachedTimestamp < new Date().getTime() )
			refresh() ;

		if( dataList == null || dataList.isEmpty() )
			return this ;
		try{
			for( Object data : dataList ){
				String value = BeanUtil.getProperty( data , keyField ) ;
				if( value == null && defKey != null && defKey.length != 0)
					value = defKey[0] ;
				Dict dict = CACHED_DICT.get(String.format("%s-%s" , type ,  value ) ) ;
				if( dict == null )
					continue ;
				trans( data  , nameField , dict , "name" ) ;
			}
		}catch(Exception e){
			//Ignore ...
		}
		return this ;

	}

	private void trans( Object data , String dataNameField ,
			Object source , String sourceNameField ){
		try {
			BeanUtil.setProperty(data, dataNameField , BeanUtil.getProperty(source, sourceNameField ) );
		} catch (IllegalAccessException | InvocationTargetException
				| NoSuchMethodException e) {
			//Ignore field .
		}
	}

	public TransContext transUser(  List<?> dataList , List<String> keyFields , List<String> nameFields ){
		if( lastCachedTimestamp < new Date().getTime() )
			refresh() ;

		if( dataList == null || dataList.isEmpty() )
			return this ;
		try{
		for( Object data : dataList ){
			for( int i = 0 ; i < keyFields.size() ; i ++ ){
				User user = CACHED_USER.get( NumberUtil.parseInt(  BeanUtil.getProperty( data , keyFields.get(i) ) , 0 ) ) ;
				if( user == null )
					continue ;
				trans( data ,nameFields.get( i ) , user , "realName" ) ;
			}
		}
		}catch(Exception e){
			//Ignore ...
		}
		return this ;
	}

	public TransContext transCity(  List<?> dataList , List<String> keyFields , List<String> nameFields ){
		if( lastCachedTimestamp < new Date().getTime() )
			refresh() ;

		if( dataList == null || dataList.isEmpty() )
			return this ;
		try{
			for( Object data : dataList ){
				for( int i = 0 ; i < keyFields.size() ; i ++ ){
					City city = CACHED_CITY.get(NumberUtil.parseInt(  BeanUtil.getProperty( data , keyFields.get(i) ) , 0 ) ) ;
					if( city == null )
						continue ;
					trans( data ,nameFields.get( i ) , city , "name" ) ;
				}
			}
		}catch(Exception e){
			//Ignore ...
		}
		return this ;
	}

	public String getCode(String name,String type){
		String code="";
		if(CACHED_DICT_BASE.containsKey(type)){
			List<Dict> list=CACHED_DICT_BASE.get(type);
			if(!CollectionUtils.isEmpty(list)){
				for(Dict dict:list){
					if(dict.getName().equals(name)){
						code=dict.getCode();
					}
					break;
				}
			}
		}
        return code;
	}
}
