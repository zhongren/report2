package com.newproj.report.quotasituation.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.newproj.core.beans.BeanUtil;
import com.newproj.core.beans.MapParam;
import com.newproj.core.exception.BusinessException;
import com.newproj.core.json.JsonSerializer;
import com.newproj.core.rest.support.AbstractBaseService;
import com.newproj.core.util.JoinUtil;
import com.newproj.core.util.NumberUtil;
import com.newproj.core.util.ScriptUtil;
import com.newproj.report.collection.dal.dao.CollectionDao;
import com.newproj.report.collection.dto.Collection;
import com.newproj.report.quotasituation.constant.SituationConstant.InstType;
import com.newproj.report.quotasituation.constant.SituationConstant.SchoolTypeEnum;
import com.newproj.report.quotasituation.dal.dao.InstQuotaSituationDao;
import com.newproj.report.quotasituation.dal.dao.QuotaSituationDao;
import com.newproj.report.quotasituation.dal.dao.VariableDao;
import com.newproj.report.quotasituation.dto.InstQuotaSituationParam;
import com.newproj.report.quotasituation.dto.QuotaSituation;
import com.newproj.report.quotasituation.dto.Variable;
import com.newproj.report.school.dal.dao.SchoolCollectionDao;
import com.newproj.report.school.dal.dao.SchoolDao;
import com.newproj.report.school.dal.dao.SchoolTypeDao;
import com.newproj.report.school.dto.School;
import com.newproj.report.school.dto.SchoolCollection;
import com.newproj.report.school.dto.SchoolType;
import com.newproj.report.sys.dal.dao.SqlExecutor;
@Service
public class SchoolQuotaSituationService extends AbstractBaseService{
	
	@Autowired
	private InstQuotaSituationDao schoolSituationDao ;
	
	@Autowired
	private QuotaSituationDao situationDao ;
	
	@Autowired
	private SchoolCollectionDao schoolCollectionDao ;
	
	@Autowired
	private CollectionDao collectionDao ;
	
	@Autowired
	private SchoolDao schoolDao ;
	
	@Autowired
	private VariableDao variableDao ;
	
	@Autowired
	private SchoolTypeDao schoolTypeDao ;
	
	private JsonSerializer jsonse = new JsonSerializer()  ;
	
	
	public void init() {
		setMapper( schoolSituationDao );
	} 
	
	/**
	 * 计算学校标准化建设监测数据统计 .
	 * 内部参数:当前学校所有填报指标值,当前学校信息,监测计算结果
	 * 
	 * @param schoolId
	 * @param reportingId
	 * @return
	 */
	private List<InstQuotaSituationParam> calculate( int schoolId , int reportingId ){
		
		Map<String,Object> args = new HashMap<String,Object>() ;
		
		School school = schoolDao.findBeanBy("id", schoolId, School.class ) ;
		if( school == null )
			throw new BusinessException("学校数据不存在!") ;
		//获取学校参数
		args.putAll( getSchoolArgs( school ) ) ;
		
		SchoolType schoolType = schoolTypeDao.findBeanBy("id", school.getType() , SchoolType.class ) ;
		//统计小学初中9年 12年义务教育学校
		if( schoolType == null || !Arrays.asList(1,2,3).contains( schoolType.getType() ) )
			return null ;
		
		List<QuotaSituation> situationList = findSchoolQuotaSituation( schoolType.getType() ) ;
		if( situationList == null || situationList.isEmpty() )
			return null ;
		
		//获取学校类型参数
		args.putAll( getSchoolTypeArgs() );
		
		//获取填报数据参数
		Map<String,Object> collectionArgs = getCollectionArgs( reportingId , schoolId ) ;
		if( collectionArgs == null || collectionArgs.isEmpty() )
			//throw new BusinessException("当前学校无填报数据!") ;
			return null ;
		
		List<InstQuotaSituationParam> instSituationList = new ArrayList<InstQuotaSituationParam>() ;
		for( QuotaSituation situation : situationList ){
			
			InstQuotaSituationParam instSituation = new InstQuotaSituationParam() ;
			instSituation.setSituationName( situation.getName() );
			instSituation.setSituationId(situation.getId());
			instSituation.setInstId( schoolId );
			instSituation.setInstType( situation.getInstType() );
			instSituation.setSchoolType( situation.getSchoolType() );
			instSituation.setReportingId(reportingId);
			
			//计算监测结果
			String calculateScript =  situation.getCalculatedFormula() , calculateResult = null ;
			if( calculateScript != null && calculateScript.trim().length() > 0 ){
				Map<String,Object> calculateNote = new HashMap<String,Object>() ;
				try{
					Map<String,Object> inArgs = getVarsValueMap(calculateScript , args) ;
					//保存计算信息 .
					calculateNote.put("inArgs", inArgs ) ;
					calculateNote.put("script", calculateScript ) ;
					
					calculateResult = ScriptUtil.eval( calculateScript , inArgs ) ;
					
					instSituation.setCalculatedValue( calculateResult );
					calculateNote.put("result", "计算结果:[" + calculateResult + "]" ) ;
					instSituation.setState( 0 );
				}catch(Exception e){
					logger.error(String.format("监测结果计算出错:SCHOOL_ID:%s,SITUATION_NAME:%s,SCRIPT:%s",
							schoolId , situation.getName() , situation.getCalculatedFormula() ) , e );
					calculateNote.put("result", "计算出错:[" + e.getMessage() +"]" ) ;
					instSituation.setState( 1 );
				}
				//保存计算计算结果
				instSituation.setCalculatedExtra( jsonse.toJson( calculateNote ) );
			}
			//指标计算成功,计算是否合格 .
			if( instSituation.getState() == null || instSituation.getState() == 0 ) {
				Map<String,Object> eligNote = new HashMap<String,Object>() ;
				
				//计算是否合格
				args.put("$calculatedValue", NumberUtil.parsePossible( calculateResult ) ) ;
				String eligibleFormula = situation.getEligibleFormula() , eligibleResult = null ;
				if( eligibleFormula != null && eligibleFormula.trim() .length() > 0 ){
					try{
						Map<String,Object> inArgs =  getVarsValueMap( eligibleFormula ,args ) ;
						eligNote.put("inArgs", inArgs ) ;
						eligNote.put("script", eligibleFormula ) ;
						
						eligibleResult = ScriptUtil.eval( eligibleFormula , inArgs ) ;
						
						instSituation.setEligibleValue(eligibleResult);
						eligNote.put("result", String.format("计算结果:[%s]", eligibleResult) ) ;
						instSituation.setState( 0 );
					}catch(Exception e){
						logger.error(String.format("监测结果计算出错:SCHOOL_ID:%s,SITUATION_NAME:%s,SCRIPT:%s",
								schoolId , situation.getName() , situation.getCalculatedFormula() ) ,e);
						eligNote.put("result",  String.format("计算出错:[%s]", e.getMessage() )  ) ;
						instSituation.setState( 1 );
					}
				}
				instSituation.setEligibleExtra( jsonse.toJson( eligNote ) );
			}
			instSituationList.add( instSituation ) ;
		}
		return instSituationList ;
	}
	
	private List<QuotaSituation> findSchoolQuotaSituation( int schoolType ){
		//统计指标
		List<String> schoolTypes = new ArrayList<String>() ;
		schoolTypes.add( SchoolTypeEnum.SCHOOL.name() ) ;
		switch( schoolType ){
		case 	1 	:
			schoolTypes.add( SchoolTypeEnum.PRIMARY.name() ) ;
			break ;
		case 	2 	:
			schoolTypes.add( SchoolTypeEnum.MIDDLE.name() ) ;
		case 	3	: 
			schoolTypes.add( SchoolTypeEnum.MIDDLE.name() ) ;
			schoolTypes.add( SchoolTypeEnum.PRIMARY.name() ) ;
		}
		Map<String,Object> situationParam = new HashMap<String,Object>() ;
		situationParam.put("instType", InstType.SCHOOL.name() ) ;
		situationParam.put("schoolType", schoolTypes ) ;
		List<QuotaSituation> situationList = situationDao.findBeanList(
				situationParam , QuotaSituation.class ) ;
		if( situationList == null || situationList.isEmpty() )
			return null ;
		return situationList ;
	}
	
	private Map<String,Object> getSchoolArgs( School school ){
		//获取学校信息参数 .
		Map<String,Object> schoolMap = BeanUtil.getBeanMap( school, false ) ;
		Map<String,Object> args = new HashMap<String,Object>() ;
		for( Map.Entry<String,Object> item : schoolMap.entrySet() ){
			args.put( String.format("$school_%s",  item.getKey() ) , NumberUtil.parsePossible( item.getValue() ) ) ;
		}
		return args ;
	}
	
	private Map<String,Object> getSchoolTypeArgs( ){
		List<SchoolType> types = schoolTypeDao.findBeanList( null , SchoolType.class ) ;
		Map<String,Object> typeArgs = new HashMap<String,Object>() ;
		if( types == null || types.isEmpty() )
			return typeArgs ;
		for( SchoolType type : types ){
			typeArgs.put( String.format("$school_type_%s", type.getId() ), type.getId() ) ;
			//权重参数
			typeArgs.put( String.format("$school_type_%s_%s", type.getId() , "primary_rate"  ),
					NumberUtil.parseFloat( type.getPrimaryRate() , 0f ) ) ;
			typeArgs.put( String.format("$school_type_%s_%s", type.getId() , "middle_rate"  ),
					NumberUtil.parseFloat( type.getMiddleRate() , 0f ) ) ;
			typeArgs.put( String.format("$school_type_%s_%s", type.getId() , "high_rate"  ),
					NumberUtil.parseFloat( type.getHighRate() , 0f ) ) ;
		}
		return typeArgs ;
	}
	
	private Map<String,Object> getCollectionArgs( int reportingId , int schoolId ){
		//填报参数 .
		List<SchoolCollection> collectionList = schoolCollectionDao.findBeanList(new MapParam<String,Object>()
				.push("schoolId" , schoolId )
				.push("reportId" , reportingId ) , SchoolCollection.class ) ;
		if( collectionList == null || collectionList.isEmpty() )
			return null ;
		Map<String,Object> typeArgs = new HashMap<String,Object>() ;
		JoinUtil.join( collectionList , new String[]{"dataType"}, "collectionId",
				collectionDao.findBeanListBy("id", JoinUtil.fieldsValue(collectionList, "collectionId"), Collection.class, "id" , "valueType"), 
				new String[]{"valueType"}, "id");
		for( SchoolCollection collection : collectionList ){
			typeArgs.put(String.format("$%s", collection.getCollectionId() ), collection.getContent() ) ;
		}
		return typeArgs ;
	}
	
	private Map<String,Object> getVarsValueMap( String script , Map<String,Object> busiValueMap ){
		if( StringUtils.isEmpty( script ) )
			return null ;
		List<String> vars = ScriptUtil.scriptVars( script ) ;
		if( vars.isEmpty() ) return null ;
		List<Variable> variables = variableDao.findBeanListBy("varName", vars , Variable.class ) ;
		Map<String,Variable> variableMap = new HashMap<String,Variable>() ;
		if( variables != null && !variables.isEmpty() ){
			for( Variable variable : variables )
				variableMap.put( variable.getVarName() , variable ) ;
		}
		Map<String,Object> result = new HashMap<String,Object>() ;
		for( String var : vars ){
			if( !variableMap.containsKey( var ) ){
				result.put( var , null ) ;
				continue ;
			}
			Variable variable = variableMap.get( var ) ;
			if( "static".equalsIgnoreCase( variable.getType() ) ){
				result.put( var , NumberUtil.parsePossible( variable.getValue() ) ) ;
			}else if( "var".equalsIgnoreCase( variable.getType() ) ){
				String keyName = StringUtils.isEmpty( 
						variable.getValue() ) ? var : variable.getValue() ;
				result.put(var ,NumberUtil.parsePossible(  busiValueMap.get( keyName ) ) ) ;
			}else if( "sql".equalsIgnoreCase( variable.getType() ) ){
				result.put(var, SqlExecutor.get( variable.getValue() , getVarsValueMap( variable.getValue() , busiValueMap ) ) ) ;
			}
		}
		return result ;
	}
	
	@Transactional( propagation=Propagation.REQUIRED , rollbackFor= Exception.class )
	public void calculate( int reportingId , int ... schoolsId ){
		if( schoolsId == null || schoolsId.length == 0 )
			throw new BusinessException("请选择学校!") ;
		for( int schoolId : schoolsId ){
			List<InstQuotaSituationParam> situations = calculate( schoolId , reportingId ) ;
			//删除重建 .
			schoolSituationDao.delete( new MapParam<String,Object>()
					.push("instId" , schoolId ) 
					.push("instType" ,InstType.SCHOOL.name() ) 
					.push("schoolType" , Arrays.asList( SchoolTypeEnum.SCHOOL.name()  , 
							SchoolTypeEnum.PRIMARY.name() , SchoolTypeEnum.MIDDLE.name() ) )
					.push("reportingId" , reportingId ) ) ;
			if( situations == null || situations.isEmpty() )
				continue ;
			for( InstQuotaSituationParam situation : situations )
				schoolSituationDao.createBean( situation , InstQuotaSituationParam.class ) ;
		}
	}

}
