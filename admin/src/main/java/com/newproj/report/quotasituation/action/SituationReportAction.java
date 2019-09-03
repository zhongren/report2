package com.newproj.report.quotasituation.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.newproj.core.rest.annotation.Api;
import com.newproj.core.rest.annotation.Get;
import com.newproj.core.rest.support.RestActionSupporter;
import com.newproj.core.util.VMUtil;
import com.newproj.core.util.VelocityUtil;
import com.newproj.report.context.Subject;
import com.newproj.report.eduinst.dto.Eduinst;
import com.newproj.report.eduinst.service.EduinstService;
import com.newproj.report.quotasituation.service.SituationReportService;
import com.newproj.report.sys.constant.UserConstant.InstType;

/**
 * 指标监测点统计报表导出 .
 * 
 * @author 10147
 *
 */
@Api("situationReport")
public class SituationReportAction extends RestActionSupporter{
	
	@Autowired
	private SituationReportService situationReportService ;
	
	@Autowired
	private EduinstService eduinstService ;
	
	/**
	 * 导出学校标准化建设达标情况 .
	 * 
	 * @param schoolId
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@Get("/school/{schoolId}")
	public void exportSchoolSituationReport( @PathVariable("schoolId") int schoolId ,
			HttpServletRequest request , HttpServletResponse response ) throws IOException{
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		Map<String,Object> result = null ;
		try{
			result = situationReportService.findSchoolSituationReport(schoolId) ;
			if( result == null ){
				output( response , "当前学校无统计数据!") ;
				return ;
			}
		}catch(Exception e){
			output( response ,"请求出错:"+ e.getMessage() ) ;
			return ;
		}
		Context context = new VelocityContext() ;
		context.put("util", new VMUtil()  ) ;
		for( Map.Entry<String, Object> entry : result.entrySet() )
			context.put( entry.getKey() , entry.getValue() ) ;
		try{
			VelocityUtil.download(String.format("%s学校标准化建设监测点达成情况表.doc", result.get("schoolName") ), 
					"/vm/school-situation-report.vm", context, request, response);
		}catch( Exception e){
			output( response ,"文件导出失败!" ) ;
			return ;
		}
	}
	
	
	
	/**
	 * 根据采集项导出学校(小学1，初中2)数据 .
	 * 
	 * @param collectionId
	 * @param cityId
	 * @param countyId
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@Get("/schoolWithCollection/{collectionId}/{schoolType}")
	public void exportSchoolCollectionWithCollectionIdReport( @PathVariable("collectionId") int collectionId ,
			@PathVariable("schoolType") int schoolType ,
			@RequestParam( required = false , value = "cityId" ) Integer cityId ,
			@RequestParam( required = false , value = "countyId" ) Integer countyId ,
			HttpServletRequest request , HttpServletResponse response ) throws IOException{
		
		InstType instType = InstType.parse( Subject.getUser().getInstType() ) ;
		if( instType == InstType.SCHOOL )
			response.getWriter().write( "当前用户无法导出此项!" );
		else if( instType == InstType.EDUINST ){
			Eduinst eduinst = eduinstService.findBy("id", Subject.getUser().getInstId() , Eduinst.class ) ;
			if( eduinst == null )
				response.getWriter().write( "当前用户无法导出此项!" );
			cityId = eduinst.getCityId() != null && eduinst.getCityId() > 0 ? eduinst.getCityId() : cityId ;
			countyId = eduinst.getCountyId() != null && eduinst.getCountyId() > 0 ? eduinst.getCountyId() : countyId ;
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		Map<String,Object> result = null ;
		try{
			result = situationReportService.findSchoolCollectionWithCollection(collectionId, schoolType , cityId, countyId) ;
		}catch(Exception e){
			output( response , e.getMessage() ) ;
			return ;
		}
		Context context = new VelocityContext() ;
		context.put("util", new VMUtil()  ) ;
		for( Map.Entry<String, Object> entry : result.entrySet() )
			context.put( entry.getKey() , entry.getValue() ) ;
		try{
			VelocityUtil.download(String.format("%s义务教育学校标准化建设监测常态数据表.doc", result.get("cityName")  ), 
					"/vm/school-collection-report.vm", context, request, response);
		}catch( Exception e){
			output( response , "文件导出失败!") ;
			return ;
		}
	}
	
	/**
	 * 导出教育局学校（小学1，初中2）填报监测情况
	 * 
	 * @param eduinstId
	 * @param schoolType
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@Get("/eduinst/{schoolType}")
	public void exportEduinstSituationReport(  @PathVariable("schoolType") int schoolType ,
			@RequestParam( required = false , value="cityId") Integer cityId ,
			@RequestParam( required = false , value="countyId") Integer countyId  ,
			HttpServletRequest request , HttpServletResponse response ) throws IOException{
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		Map<String,Object> result = null ;
		
		Map<String,Object> param = new HashMap<String,Object>() ;
		param.put("cityId" , cityId == null ? 0 : cityId) ;
		param.put("countyId" , countyId == null ? 0 : countyId) ;
		Eduinst eduinst = eduinstService.findOne( param , Eduinst.class , "id") ;
		try{
			result = situationReportService.findEduinstSituationReport( eduinst == null ? 0 : eduinst.getId() , schoolType ) ;
			if( result == null || result.isEmpty() )
				output( response , "当前教育局无统计数据!" ) ;
		}catch(Exception e){
			output( response , e.getMessage() ) ;
			return ;
		}
		Context context = new VelocityContext() ;
		context.put("util", new VMUtil()  ) ;
		for( Map.Entry<String, Object> entry : result.entrySet() )
			context.put( entry.getKey() , entry.getValue() ) ;
		try{
			VelocityUtil.download(String.format("%s义务教育学校标准化建设监测点达成情况表.doc", result.get("eduinstName") ), 
					"/vm/eduinst-situation-report.vm", context, request, response);
		}catch( Exception e){
			output( response , "文件导出失败!") ;
			return ;
		}
	}
	
	
	/**
	 * 导出区县学校监测情况 .
	 * 
	 * @param countyId
	 */
	@Get("/countySchool/{countyId}")
	public void exportCountySchoolSituation( @PathVariable("countyId") int countyId ){
		
	}
	
	/**
	 * 导出市学校监测情况 .
	 * 
	 * @param cityId
	 */
	@Get("/countyOfCitySituation/{cityId}")
	public void exportCountyOfCitySituation( @PathVariable("cityId") int cityId ){
		
	}
	
	/**
	 * 导出所有监测项通过的学校列表 .
	 */
	@Get("/passAllSchool")
	public void exportPassAllSchool( ){
		
	}
}
