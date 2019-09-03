package com.newproj.report.eduinst.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.newproj.core.beans.MapParam;
import com.newproj.core.exception.BusinessException;
import com.newproj.core.page.PageParam;
import com.newproj.core.page.RemotePage;
import com.newproj.core.rest.annotation.Api;
import com.newproj.core.rest.annotation.Get;
import com.newproj.core.rest.annotation.Put;
import com.newproj.core.rest.support.ParamModal;
import com.newproj.core.rest.support.RestActionSupporter;
import com.newproj.core.util.JoinUtil;
import com.newproj.core.util.StringUtil;
import com.newproj.core.util.VelocityUtil;
import com.newproj.report.collection.dto.Collection;
import com.newproj.report.context.Subject;
import com.newproj.report.context.trans.TransContext;
import com.newproj.report.eduinst.EduinstConstant.EduinstType;
import com.newproj.report.eduinst.dto.Eduinst;
import com.newproj.report.eduinst.dto.EduinstAuditing;
import com.newproj.report.eduinst.dto.EduinstAuditingParam;
import com.newproj.report.eduinst.dto.EduinstCollection;
import com.newproj.report.eduinst.dto.EduinstProcess;
import com.newproj.report.eduinst.dto.EduinstReportingData;
import com.newproj.report.eduinst.service.EduinstAuditingService;
import com.newproj.report.eduinst.service.EduinstCollectionService;
import com.newproj.report.eduinst.service.EduinstService;
import com.newproj.report.quota.dto.Quota;
import com.newproj.report.quota.service.QuotaService;
import com.newproj.report.reporting.dto.Reporting;
import com.newproj.report.reporting.service.ReportingService;
import com.newproj.report.sys.constant.RoleConstant.RoleCode;
import com.newproj.report.sys.constant.UserConstant.InstType;
@Api("/eduinstAuditing")
public class EduinstAuditingAction extends RestActionSupporter{

	@Autowired
	private EduinstAuditingService auditingService ;
	
	@Autowired
	private TransContext trans ;
	
	@Autowired
	private ReportingService reportingService ;
	
	@Autowired
	private EduinstCollectionService eduinstCollectionService ;
	
	@Autowired
	private QuotaService quotaService ;
	
	
	@Autowired
	private EduinstService eduinstService ;
	
	@Get("")
	public String findAuditing( ParamModal modal ){
		
		//审核列表查询
		Map<String,Object> param = modal.getParam("name" , "eduinstCode","status" , "processStatus") ;
		
		//市教育局获取所属区域县教育局填报列表
		Eduinst eduinst = null ;
		if( InstType.EDUINST != InstType.parse( Subject.getUser().getInstType() ) 
				|| ( eduinst = eduinstService.findBy("id", Subject.getUser().getInstId() , Eduinst.class ) ) == null
				|| EduinstType.parse( eduinst.getType() ) != EduinstType.CITY ){
			return success( null ) ;
		}
		RoleCode role = RoleCode.parse( Subject.getUser().getRoleCode() ) ;
		if( role == null || 
				!Arrays.asList( RoleCode.CITY , RoleCode.CITY_SUBACC ).contains( role ) )
			return success( null ) ;
		
		param.put("cityId", eduinst.getCityId() ) ;
		param.put("reportingId" , verifyReporting().getId() ) ;
		param.put("userId", Subject.getUserId() ) ;
		
		RemotePage<EduinstAuditing> pageData = null ;
		if( role == RoleCode.CITY ){
			pageData = auditingService.findAuditing(param, modal.getPageParam() ) ;
			trans.transDict("EDUINST_PROCESS_STATUS", pageData , "processStatus", "processStatusName" , "0") ;
		}else if( role == RoleCode.CITY_SUBACC ){
			pageData =  auditingService.findSubaccAuditing(param, modal.getPageParam() ) ;
			trans.transDict("AUDIT_STATUS", pageData, "status", "statusName" , "0") ;
		}
		if( pageData == null || pageData.isEmpty() )
			return success( null ) ;
		
		trans.transCity(pageData , Arrays.asList( "cityId","countyId" ), Arrays.asList("cityName","countyName") );
		return success( pageData , pageData.getTotal() ) ;
	}
	
	/**
	 * 获取填报数据列表 .
	 * 
	 * @param modal
	 * @return
	 */
	@Get("/reportingData")
	public String findReportingData( ParamModal modal ){
		Map<String,Object> param = modal.getParam("eduinstName" , "code" , "processStatus") ;
		//数据权限控制
		RoleCode code = RoleCode.parse( Subject.getUser().getRoleCode() ) ;
		Reporting reporting = reportingService.getPresent() ;
		if(reporting == null || code == null || !Arrays.asList( RoleCode.SYS , RoleCode.PROVINCE , 
				RoleCode.PROVINCE_SUBACC , RoleCode.CITY , RoleCode.CITY_SUBACC  ) .contains( code ) )
			return success( null ) ;
		Eduinst eduinst = eduinstService.findBy( "id", Subject.getUser().getInstId() , Eduinst.class ) ;
		if( eduinst != null ){
			if(eduinst.getCityId()>0) {
				param.put("cityId", eduinst.getCityId() ) ;
			}
			
			if(eduinst.getCountyId()>0) {
				param.put("countyId" , eduinst.getCountyId() ) ;
			}
		
		}
		//查询填报数据
		param.put("reportingId", reporting.getId() ) ;
		RemotePage<EduinstAuditing> pageData = auditingService.findReporting( param , modal.getPageParam() ) ;
		
		trans.transDict("EDUINST_PROCESS_STATUS", pageData , "processStatus", "processStatusName" , "0")
		.transCity(pageData , Arrays.asList( "cityId","countyId" ), Arrays.asList("cityName","countyName"));
		return success( pageData , pageData == null ? 0 : pageData.getTotal() ) ;
	}
	
	/**
	 * 导出填报数据。
	 * 
	 * @param eduinstId
	 * @param request
	 * @param response
	 */
	@Get("/reportingData/{eduinstId}/export")
	public void exportReportingData(  @PathVariable("eduinstId") int eduinstId , 
			HttpServletRequest request , HttpServletResponse response  ){
		if( eduinstId == 0 ){
			eduinstId = Subject.getUser().getInstId() ;
		}
		Eduinst eduinst = null ;
		List<EduinstReportingData> dataList = new  ArrayList<EduinstReportingData>();
		Reporting reporting = null ;
		if( (eduinst = eduinstService.findBy("id", eduinstId , Eduinst.class )  ) != null && 
				( reporting = reportingService.getPresent() ) != null ){
			dataList = auditingService.exportReportingData(eduinstId, reporting.getId() ) ;
		}
		Context context = new VelocityContext() ;
		context.put("data", dataList ) ;
		context.put("instName", eduinst.getName() ) ;
		try {
			VelocityUtil.download(String.format("%s填报数据.doc", eduinst.getName() ),
					"/vm/reporting.vm", context, request, response);
		} catch (IOException e) {
			logger.error("导出填报数据出错!" , e );
		}
	}
	
	/**
	 * 获取填报数据。
	 * 
	 * @param eduinstId
	 * @return
	 */
	@Get("/{eduinstId}/reportingData")
	public String findReportingData( @PathVariable("eduinstId") int eduinstId ){
		return success( new MapParam<String,Object>().push("quota" , getReportingData( eduinstId ) ) ) ;
	}
	
	private List<Quota> getReportingData( int eduinstId ){
		Reporting reporting = null ;
		if( ( reporting = reportingService.getPresent() )== null  )
			return null ;
		//获取教育局所有填报项
		List<Collection> eduinstCollections = eduinstCollectionService.findEduinstCollection(
				eduinstId , RoleCode.COUNTY.name() , reporting.getId() ) ;
		if( eduinstCollections == null || eduinstCollections.isEmpty() )
			return null ;
		
		//查询教育局填报数据
		List<EduinstCollection> eduinstCntCollections = eduinstCollectionService.findList(
				new MapParam<String,Object>().push("reportId" , reporting.getId() )
				.push("instId" , eduinstId ) , null , EduinstCollection.class ) ;
		JoinUtil.join(eduinstCollections, new String[]{"content"}, "id", eduinstCntCollections  , 
				new String[]{"content"}, "collectionId");
		
		//结构化结果返回
		Map<String,Object> param = new HashMap<String,Object>() ;
		param.put("id", JoinUtil.fieldsValue( eduinstCollections, "quotaId") ) ;
		List<Quota> quota2List = quotaService.findList( param , new PageParam().orderBy("rank", "ASC") , Quota.class ) ;
		if( quota2List == null || quota2List.isEmpty() )
			return null ;
		Map<Integer,List<Collection>> tempMap = new HashMap<Integer,List<Collection>>() ;
		for( Collection collection : eduinstCollections ){
			if( !tempMap.containsKey( collection.getQuotaId() ) )
				tempMap.put(collection.getQuotaId() , new ArrayList<Collection>() ) ;
			tempMap.get( collection.getQuotaId() ).add( collection ) ;
		}
		for( Quota quota : quota2List ){
			quota.setCollections( tempMap.get( quota.getId() ) );
		}
		return quota2List;
	}
	
	
	/**
	 * 获取填报详情
	 * 
	 * @param schoolId
	 * @return
	 */
	@Get("/{eduinstId}/reporting")
	public String getReporting( @PathVariable("eduinstId") int eduinstId  ){
		//是管理员、子账号
		RoleCode roleCode = null ;
		if( !Arrays.asList( RoleCode.CITY ,RoleCode.CITY_SUBACC )
					.contains( ( roleCode = RoleCode.parse( Subject.getUser().getRoleCode() ) ) ) )
			return success( null ) ;
		
		Reporting reporting = null ;
		if( ( reporting = reportingService.getPresent() )== null  )
			return success( null ) ;
		
		//获取教育局所有填报项
		List<Collection> eduinstCollections = eduinstCollectionService.findEduinstCollection(
				eduinstId , RoleCode.COUNTY.name() , reporting.getId() ) ;
		if( eduinstCollections == null || eduinstCollections.isEmpty() )
			return success( null ) ;
		
		//如果当前账号为子账号,获取当前子账号审核指标
		List<Integer> quotaList = new ArrayList<Integer>() ;
		if( roleCode == RoleCode.CITY_SUBACC ){
			quotaList = quotaService.findSubaccQuota( Subject.getUserId() ) ;
			if( quotaList == null || quotaList.isEmpty() )
				return success( null ) ;
		}
		
		List<Collection> auditCollectionList = new ArrayList<Collection>() ;
		for( Collection item : eduinstCollections ){
			if( roleCode == RoleCode.CITY || quotaList.contains( item.getQuotaId() ) )
				auditCollectionList.add( item ) ;
		}
		//查询二级指标
		Map<String,Object> param = new HashMap<String,Object>() ;
		param.put("id", JoinUtil.fieldsValue(auditCollectionList, "quotaId") ) ;
		List<Quota> quota2List = quotaService.findList( param ,
				new PageParam().orderBy("rank", "ASC") , Quota.class ) ;
		if( quota2List == null || quota2List.isEmpty() )
			return success( null ) ;
		
		//查询教育局填报数据
		List<EduinstCollection> eduinstCntCollections = eduinstCollectionService.findList(
				new MapParam<String,Object>().push("reportId" , reporting.getId() )
				.push("instId" , eduinstId )
				.push("collectionId" , JoinUtil.fieldsValue( auditCollectionList , "id") ), null , EduinstCollection.class ) ;
		JoinUtil.join(auditCollectionList, new String[]{"content"}, "id", eduinstCntCollections  , 
				new String[]{"content"}, "collectionId");
		
		//结构化结果返回
		Map<Integer,List<Collection>> tempMap = new HashMap<Integer,List<Collection>>() ;
		for( Collection collection : auditCollectionList ){
			if( !tempMap.containsKey( collection.getQuotaId() ) )
				tempMap.put(collection.getQuotaId() , new ArrayList<Collection>() ) ;
			tempMap.get( collection.getQuotaId() ).add( collection ) ;
		}
		for( Quota quota : quota2List ){
			quota.setCollections( tempMap.get( quota.getId() ) );
		}
		
		EduinstProcess process = eduinstCollectionService.getProcess(eduinstId, reporting.getId() ) ;
		
		Map<String,Object> result = new HashMap<String,Object>() ;
		result.put("quota", quota2List ) ;
		//是否可审核
		result.put("audit", process != null && 
				( Arrays.asList(1,2).contains( process.getStatus() ) && roleCode == RoleCode.CITY_SUBACC ) 
				|| Arrays.asList(1,2,3).contains( process.getStatus() ) && roleCode == RoleCode.CITY ) ;
		//是否可提交审核
		//result.put("submit", roleCode == RoleCode.CITY && Arrays.asList(3).contains( process.getStatus() )  ) ;
		result.put("submit", false ) ;
		return success( result ) ;
	}
	
	private List<Integer> getUserAuditCollections( int eduinstId ){
		Reporting reporting = verifyReporting() ;
		
		//获取当前用户角色类型
		RoleCode role = RoleCode.parse( Subject.getUser().getRoleCode() ) ;
		if( !Arrays.asList( RoleCode.CITY , RoleCode.CITY_SUBACC ).contains( role ) )
			throw new BusinessException("当前用户无权审核!") ;
		Integer userId = role == RoleCode.CITY ? null : Subject.getUserId() ;
		
		//获取被审核的教育局所有采集项
		List<Collection> eduinstCollection = eduinstCollectionService
				.findEduinstCollection(eduinstId, "COUNTY" , reporting.getId() ) ;
		if( eduinstCollection == null || eduinstCollection.isEmpty() )
			return null ;
		
		//如果当前账号为子账号,获取当前子账号审核指标
		List<Integer> quotaList = new ArrayList<Integer>() ;
		if( userId != null && 
				(quotaList = quotaService.findSubaccQuota( userId ) ) == null )
			return null ;

		List<Integer> result = new ArrayList<Integer>() ;
		for( Collection collection : eduinstCollection )
			if( userId == null || quotaList.contains( collection.getQuotaId() ) )
				result.add( collection.getId() ) ;
		
		return result ;
	}
	
	/**
	 * 填报审核
	 * 
	 * @param schoolId
	 * @param param
	 * @return
	 */
	@Put("/{eduinstId}/audit")
	public String audit( @PathVariable("eduinstId") int eduinstId , @RequestBody EduinstAuditingParam param ){
		RoleCode role = RoleCode.parse( Subject.getUser().getRoleCode() ) ;
		if( role == null ||
				!Arrays.asList( RoleCode.CITY , RoleCode.CITY_SUBACC ).contains( role ) )
			throw new BusinessException("当前账号无法审核县教育局填报!") ;
		
		param.setEduinstId(eduinstId);
		param.setUserId( Subject.getUserId() );
		param.setCollectionsId( StringUtil
				.join( getUserAuditCollections( eduinstId ), "," ) );
		if( param.getCollectionsId() == null )
			throw new BusinessException("当前账号没有分配指标,无法审核!") ;
		
		param.setReportId( verifyReporting().getId() );
		param.setStatus( param.getStatus() != null && param.getStatus() == 1 ? 1 : 0 );
		if( param.getStatus() == 0 && StringUtil.isEmpty( param.getAuditNote() ) )
			throw new BusinessException("返回修改请填写修改意见!") ;
		auditingService.audit(  param , role == RoleCode.CITY ) ;
		
		return success( null ) ;
	}
	
	private Reporting verifyReporting(){
		Reporting reporting = null ;
		if( ( reporting = reportingService.getPresent() )== null )
			throw new BusinessException("填报已关闭!") ;
		return reporting ;
	}
	
	/**
	 * 提交审核结果
	 * 
	 * @param schoolId
	 * @return
	 */
	@Put("/{eduinstId}/submitAuditing")
	public String submitToAudit(  @PathVariable("eduinstId") int eduinstId ){
		auditingService.submitAuditing( eduinstId , verifyReporting().getId() ) ;
		return success( null ) ;
	}
}
