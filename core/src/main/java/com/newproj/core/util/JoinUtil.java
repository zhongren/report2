package com.newproj.core.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;


public class JoinUtil {
	public static List<String> fieldsValue( List<?> source , String ... fieldNames  ){
		if( source == null || source.isEmpty()|| fieldNames == null || fieldNames.length == 0 ){
			return null ;
		}
		List<String> list = new ArrayList<String> () ;
		try{
			for( Object item : source ){
				for( String fieldName : fieldNames ){
					String tmp = BeanUtils.getProperty( item , fieldName )  ;
					if( tmp == null ) continue ;
					list.add( tmp ) ;
				}
			}
		}catch(Exception e){
			e.printStackTrace( );
		}
		return list ;
	}
	
	public static void join( 
			List<?> targets , String [] targetFields , String tfk ,
			List<?> source , String [] sourceFields  , String sfk ){
		if( targets == null || targets.isEmpty() || source == null || source.isEmpty() ){
			return ;
		}
		for( Object target : targets ){
			join( target ,targetFields , tfk , source , sourceFields , sfk  ) ;
		}
	}
	
	public static void join( 
			Object target , String [] targetFields , String tfk , List<?> sources , String [] sourceFields , String sfk ){
		if( target == null || targetFields == null || targetFields.length == 0 ||
				sources == null || sources.isEmpty() ||sourceFields == null || 
				sourceFields.length == 0 || sfk == null || tfk == null  ){
			return ;
		}
		for( Object source : sources ){
			try{
				String sfkValue = BeanUtils.getProperty( source , sfk ) ;
				String tfkValue = BeanUtils.getProperty( target , tfk ) ;
				if( sfkValue == null || tfkValue == null || !sfkValue.equals( tfkValue ) ){
					continue ;
				}
			}catch(Exception e){
				continue ;
			}
			int index = 0;
			for( String targetField : targetFields ){
				try{
					BeanUtils.setProperty(target, targetField, 
							BeanUtils.getProperty( source ,  sourceFields[index ++ ] ) ) ;
				}catch(Exception e){
					//Ignore error ...
				}
			}
			break ;
		}
	}
}
