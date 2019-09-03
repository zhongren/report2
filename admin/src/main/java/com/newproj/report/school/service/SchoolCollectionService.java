package com.newproj.report.school.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.newproj.core.beans.MapParam;
import com.newproj.core.exception.BusinessException;
import com.newproj.core.page.PageParam;
import com.newproj.core.rest.support.AbstractBaseService;
import com.newproj.core.util.NumberUtil;
import com.newproj.core.util.ScriptUtil;
import com.newproj.core.util.TimeUtil;
import com.newproj.report.collection.dal.dao.CollectionDao;
import com.newproj.report.collection.dto.Collection;
import com.newproj.report.export.bean.SchoolCollectionData;
import com.newproj.report.reporting.dal.dao.ReportingDao;
import com.newproj.report.school.dal.dao.SchoolAuditingDao;
import com.newproj.report.school.dal.dao.SchoolClassesNumsDao;
import com.newproj.report.school.dal.dao.SchoolCollectionDao;
import com.newproj.report.school.dal.dao.SchoolDao;
import com.newproj.report.school.dal.dao.SchoolProcessDao;
import com.newproj.report.school.dto.School;
import com.newproj.report.school.dto.SchoolCollection;
import com.newproj.report.school.dto.SchoolCollectionParam;
import com.newproj.report.school.dto.SchoolProcess;
import com.newproj.report.school.dto.SchoolProcessParam;
import com.newproj.report.school.dto.SchoolReportingData;
@Service
public class SchoolCollectionService extends AbstractBaseService{

	@Autowired
	private SchoolCollectionDao schoolCollectionDao ;
	
	@Autowired
	private CollectionDao collectionDao ;
	
	@Autowired
	private SchoolProcessDao processDao ;
	
	@Autowired
	private SchoolAuditingDao auditingDao ;
	
	@Autowired
	private ReportingDao reportingDao ;
	
	@Autowired
	private SchoolClassesNumsDao classNumDao ;
	
	@Autowired
	private SchoolDao schoolDao ;
	
	public int saveSchoolCollection( SchoolCollectionParam param ){
		if( param.getSchoolId() == null || 
				param.getCollectionId() == null || 
					param.getReportId() == null ){
			throw new BusinessException("填报参数有误,数据保存失败!") ;
		}
		Collection collection = null ;
		if( ( collection = collectionDao.findBeanBy(
				"id", param.getCollectionId(), Collection.class , "id" , "quotaId") ) == null )
			throw new BusinessException("无效的采集项,数据保存失败!") ;
		
		param.setCreateTime(TimeUtil.now());
		param.setQuotaId( collection.getQuotaId() );
		
		List<SchoolCollection> oldRow = schoolCollectionDao.findBeanList(
				new MapParam<String,Object>().push("schoolId", param.getSchoolId() )
					.push("collectionId", param.getCollectionId() )
					.push("reportId", param.getReportId() ), SchoolCollection.class ) ;
		if( oldRow != null && !oldRow.isEmpty() ){//更新记录
			schoolCollectionDao.updateBean("id" , oldRow.get(0).getId() , param ) ;
			return oldRow.get(0).getId() ;
		}
		//添加新记录
		return NumberUtil.parseInt( schoolCollectionDao
				.createBean( param , SchoolCollectionParam.class ) , 0 ) ;	
	}
	
	/**
	 * 批量保存学校填报数据 .
	 * 
	 * @param params
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED , timeout=10000 )
	public List<Integer> saveSchoolCollection( List<SchoolCollectionParam> params ){
		
		if( params == null || params.isEmpty() )
			throw new BusinessException("保存填报数据失败,无填报数据!") ;
		
		//保存填报数据.
		List<Integer> ids = new ArrayList<Integer>() ;
		for( SchoolCollectionParam item : params ){
			ids.add( saveSchoolCollection( item ) ) ;
		}
		SchoolCollectionParam param = params.get( 0 ) ;
		updateProcess( param.getReportId() , param.getSchoolId() ) ;
		return ids ;
	}
	
	/**
	 * 更新学校填报进度 .
	 * 
	 * @param reportId
	 * @param schoolId
	 */
	public void updateProcess( int reportId , int schoolId ){
		School school = schoolDao.findBeanBy("id", schoolId , School.class ) ;
		if( school == null )
			throw new BusinessException("学校不存在!") ;
		
		//查询填报表中填报数量 .
		List<?> dataList = schoolCollectionDao.findMapList(new MapParam<String,Object>()
				.push("reportId", reportId )
				.push("schoolId", schoolId ), "id" ) ;
		int finishedNum = dataList == null ? 0 : dataList.size() ;
		
		/*//查询是否导入学校班额
		if( classNumDao.findMapList( new MapParam<String,Object>()
				.push("schoolId" , schoolId ).push("year" , reportId ) ) != null ){
			finishedNum+=1 ;
		}*/
		
		SchoolProcessParam editable = new SchoolProcessParam() ;
		editable.setFinishNum( finishedNum ) ;
		
		//查询所有填报数量
		List<?> collections = collectionDao.findSchoolCollection(
				reportId , school.getType() , Collection.class );
		editable.setTotalNum( collections == null ? 0 : collections.size() );
		
		//更新填报进度 .
		processDao.updateBean("id", getProcess( schoolId , reportId ).getId() , editable ) ;
	}
	
	public SchoolProcess getProcess( int schoolId , int reportId ){
		SchoolProcess process = processDao.findBean( new MapParam<String,Object>()
				.push("schoolId", schoolId )
				.push("reportId" ,reportId ), SchoolProcess.class ) ;
		if( process == null ){//创建进度记录 .
			SchoolProcessParam editable = new SchoolProcessParam() ;
			editable.setSchoolId( schoolId );
			editable.setReportId( reportId );
			processDao.createBean(editable, SchoolProcessParam.class ) ;
			updateProcess( reportId , schoolId ) ;
			process = processDao.findBean( new MapParam<String,Object>()
					.push("schoolId", schoolId )
					.push("reportId" ,reportId ), SchoolProcess.class ) ;
		}
		
		return process ;
	}
	
	public List<Collection> findSchoolTypeCollection( Integer type , int reportingId ){
		return collectionDao.findSchoolCollection(reportingId, type, Collection.class ) ;
	}
	
	public List<SchoolProcess> findSchoolProcess( Map<String,Object> params , PageParam pageParam ){
		return withMapper( processDao).findList(params, pageParam, SchoolProcess.class );
	}
	
	
	/**
	 * 查询学校填报列表 .
	 * 
	 * @param schoolId
	 * @param reportId
	 * @return
	 */
	public List<SchoolCollection> findSchoolCollection( int schoolId, int reportId ){
		return schoolCollectionDao.findBeanList(new MapParam<String,Object>()
				.push("reportId", reportId )
				.push("schoolId", schoolId ), SchoolCollection.class  ) ;
	}
	
	public boolean hasFinished( int schoolId , int reportId ){
		SchoolProcess process = processDao.findBean(new MapParam<String,Object>()
				.push("schoolId", schoolId)
				.push("reportId", reportId ), SchoolProcess.class ) ;
		return process != null && process.getTotalNum().intValue() == process.getFinishNum().intValue() ;
	}
	
	/**
	 * 提交审核 .
	 * 
	 * @param schoolId
	 * @param reportId
	 * @param userId
	 */
	public void submitAudit( int schoolId , int reportId , int userId ){
		//服务端校验 .
		List<Map<String,String>> errors = validateSchoolCollectionData( schoolId , reportId ) ;
		if( errors != null && !errors.isEmpty() )
			throw new BusinessException( 2 , JSONObject.wrap( errors ).toString() ) ;
		
		SchoolProcess process = processDao.findBean(new MapParam<String,Object>()
				.push("schoolId", schoolId)
				.push("reportId", reportId ), SchoolProcess.class ) ;
		if( !hasFinished( schoolId , reportId ) )
			throw new BusinessException("提交审核失败,当前填报未完成!") ;
		if( ! Arrays.asList(0 , 4).contains( process.getStatus() )  )
			throw new BusinessException("提交审核失败,当前填报已提交!") ;
		
		SchoolProcessParam editable = new SchoolProcessParam() ;
		editable.setStatus( 1 );//待审核
		editable.setSubmitTime(TimeUtil.now() );
		editable.setSubmitId( userId );
		processDao.updateBean("id", process.getId(), editable  ) ;
	}
	
	
	private List<Map<String,String>> validateSchoolCollectionData( int schoolId , int reportingId ){
		School school = schoolDao.findBeanBy("id", schoolId, School.class ) ;
		if( school == null )
			throw new BusinessException("学校不存在!") ;
		List<SchoolReportingData> dataList = auditingDao.exportReportingData(
				schoolId, school.getType() , reportingId , SchoolReportingData.class ) ;
		if( dataList == null || dataList.isEmpty() )
			throw new BusinessException("没有发现填报数据!") ;
		Map<String,Object> params = new HashMap<String,Object>() ;
		for( SchoolReportingData data : dataList ){
			Object value = data.getContent() ;
			if(  data.getValueType() != null && data.getValueType().equalsIgnoreCase("number") ){
				if( value == null )
					value = 0 ;
				
				if( Pattern.matches("^[0-9]+$", value.toString() ) )
					value = NumberUtil.parseInt( value , 0 ) ;
				else if( Pattern.matches("^[0-9]+\\.[0-9]+$", value.toString() ) )
					value = NumberUtil.parseFloat( value , 0f ) ;
			}
			params.put(String.format("$%s", data.getCollectionId() ) , value ) ;
		}
		
		List<Map<String,String>> errors = new ArrayList<Map<String,String>>();
		for( SchoolReportingData data : dataList ){
			String validation = data.getValidation() ;
			if( validation == null || validation.trim().length() == 0 )
				continue ;
			JSONObject validJson = null ;
			String script = null , message = "校验不通过!";
			try{
				validJson = new JSONObject( validation ) ;
				if( !validJson.has("rules") || !validJson.getJSONObject("rules").has("script") )
					continue ;
				script = validJson.getJSONObject("rules").getString("script") ;
				if( validJson.has("messages") ){
					Object messages = validJson.get("messages") ;
					if( messages instanceof String )
						message = messages.toString() ;
					else if( messages instanceof JSONObject && ((JSONObject)messages).has("script")){
						message = ((JSONObject)messages).getString("script") ;
					}
				}
			}catch(Exception e){ }
			//空值不参与校验。
			if( validJson == null || data.getContent() == null || data.getContent().trim().length() == 0 ) 
				continue ;
			Map<String,String> error = new MapParam<String,String>() 
					.push("quota1" , data.getQuota1Name() )
					.push("quota2" , data.getQuota2Name() ) 
					.push("title" , data.getTitle() ) ;
			try{
				String result = doScriptValidate(script , params ) ;
				if( !Arrays.asList("true","false").contains( result.toLowerCase() ) ){
					error.put("message", result ) ;
				}else if( Boolean.parseBoolean( result ) )
					continue ;
				else error.put("message", message ) ;
			}catch(Exception e){
				error.put("message", e.getMessage() ) ;
			}
			errors.add( error ) ;
		}
		return errors.isEmpty() ? null : errors ;
	}
	
	private String doScriptValidate( String script , Map<String,Object> params ){
		String result = null ;
		try {
			result = ScriptUtil.eval(script, params) ;
		} catch (Exception e) {
			throw new BusinessException("校验脚本出错!") ;
		}
		return result ;
	}
	
	/**
	 * 填报审核 .
	 * 
	 * @param schoolId
	 * @param reportId
	 * @param pass
	 * @param note
	 * @param userId
	 */
	public void reportAudit( int schoolId , int reportId , boolean pass , String note , int userId ){
		SchoolProcess process = processDao.findBean(new MapParam<String,Object>()
				.push("schoolId", schoolId)
				.push("reportId", reportId ), SchoolProcess.class ) ;
		if( process == null || process.getStatus() != 1 )
			throw new BusinessException("审核出错,当前填报无法审核!") ;
		
		SchoolProcessParam editable = new SchoolProcessParam() ;
		editable.setAuditId( userId );
		editable.setAuditTime( TimeUtil.now() );
		editable.setAuditNote( note );
		editable.setStatus( pass ? 2 : 3 );
		processDao.updateBean("id", process.getId(), editable  ) ;
	}
	
	public List<SchoolCollectionData>   getSchoolCollections(String sid) throws SQLException{
		 return schoolCollectionDao.getSchoolCollections(sid);
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		setMapper(schoolCollectionDao) ;
	}

}
