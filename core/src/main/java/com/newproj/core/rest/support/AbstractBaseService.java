package com.newproj.core.rest.support;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.pagehelper.Page;
import com.newproj.core.beans.BeanUtil;
import com.newproj.core.exception.BusinessException;
import com.newproj.core.mybatis.AbstractBaseMapper;
import com.newproj.core.page.PageParam;
import com.newproj.core.page.PageUtil;
import com.newproj.core.page.RemotePage;

/**
 * @author 10147
 * Base abstract service .
 * To do some common service .
 *
 */
public abstract class AbstractBaseService {
	
	protected Logger logger = LoggerFactory.getLogger( getClass() ) ;
	
	private AbstractBaseMapper mapper ;
	
	/**
	 * Initialization after construction .
	 * You can do some configuration . Add condition mapping .
	 */
	@PostConstruct
	public abstract void init() ;
	
	protected AbstractBaseService setMapper( AbstractBaseMapper mapper ){
		this.mapper = mapper ;
		return this ;
	}
	
	protected AbstractBaseService withMapper( AbstractBaseMapper mapper ){
		return new AbstractBaseService(){
			public void init(){
				
			}
		}.setMapper(mapper);
	}
	
	/**
	 * Find data list include condition , page , searching fields .
	 * Must configure condition mapping , include field , symbol , value .
	 * 
	 * @param param			Search condition .
	 * @param pageParam		Page parameter .
	 * @param properties	Search fields .
	 * @return				Page data .
	 */
	public <T> RemotePage<T> findList( 
			Map<String,Object> params , PageParam pageParam , Class<T> clazz , String ... properties ){
		Page<?> page = PageUtil.startPage(pageParam) ;
		List<Map<String,Object>> dataList = mapper.findMapList( params , properties ) ;
		if( dataList == null || dataList.isEmpty() ){
			return null ;
		}
		return new RemotePage<T>( BeanUtil.convertMapList( dataList, clazz) , page ) ;
	}
	
	public <T> T findOne( Map<String,Object> params , Class<T> clazz , String ... properties ){
		PageParam page = new PageParam( 1 , 1 , false ) ;
		List<T> dataList = findList( params , page , clazz , properties ) ;
		return dataList == null || dataList.isEmpty() ? null : dataList.get( 0 ) ;
	}
	
	/**
	 * Find object by a field .
	 * 
	 * @param by			Entity java field .
	 * @param value			By value .
	 * @param clazz			Return type .
	 * @param properties	Select fields .
	 * @return				Object bean .
	 */
	public <T> T findBy( String by , Object value , Class<T> clazz , String ... properties ){
		return mapper.findBeanBy(by, value, clazz, properties) ;
	}
	
	public <T> List<T> findListBy(  String by , Object value , Class<T> clazz , String ... properties ){
		return mapper.findBeanListBy(by, value, clazz, properties) ;
	}
	
	
	public <T> Object create( T bean , Class<T> clazz , String ... properties ){
		return mapper.createBean(bean, clazz, properties);
	}
	
	public Object create( Map<String,Object> params ){
		return mapper.createMap(params) ;
	}
	
	public int update( String by , Object value , Map<String,Object> params ){
		if( mapper.findMapBy( by , value, by) == null ){
			throw new BusinessException("记录不存在，更新失败!") ;
		}
		return mapper.updateMap(by, value, params) ;
	}
	
	public <T> int update( String by , Object value , T bean , String ... properties ){
		if( mapper.findMapBy( by , value, by) == null ){
			throw new BusinessException("记录不存在，更新失败!") ;
		}
		return mapper.updateBean(by, value, bean, properties);
	}
	
	public int delete( Map<String,Object> param ){
		return mapper.delete( param ) ;
	}
}
