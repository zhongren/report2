
package com.newproj.core.beans;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.newproj.core.util.AnnotationUtils;
 

public class BeanUtil extends BeanUtils {
	
	/**
	 * bean copy ... 
	 * 
	 * @param source
	 * @param desc
	 * @param sourceProperties
	 * @param descProperties
	 * @throws RuntimeException
	 */
	public static <T> T convert (
			Object source , Class<T> clazz , String [] sourceProperties , String [] descProperties )
					throws RuntimeException {
		if( source == null || clazz == null 
				|| sourceProperties == null || descProperties == null ){
			throw new IllegalArgumentException("bean copy is error , method arguments is not valid ... ") ;
		}
		if( sourceProperties.length != descProperties.length ){
			throw new IllegalArgumentException("source properties is not match desc properties .") ;
		}
		int loop = sourceProperties.length - 1 ;
		T desc = null ;
		try{
			desc = clazz.newInstance() ;
		}catch(Exception e){
			// Ignore ...
		};
		while( loop >= 0 ){
			if( isExistBeanFiled( source , sourceProperties[loop] , source.getClass() ) 
					&& isExistBeanFiled( desc , descProperties[loop] , desc.getClass() ) ) {
				try{
					String value = BeanUtils.getProperty(source, sourceProperties[loop]) ;
					if( value == null ){
						Reflection.setFieldValue( desc , sourceProperties[loop] , null );
					}else{
						BeanUtils.setProperty(desc, descProperties[loop], value);
					}
				}catch(Exception e){
					//Skip ...
				}
			}
			loop -- ;
		}
		return desc ;
	}
	
	
	/**
	 * 如果某个属性设置了默认值，目标对象中值为空则使用默认对象中指定的默认值.
	 * 
	 * @param source 		需要处理的目标对象
	 * @throws Exception	处理异常
	 */
	public static  <T> void setDefaultValue( T target , T defSource ){
		if( target == null || defSource == null ){
			return ;
		}
		Field [] fields = target.getClass().getDeclaredFields() ;
		for( Field field : fields ){
			try{
				if( getProperty( target , field.getName() ) != null || 
						getProperty( defSource , field.getName() ) == null ){
					continue ;
				}
				setProperty(  target , 
						field.getName() , getProperty(defSource, field.getName() ) );
			}catch(Exception e){
				//skip ...
			}
		}
	}
	
	/**
	 * field is exists ... 
	 * 
	 * @param bean
	 * @param fieldName
	 * @param clazz
	 * @return
	 */
	private static boolean isExistBeanFiled( Object bean , String fieldName , Class<?> clazz ){
		if( bean == null || fieldName == null || clazz == null ) return false ;
		Field [] fields = clazz.getDeclaredFields( ) ;
		for( Field field : fields ){
			if( field.getName().equals( fieldName ) ){
				return true ;
			}
		}
		return false ;
	}
	
	public static<T> T convert( Object source , Class<T> clazz ,  T def ){
		return convert( source , clazz , null , def ) ;
	}
	/**
	 * 对象属性复制时采用默认对象做空值复制 .
	 * 
	 * @param source		源对象
	 * @param desc			目标对象
	 * @param properties	字段
	 * @param def			默认值对象
	 */
	public static<T> T convert( Object source , Class<T> clazz , String [] properties , T def ){
		T desc = null ;
		if( properties == null || properties.length == 0 ){
			desc = convert( source , clazz  ) ;
		}else{
			desc = convert( source , clazz , properties , properties ) ;
		}
		if( def == null ){
			return desc;
		}
		Field [] defFields = def.getClass().getDeclaredFields() ;
		for( Field defField : defFields ){
			try{
				if( Reflection.getFieldValue( desc , defField.getName() ) == null ){
					Reflection.setFieldValue(
							desc, defField.getName() , Reflection.getFieldValue(def, defField.getName() ));
				}
			}catch(Exception e){
				//skip ...
			}
		}
		return desc ;
	}
	
	/**
	 * bean copy with equal bean ...
	 * 
	 * @param source
	 * @param desc
	 */
	public static <T> T convert( Object source , Class<T> clazz ){
		if( source == null )
			throw new RuntimeException("bean copy fail , source is null ..." ) ;
		Field [] fields = source.getClass().getDeclaredFields()  ;
		String [] properties = new String [ fields.length ] ;
		for( int i = 0 ; i < fields.length ; i ++ ){
			properties[ i ] = fields[ i ].getName() ;
		}
		return convert( source , clazz , properties , properties ) ; 
	}
	
	public static <T> List<T> convertList(List<?> sourceList , Class<T> clazz ) {
		if( sourceList == null || sourceList.isEmpty() || clazz == null ){
			return null ;
		}
		List<T> rsList = new ArrayList<T>() ;
		try{
			for( Object source : sourceList ){
				rsList.add( convert( source , clazz ) ) ;
			}
		}catch(Exception e){
			e.printStackTrace();
			return null ;
		}
		return rsList ;
	}
	
	/**
	 * deep copy object ... 
	 * 
	 * @param t
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T deepCopy( T t ){
		if( t == null ) return null ;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream() ;
		ObjectOutputStream objectOutputStream = null ;
		ByteArrayInputStream byteArrayInputStream  = null ;
		ObjectInputStream objectInputStream = null ;
		T temp = null ;
		try{
			objectOutputStream = new ObjectOutputStream( byteArrayOutputStream ) ;
			objectOutputStream.writeObject( t );
			byteArrayInputStream = new ByteArrayInputStream( byteArrayOutputStream.toByteArray() ) ;
			objectInputStream = new ObjectInputStream( byteArrayInputStream ) ;
			temp =  (T) objectInputStream.readObject() ;
		}catch(Exception e){
			e.printStackTrace();
		}
		return temp ;
	}
	
	public static <T> T convert( Object source, Class<T> clazz, String [] properties ){
		T desc = null;
		try {
			if( properties == null || properties.length == 0 ){
				desc = convert( source , clazz ) ;
			}else{
				desc = convert( source , clazz , properties , properties ) ;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return desc;
	}
	
	public static <T> List<T> convertMapList( List<Map<String,Object>> mapList , Class<T> clazz ){
		if( mapList == null || mapList.isEmpty() || clazz == null ){
			return null ;
		}
		List<T> beans = new ArrayList<T>() ;
		for( Map<String , Object> map : mapList ){
			T bean = convertMap( map , clazz ) ;
			if( bean == null ) continue ;
			beans.add( bean ) ;
		}
		return beans ;
	}
	
	public static <T> T convertMap( Map<String,Object> map , Class<T> clazz ){
		if( map == null || map.isEmpty() || clazz == null ){
			return null ;
		}
		T bean = null ;
		try{
			bean = clazz.newInstance() ;
			populate( bean , map );
		}catch(Exception e){
			//Ignore ;
		}
		return bean ;
	}
	
	public static Map<String,Object> getBeanMap( Object bean , boolean isTransient , String ... properties ){
		if( bean == null ) return null ;
		Field [] fields = bean.getClass().getDeclaredFields() ;
		List<String> excludes = Arrays.asList("class") ;
		List<String> includes = null ;
		if( properties != null && properties .length > 0 ){
			includes = Arrays.asList( properties ) ;
		}
		Map<String , Object> beanMap = new HashMap<String,Object>() ;
		for( Field field : fields ){
			if( isTransient && AnnotationUtils.isAnnotated( field , Transient.class ) ){
				continue ;
			}
			if( excludes.contains( field.getName() ) ){
				continue ;
			}
			if( includes == null || includes.isEmpty() ){
				beanMap.put( field.getName() , Reflection.getFieldValue( bean , field.getName() ) ) ;
				continue ;
			}
			if( includes.contains( field.getName() ) ){
				beanMap.put( field.getName() , Reflection.getFieldValue( bean , field.getName() ) ) ;
			}
		}
		return beanMap ;
	}
	
}


