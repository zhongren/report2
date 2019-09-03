package com.newproj.core.mybatis;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.newproj.core.beans.BeanUtil;
import com.newproj.core.mybatis.Condition.SYMBOL;
import com.newproj.core.util.AnnotationUtils;
import com.newproj.core.util.StringUtil;

public class AbstractConditionHandler {
	private Map<String,ConditionMapping> conditionMappings = new HashMap<String,ConditionMapping>() ;
	protected final static String DEF_MAPPING_KEY = "default.mapping.key" ;
	private Condition[]  unityFlterCondition ;
	protected void putMapping( String mappingName , ConditionMapping mapping ){
		conditionMappings.put( mappingName , mapping ) ;
	}
	protected ConditionMapping getMapping( String mappingName ){
		return conditionMappings.get( mappingName ) ;
	}
	protected SearchCriteria resolveSearchCriteria(
			Map<String , Object> params , String mappingName , String ... properties ){
		if( StringUtils.isEmpty( mappingName ) ) {
			mappingName = DEF_MAPPING_KEY ;
		}
		Table table = AnnotationUtils.getAnnotation( this.getClass() , Table.class ) ;
		if( table == null ){
			throw new RuntimeException("查询失败，未发现Table注解.") ;
		}
		SearchCriteria criteria = new SearchCriteria( table.value() ) ;
		ConditionMapping mapping = conditionMappings.get( mappingName ) ;
		if( mapping == null ) mapping = new ConditionMapping() ;
		if( mapping != null && params != null && !params.isEmpty() ){
			for( Map.Entry<String, Object> param : params.entrySet() ){
				if( !mapping.containsKey( param.getKey() ) ){
					SYMBOL symbol = SYMBOL.EQ ;
					String key = param.getKey() ;
					if( key.startsWith( "?" ) ){
						symbol = SYMBOL.LIKE ;
						key = key.substring( 1 ) ;
					}
					Condition condition = new Condition( 
							StringUtil.toColumnWithHump( key ) ,  param.getValue() , symbol  ) ;
					criteria.addConditions( condition );
					continue ;
				}
				Condition condition = BeanUtil.deepCopy( mapping.get( param.getKey() ) ) ;
				condition.setValue( param.getValue() );
				criteria.addConditions( condition );
			}
		}
		if( properties != null &&  properties.length != 0  ){
			for( String property : properties ){
				if( StringUtils.isEmpty(  property ) ){
					continue ;
				}
				criteria.addColumns( StringUtil.toColumnWithHump( property ) );
			}
		}
		if( unityFlterCondition != null && unityFlterCondition.length != 0 ){
			criteria.addConditions( unityFlterCondition  );
		}
		return criteria ;
	}
	
	protected SearchCriteria resolveSearchCriteria(
			String by , Object value , String ... properties ){
		if( StringUtils.isEmpty( by ) ){
			throw new RuntimeException("构建查询参数失败,查询字段不能为空!") ;
		}
		Table table = AnnotationUtils.getAnnotation( this.getClass() , Table.class ) ;
		if( table == null ){
			throw new RuntimeException("查询失败，未发现Table注解.") ;
		}
		SearchCriteria criteria = new SearchCriteria( table.value() ) ;
		criteria.addConditions( new Condition( StringUtil.toColumnWithHump( by ) , value , SYMBOL.EQ ) ) ;
		if( properties != null &&  properties.length != 0  ){
			for( String property : properties ){
				if( StringUtils.isEmpty(  property ) ){
					continue ;
				}
				criteria.addColumns( StringUtil.toColumnWithHump( property ) );
			}
		}
		if( unityFlterCondition != null && unityFlterCondition.length != 0 ){
			criteria.addConditions( unityFlterCondition  );
		}
		return criteria ;
	}
	
	protected UpdateCriteria resolveUpdateCriteria( Map<String,Object> params , Condition ... conditions  ){
		if( params == null || params.isEmpty() ){
			throw new RuntimeException("记录创建失败，参数为空 .") ;
		}
		Table table = AnnotationUtils.getAnnotation( this.getClass() , Table.class ) ;
		if( table == null ){
			throw new RuntimeException("记录创建失败，DAO未发现Table注解 .") ;
		}
		UpdateCriteria criteria = new UpdateCriteria( table.value() ) ;
		for( Map.Entry<String, Object> param : params.entrySet() ){
			UpdateValue value = new UpdateValue() ;
			value.setColumn( StringUtil.toColumnWithHump( param.getKey() ) );
			value.setValue( param.getValue() );
			criteria.addUpdateValue( value );
		}
		if( conditions != null && conditions.length != 0 ){
			criteria.setConditions( Arrays.asList( conditions ) );
		}
		if( unityFlterCondition != null && unityFlterCondition.length != 0 ){
			criteria.setConditions( Arrays.asList( unityFlterCondition )  );
		}
		return criteria ;
	}
	
	protected UpdateCriteria resolveUpdateCriteria( Condition ... conditions  ){
		Table table = AnnotationUtils.getAnnotation( this.getClass() , Table.class ) ;
		if( table == null ){
			throw new RuntimeException("记录创建失败，DAO未发现Table注解 .") ;
		}
		UpdateCriteria criteria = new UpdateCriteria( table.value() ) ;
		if( conditions != null && conditions.length != 0 ){
			criteria.setConditions( Arrays.asList( conditions ) );
		}
		if( unityFlterCondition != null && unityFlterCondition.length != 0 ){
			criteria.setConditions( Arrays.asList( unityFlterCondition )  );
		}
		return criteria ;
	}
	
	public Condition[] getUnityFlterCondition() {
		return unityFlterCondition;
	}
	public void setUnityFlterCondition(Condition[] unityFlterCondition) {
		this.unityFlterCondition = unityFlterCondition;
	}
}
