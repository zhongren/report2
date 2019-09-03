package com.newproj.core.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.newproj.core.beans.BeanUtil;
import com.newproj.core.mybatis.Condition.SYMBOL;
import com.newproj.core.util.StringUtil;

public abstract class AbstractBaseMapper extends AbstractConditionHandler {

	@Autowired
	protected SqlSessionTemplate sqlTemplate ;
	
	public List<Map<String,Object>> findMapList( Map<String , Object> params , String ... properties ){
		String mappingName = null ;
		if( params != null && params.containsKey("mappingName") ){
			mappingName = params.get("mappingName").toString() ;
			params.remove("mappingName") ;
		}
		SearchCriteria criteria = resolveSearchCriteria( params , mappingName , properties ) ;
		List<Map<String,Object>> dataList = sqlTemplate.selectList("TemplateMapper.findList", criteria ) ;
		for( Map<String,Object> data : dataList ){
			handleHumpKey( data ) ;
		}
		return dataList ;
	}
	
	protected void handleHumpKey( Map<String,Object> dataMap ){
		if( dataMap == null || dataMap.isEmpty() ){
			return ;
		}
		Map<String,Object> tempDataMap = new HashMap<String,Object>() ;
		for( Map.Entry<String, Object> data : dataMap.entrySet() ){
			tempDataMap.put( StringUtil.toPropertyWithHump( data.getKey() ), data.getValue() ) ;
		}
		dataMap.clear();
		dataMap.putAll( tempDataMap );
	}
	
	protected void handleHumpKey( List<Map<String,Object>> dataList ){
		if( dataList == null ||dataList.isEmpty() ){
			return ;
		}
		for( Map<String,Object> data : dataList ){
			handleHumpKey( data ) ;
		}
	}
	
	public Map<String ,Object> findMapBy( String by , Object value , String ... properties ){
		List<Map<String,Object>> dataList = findMapListBy( by , value , properties ) ;
		if( dataList == null || dataList.isEmpty() ){
			return null ;
		}
		return dataList.get( 0 ) ;
	}
	
	public List<Map<String,Object>> findMapListBy( String by , Object value , String ... properties ){
		if( StringUtils.isEmpty( by ) || value == null ||
				(value instanceof List && ((List<?>)value).isEmpty() ) ){
			return null ;
		}
		SearchCriteria criteria = resolveSearchCriteria( by , value , properties ) ;
		List<Map<String,Object>> dataList = sqlTemplate.selectList("TemplateMapper.findList" , criteria ) ;
		if( dataList == null || dataList.isEmpty() ){
			return null ;
		}
		for( Map<String,Object> dataMap : dataList ){
			handleHumpKey( dataMap ) ;
		}
		return dataList ;
	}
	
	public <T> List<T> findBeanList( Map<String , Object> params , Class<T> clazz ){
		List<Map<String,Object>> dataList = findMapList( params ) ;
		if( dataList == null || dataList.isEmpty() ){
			return null ;
		}
		return BeanUtil.convertMapList( dataList , clazz ) ;
	}
	
	public <T> T findBean( Map<String,Object> params , Class<T> clazz ){
		List<T> dataList = findBeanList( params , clazz ) ;
		if( dataList == null || dataList.isEmpty() )
			return null ;
		return dataList.get( 0 ) ;
	}
	
	public <T> T findBeanBy( String by , Object value , Class<T> clazz , String ... properties ){
		Map<String,Object> dataMap = findMapBy( by , value , properties ) ;
		if( dataMap == null || dataMap.isEmpty() ){
			return null ;
		}
		return BeanUtil.convertMap( dataMap , clazz ) ;
	}
	
	public <T> List<T> findBeanListBy( String by , Object value , Class<T> clazz , String ... properties ){
		List<Map<String,Object>> dataMapList = findMapListBy( by , value , properties ) ;
		if( dataMapList == null || dataMapList.isEmpty() ){
			return null ;
		}
		return BeanUtil.convertMapList( dataMapList , clazz ) ;
	}
	
	public Object createMap( Map<String,Object> params ){
		UpdateCriteria criteria = resolveUpdateCriteria( params ) ;
		sqlTemplate.insert("TemplateMapper.create" , criteria ) ;
		return criteria.getId()  ;
	}
	
	public <T> Object createBean( T bean , Class<T> clazz , String ... properties ){
		Map<String,Object> beanMap = BeanUtil.getBeanMap( bean , true , properties) ;
		return createMap( beanMap ) ;
	}
	
	public int updateMap( String by , Object value , Map<String,Object> params ){
		if( StringUtils.isEmpty( by ) || params == null || params.isEmpty() ){
			throw new RuntimeException("更新数据失败，参数有误!") ;
		}
		by = StringUtil.toColumnWithHump( by ) ;
		UpdateCriteria criteria = resolveUpdateCriteria( params , new Condition( by , value , SYMBOL.EQ )) ;
		return sqlTemplate.update("TemplateMapper.update" , criteria ) ;
	}
	
	public int updateBean( String by , Object value , Object bean , String ... properties ){
		Map<String,Object> beanMap = BeanUtil.getBeanMap( bean , true , properties) ;
		return updateMap( by , value , beanMap ) ;
	}
	
	public int delete( Map<String,Object> condition ){
		if( condition == null || condition.isEmpty() ){
			throw new RuntimeException("删除操作必须添加条件!") ;
		}
		Condition [] conditions = new Condition[ condition.size() ] ;
		int index = 0;
		for( Map.Entry<String, Object> item : condition.entrySet() ){
			String by = StringUtil.toColumnWithHump( item.getKey() ) ;
			conditions[index++] = new Condition( by , item.getValue() , SYMBOL.EQ ) ;
		}
		UpdateCriteria criteria = resolveUpdateCriteria( conditions ) ;
		return sqlTemplate.update("TemplateMapper.delete" , criteria ) ;
	}
	
	@PostConstruct
	public abstract void init() ;
	
	protected <T> List<T> bean( List<Map<String,Object>> dataList , Class<T> clazz) {
		if( dataList == null || dataList.isEmpty() )
			return null ;
		return BeanUtil.convertMapList( dataList , clazz) ;
	}
	
}
