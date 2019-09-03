package com.newproj.report.quotasituation.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newproj.core.exception.BusinessException;
import com.newproj.core.rest.support.AbstractBaseService;
import com.newproj.core.util.JoinUtil;
import com.newproj.report.collection.dal.dao.CollectionDao;
import com.newproj.report.collection.dto.Collection;
import com.newproj.report.eduinst.EduinstConstant.EduinstType;
import com.newproj.report.eduinst.dal.dao.EduinstDao;
import com.newproj.report.eduinst.dto.Eduinst;
import com.newproj.report.quotasituation.constant.SituationConstant.InstType;
import com.newproj.report.quotasituation.constant.SituationConstant.SchoolTypeEnum;
import com.newproj.report.quotasituation.dal.dao.InstQuotaSituationDao;
import com.newproj.report.quotasituation.dal.dao.QuotaSituationDao;
import com.newproj.report.quotasituation.dto.InstQuotaSituation;
import com.newproj.report.quotasituation.dto.InstQuotaSituationReport;
import com.newproj.report.quotasituation.dto.QuotaSituation;
import com.newproj.report.reporting.dal.dao.ReportingDao;
import com.newproj.report.reporting.dto.Reporting;
import com.newproj.report.school.dal.dao.SchoolDao;
import com.newproj.report.school.dal.dao.SchoolTypeDao;
import com.newproj.report.school.dto.School;
import com.newproj.report.school.dto.SchoolType;
import com.newproj.report.sys.dal.dao.CityDao;
import com.newproj.report.sys.dto.City;

@Service
public class SituationReportService extends AbstractBaseService{
	
	@Autowired
	private InstQuotaSituationDao instSituationDao ;
	
	@Autowired
	private CollectionDao collectionDao ;
	
	@Autowired
	private ReportingDao reportingDao ;
	
	@Autowired
	private SchoolDao schoolDao ;
	
	@Autowired
	private SchoolTypeDao schoolTypeDao ;
	
	@Autowired
	private EduinstDao eduinstDao ;
	
	@Autowired
	private CityDao cityDao ;
	
	@Autowired
	private QuotaSituationDao situationDao ;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		setMapper( instSituationDao ) ; 
	}

	/**
	 * 根据采集项名称查询学校常态数据表 .
	 * 
	 * @param collectionId
	 * @param param
	 * @return
	 */
	public Map<String,Object> findSchoolCollectionWithCollection( 
			int collectionId , int schoolType , Integer cityId , Integer countyId ){
		Reporting reporting = reportingDao.getPresent(Reporting.class) ;
		if( reporting == null )
			throw new BusinessException("填报已关闭!") ;
		
		Map<String,Object> param = new HashMap<String,Object>() , result = new HashMap<String,Object>() ; 
		param.put("reportingId", reporting.getId() ) ;
		
		Collection collection = collectionDao.findBeanBy(
				"id", collectionId, Collection.class , "id" , "title" , "valueType" ) ;
		if( collection == null )
			throw new BusinessException("采集项不存在!") ;
		param.put("collectionId", collectionId ) ;
		result.put("title", collection.getTitle() ) ;
		
		List<SchoolType> types = schoolTypeDao.findBeanListBy("type", schoolType, SchoolType.class , "id") ;
		param.put("schoolType", 0 );
		if( types != null && !types.isEmpty() )
			param.put("schoolType", JoinUtil.fieldsValue(types, "id") );
		
		City city = null ;
		if( countyId != null && (city = cityDao.findBeanBy("id", countyId, City.class ) ) != null ){
			param.put("countyId", countyId ) ;
			result.put("cityName", city.getName() ) ;
		}else if( cityId != null && (city = cityDao.findBeanBy("id", cityId, City.class ) ) != null ){
			param.put("cityId", cityId ) ;
			result.put("cityName", city.getName() ) ;
			result.put("cityNameExtra", "(区、县)") ;
		}else{
			result.put("cityName", "江苏省" ) ;
		}
		result.put("dataList", instSituationDao.findSchoolCollectionReportWithCollection(
				param, InstQuotaSituationReport.class ) ) ;
		return result ;
	}
	
	/**
	 * 学校标准化建设监测达成情况表 .
	 * 
	 * @param schoolId
	 * @return
	 */
	public Map<String,Object> findSchoolSituationReport( int schoolId ){
		Reporting reporting = reportingDao.getPresent( Reporting.class ) ;
		if( reporting == null )
			throw new BusinessException("填报已关闭!") ;
		School school = schoolDao.findBeanBy("id", schoolId, School.class ) ;
		if( school == null )
			throw new BusinessException("学校不存在!") ;
		SchoolType schoolType = schoolTypeDao.findBeanBy("id", school.getType() , SchoolType.class ) ;
		if( schoolType == null )
			throw new BusinessException("无效的学校类型!") ;
		
		Map<String,Object> result = new HashMap<String,Object>() ;
		result.put("schoolName", school.getName() ) ;
		List<InstQuotaSituationReport> dataList = null ;
		if( schoolType.getType() == 1 || schoolType.getType() == 3 )//小学
			dataList = findInstSituationReport( reporting.getId() , school.getId() , 
					Arrays.asList( InstType.SCHOOL.name() ) , Arrays.asList( SchoolTypeEnum.PRIMARY.name() , SchoolTypeEnum.SCHOOL.name()  ))  ;
		else if( schoolType.getType() == 2 || schoolType.getType() == 3 )//初中
			dataList = findInstSituationReport( reporting.getId() , school.getId() , 
					Arrays.asList( InstType.SCHOOL.name() ) , Arrays.asList( SchoolTypeEnum.MIDDLE.name() , SchoolTypeEnum.SCHOOL.name()  ))  ;
		result.put( schoolType.getType() == 1 ? "primarySchoolData" : "middleSchoolData"
			,  dataList == null ?new ArrayList<InstQuotaSituationReport>(): dataList ) ;
		return result ;
	}
	
	/**
	 * 教育局标准化建设监测达成情况表 .
	 * 
	 * @param eduinstId
	 * @return
	 */
	public Map<String,Object> findEduinstSituationReport( int eduinstId , int schoolType ){
		if(! Arrays.asList( 1,2 ).contains( schoolType ) )
			throw new BusinessException("学校类型不正确!") ;
		List<String> schoolTypes = Arrays.asList( SchoolTypeEnum.SCHOOL.name() , 
				schoolType == 1 ? SchoolTypeEnum.PRIMARY.name() : SchoolTypeEnum.MIDDLE.name() ) ;
		Reporting reporting = reportingDao.getPresent( Reporting.class ) ;
		if( reporting == null )
			throw new BusinessException("填报已关闭!") ;
		Eduinst eduinst = eduinstDao.findBeanBy("id", eduinstId, Eduinst.class ) ;
		if( eduinst == null )
			throw new BusinessException("教育局不存在!") ; 
			
		Map<String,Object> result = new HashMap<String,Object>() ;
		result.put("eduinstName", eduinst.getName() ) ;
		EduinstType eduinstType = EduinstType.parse( eduinst.getType() ) ;
		List<InstQuotaSituationReport> dataList = null ;
		if( eduinstType == EduinstType.COUNTY )//县
			dataList = findInstSituationReport( reporting.getId() , eduinst.getId() ,  
					Arrays.asList( InstType.EDUINST.name() , InstType.COUNTY_EDUINST.name() ) , schoolTypes )  ;
		else if(  eduinstType == EduinstType.CITY  )//市
			dataList =  findInstSituationReport( reporting.getId() , eduinst.getId() ,  
					Arrays.asList( InstType.EDUINST.name() , InstType.CITY_EDUINST.name() ) , schoolTypes )  ;
		else if(  eduinstType == EduinstType.PROVINCE  )//省
			dataList =  findInstSituationReport( reporting.getId() , eduinst.getId() ,  
					Arrays.asList( InstType.EDUINST.name() , InstType.PROVINCE_EDUINST.name() ) , schoolTypes )  ;
		result.put( schoolType == 1 ? "primarySchoolData" : "middleSchoolData" 
			, dataList == null ? new ArrayList<InstQuotaSituationReport>() : dataList ) ;
		return result ;
	}
	
	private List<InstQuotaSituationReport> findInstSituationReport( int reportingId ,  int instId ,
			List<String> instType , List<String> schoolType ){
		if( instType == null || instType.isEmpty() || schoolType == null || schoolType.isEmpty() )
			return null ;
		Map<String,Object> param = new HashMap<String,Object>() ;
		param.put("instType", instType ) ;
		param.put("schoolType", schoolType ) ;
		List<QuotaSituation> situationList = situationDao.findBeanList( param , QuotaSituation.class );
		if( situationList == null || situationList.isEmpty() )
			return null ;
		List<InstQuotaSituationReport> dataList = new ArrayList<InstQuotaSituationReport>() ;
		for( QuotaSituation situation : situationList ){
			InstQuotaSituationReport data = new InstQuotaSituationReport() ;
			data.setSituationName( situation.getName() );
			data.setStandardNote( situation.getStandardNote() );
			data.setSituationId( situation.getId() );
			dataList.add( data ) ;
		}
		param.put("instId", instId ) ;
		param.put("reportingId", reportingId ) ;
		List<InstQuotaSituation> instSituationList = instSituationDao.findBeanList( param , InstQuotaSituation.class ) ;
		JoinUtil.join( dataList , new String[]{"calculatedValue","eligibleValue"} ,
				"situationId" , instSituationList ,  new String[]{"calculatedValue","eligibleValue"} , "situationId");
		
		return dataList ;
	}
	
	/**
	 * 查询区县学校监测情况 .
	 * 
	 * @param countyId
	 * @param reportingId
	 * @return
	 */
	public Map<String,Object> findEduinstSchoolSituationReport( int countyId , int reportingId ){
		return null;
	}
	
	/**
	 * 查询市学生监测情况 .
	 * 
	 * @param cityId
	 * @param reportingId
	 * @return
	 */
	public Map<String,Object> findCountyOfCitySituationReport( int cityId , int reportingId ){
		return null ;
	}
	
	/**
	 * 查询所有通过的学校列表 .
	 * 
	 * @param reportingId
	 * @return
	 */
	public Map<String,Object> findPassAllSchool( int reportingId ){
		return null ;
	}
	
}
