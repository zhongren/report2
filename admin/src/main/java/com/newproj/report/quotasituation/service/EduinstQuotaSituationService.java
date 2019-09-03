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
import com.newproj.report.eduinst.EduinstConstant.EduinstType;
import com.newproj.report.eduinst.dal.dao.EduinstCollectionDao;
import com.newproj.report.eduinst.dal.dao.EduinstDao;
import com.newproj.report.eduinst.dto.Eduinst;
import com.newproj.report.eduinst.dto.EduinstCollection;
import com.newproj.report.quotasituation.constant.SituationConstant.InstType;
import com.newproj.report.quotasituation.dal.dao.InstQuotaSituationDao;
import com.newproj.report.quotasituation.dal.dao.QuotaSituationDao;
import com.newproj.report.quotasituation.dal.dao.VariableDao;
import com.newproj.report.quotasituation.dto.InstQuotaSituationParam;
import com.newproj.report.quotasituation.dto.QuotaSituation;
import com.newproj.report.quotasituation.dto.Variable;
import com.newproj.report.sys.dal.dao.SqlExecutor;
@Service
public class EduinstQuotaSituationService extends AbstractBaseService{
	
	@Autowired
	private EduinstDao eduinstDao ;
	
	@Autowired
	private QuotaSituationDao situationDao ;
	
	@Autowired
	private EduinstCollectionDao eduinstCollectionDao ;
	
	@Autowired
	private CollectionDao collectionDao ;
	
	@Autowired
	private VariableDao variableDao ;
	
	@Autowired
	private InstQuotaSituationDao eduinstSituationDao ;
	
	private JsonSerializer jsonse = new JsonSerializer()  ;
	
	/**
	 * 计算教育局标准化建设监测数据统计 .
	 * 内部参数:教育局信息，填报数据
	 * 
	 * @param eduinstId
	 * @param reportingId
	 * @return
	 */
	private List<InstQuotaSituationParam> calculate( int eduinstId , int reportingId ){
		
		Map<String,Object> args = new HashMap<String,Object>() ;
		//获取教育局参数
		args.putAll( getEduinstArgs( eduinstId ) ) ;
		
		Eduinst eduinst = eduinstDao.findBeanBy("id", eduinstId, Eduinst.class ) ;
		EduinstType eduinstType = EduinstType.parse( eduinst.getType() ) ;
		InstType instType = null ;
		if( eduinstType == EduinstType.CITY )
			instType = InstType.CITY_EDUINST ;
		else if( eduinstType == EduinstType.PROVINCE )
			instType = InstType.PROVINCE_EDUINST ;
		else 
			instType = InstType.COUNTY_EDUINST ;
		
		//统计指标
		List<QuotaSituation> situationList = situationDao.findBeanListBy(
				"instType" , Arrays.asList( InstType.EDUINST.name() , instType ) , QuotaSituation.class ) ;
		if( situationList == null || situationList.isEmpty() )
			return null ;
		
		//获取填报数据参数
		Map<String,Object> collectionArgs = getCollectionArgs( reportingId , eduinstId ) ;
		if( collectionArgs == null || collectionArgs.isEmpty() )
			throw new BusinessException("当前教育局无填报数据!") ;
		
		List<InstQuotaSituationParam> instSituationList = new ArrayList<InstQuotaSituationParam>() ;
		for( QuotaSituation situation : situationList ){
			
			InstQuotaSituationParam instSituation = new InstQuotaSituationParam() ;
			instSituation.setSituationName( situation.getName() );
			instSituation.setSituationId(situation.getId());
			instSituation.setInstId( eduinstId );
			instSituation.setInstType( situation.getInstType() );
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
					logger.error(String.format("监测结果计算出错:EDUINST_ID:%s,SITUATION_NAME:%s,SCRIPT:%s",
							eduinstId , situation.getName() , situation.getCalculatedFormula() ) , e );
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
						logger.error(String.format("监测结果计算出错:EDUINST_ID:%s,SITUATION_NAME:%s,SCRIPT:%s",
								eduinstId , situation.getName() , situation.getCalculatedFormula() ) ,e);
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
	
	private Map<String,Object> getEduinstArgs( int eduinstId ){
		Eduinst eduinst = eduinstDao.findBeanBy("id", eduinstId, Eduinst.class ) ;
		if( eduinst == null )
			throw new BusinessException("教育局不存在!") ;
		//获取学校信息参数 .
		Map<String,Object> eduinstMap = BeanUtil.getBeanMap( eduinst, false ) ;
		Map<String,Object> args = new HashMap<String,Object>() ;
		for( Map.Entry<String,Object> item : eduinstMap.entrySet() ){
			args.put( String.format("$eduinst_%s",  item.getKey() ) , NumberUtil.parsePossible( item.getValue() ) ) ;
		}
		return args ;
	}
	
	private Map<String,Object> getCollectionArgs( int reportingId , int eduinstId ){
		//填报参数 .
		List<EduinstCollection> collectionList = eduinstCollectionDao.findBeanList(new MapParam<String,Object>()
				.push("instId" , eduinstId )
				.push("reportId" , reportingId ) , EduinstCollection.class ) ;
		if( collectionList == null || collectionList.isEmpty() )
			return null ;
		Map<String,Object> typeArgs = new HashMap<String,Object>() ;
		JoinUtil.join( collectionList , new String[]{"dataType"}, "collectionId",
				collectionDao.findBeanListBy("id", JoinUtil.fieldsValue(collectionList, "collectionId"), Collection.class, "id" , "valueType"), 
				new String[]{"valueType"}, "id");
		for( EduinstCollection collection : collectionList ){
			Object value = collection.getContent() ;
			if( collection.getDataType() != null && collection.getDataType().equalsIgnoreCase("number") )
				value = NumberUtil.parsePossible(value , 0 ) ;
			typeArgs.put(String.format("$eduinst_collection_%s", collection.getCollectionId() ), value ) ;
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
	public void calculate( int reportingId , int ... eduinstIds ){
		if( eduinstIds == null || eduinstIds.length == 0 )
			throw new BusinessException("请选择教育局!") ;
		for( int eduinstId : eduinstIds ){
			List<InstQuotaSituationParam> situations = calculate( eduinstId , reportingId ) ;
			//删除重建 .
			eduinstSituationDao.delete( new MapParam<String,Object>()
					.push("instId" , eduinstId ) 
					.push("instType" , Arrays.asList( InstType.EDUINST.name() , InstType.CITY_EDUINST.name() ,
							InstType.COUNTY_EDUINST.name(), InstType.PROVINCE_EDUINST.name()  ) )
					.push("reportingId" , reportingId ) ) ;
			for( InstQuotaSituationParam situation : situations )
				eduinstSituationDao.createBean( situation , InstQuotaSituationParam.class ) ;
		}
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		setMapper( eduinstSituationDao ) ;
	}
}
