package com.newproj.report.school.action;

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
import org.json.JSONArray;
import org.json.JSONObject;
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
import com.newproj.report.eduinst.dto.Eduinst;
import com.newproj.report.eduinst.service.EduinstService;
import com.newproj.report.export.bean.SchoolClassesNumBean;
import com.newproj.report.quota.dto.Quota;
import com.newproj.report.quota.service.QuotaService;
import com.newproj.report.reporting.dto.Reporting;
import com.newproj.report.reporting.service.ReportingService;
import com.newproj.report.school.dto.School;
import com.newproj.report.school.dto.SchoolAuditing;
import com.newproj.report.school.dto.SchoolAuditingParam;
import com.newproj.report.school.dto.SchoolCollection;
import com.newproj.report.school.dto.SchoolProcess;
import com.newproj.report.school.dto.SchoolReportingData;
import com.newproj.report.school.service.SchoolAuditingService;
import com.newproj.report.school.service.SchoolClassesNumsService;
import com.newproj.report.school.service.SchoolCollectionService;
import com.newproj.report.school.service.SchoolService;
import com.newproj.report.sys.constant.RoleConstant.RoleCode;
import com.newproj.report.sys.constant.UserConstant.InstType;
import com.newproj.report.sys.dto.SysRole;
import com.newproj.report.sys.service.SysRoleService;
@Api("schoolAuditing")
public class SchoolAuditingAction extends RestActionSupporter{
	
	@Autowired
	private SchoolClassesNumsService schoolClassesNumsService;
	
	@Autowired
	private SchoolService schoolService ;
	
	@Autowired
	private SchoolCollectionService schoolCollectionService ;
	
	@Autowired
	private ReportingService reportingService ;
	
	@Autowired
	private SchoolAuditingService auditingService ;
	
	@Autowired
	private QuotaService quotaService ;
	
	
	@Autowired
	private TransContext trans ;
	
	@Autowired
	private SysRoleService roleService ;
	
	@Autowired
	private EduinstService eduinstService ;
	
	
	@Get("")
	public String findAuditing( ParamModal modal ){
		//教育局账号类型
		InstType instType = InstType.parse( Subject.getUser().getInstType() ) ;
		RoleCode role = RoleCode.parse( Subject.getUser().getRoleCode() ) ;
		if( instType != InstType.EDUINST || !Arrays.asList(
				RoleCode.COUNTY , RoleCode.COUNTY_SUBACC , RoleCode.CITY ).contains( role ) )
			return success( null ) ;
	
		//学校审核列表查询
		Map<String,Object> param = modal.getParam("name" , "schoolCode","auditStatus" , "processStatus") ;
		Eduinst eduinst = eduinstService.findBy("id", Subject.getUser().getInstId(), Eduinst.class ) ;
		if( eduinst == null ) 
			return success( null ) ;
		param.put("cityId", eduinst.getCityId() != null && eduinst.getCityId() > 0 ? eduinst.getCityId() : null ) ;
		param.put("countyId" , eduinst.getCountyId() != null && eduinst.getCountyId()> 0 ? eduinst.getCountyId() : null ) ;
		param.put("reportingId" , verifyReporting().getId() ) ;
		param.put("userId", Subject.getUserId() ) ;
		RemotePage<SchoolAuditing> pageData = null ;
		if( role == RoleCode.COUNTY_SUBACC ){
			pageData = auditingService.findSubaccAuditing(param, modal.getPageParam() ) ;
			trans.transDict("AUDIT_STATUS", pageData, "status", "statusName" , "0") ;
		}else if( role == RoleCode.COUNTY || role == RoleCode.CITY ){
			pageData = auditingService.findAuditing(param, modal.getPageParam() ) ;
			trans.transDict("SCHOOL_PROCESS_STATUS", pageData , "processStatus", "processStatusName" , "0") ;
		}
			
		if( pageData == null || pageData.isEmpty() )
			return success( null ) ;
		
		//学校类型名称
		JoinUtil.join( pageData , new String[]{"typeName"}, "type",
				schoolService.findSchoolType( new MapParam<String,Object>()
						.push("id" , JoinUtil.fieldsValue( pageData, "type") ), null ), new String[]{"name"}, "id");
		JoinUtil.join( pageData, new String[]{"eduinstName"}, "eduinstId",
				eduinstService.findListBy("id", JoinUtil.fieldsValue(pageData, "eduinstId"), Eduinst.class ,  "id" , "name"),
				new String[]{"name"}, "id");
		trans.transDict("REGION_TYPE", pageData, "regionType", "regionTypeName" );
		return success( pageData , pageData.getTotal() ) ;
	}
	
	/**
	 * 获取填报数据进度列表 .
	 * 
	 * @param modal
	 * @return
	 */
	@Get("/reportingData")
	public String findReportingData( ParamModal modal ){
		Map<String,Object> param = modal.getParam("schoolName" , "code" , "processStatus") ;
		//数据权限控制
		InstType instType = InstType.parse( Subject.getUser().getInstType() ) ;
		if( instType == null || instType == InstType.SCHOOL )
			return success( null ) ;
		Reporting reporting = reportingService.getPresent() ;
		if(reporting == null  )
			return success( null ) ;
		Eduinst eduinst = null ;
		if( instType == InstType.EDUINST && (eduinst = eduinstService.findBy(
				"id", Subject.getUser().getInstId() , Eduinst.class ) ) != null ){
			if(eduinst.getCityId() > 0 ) {
				param.put("cityId", eduinst.getCityId() ) ;
			}
			if(eduinst.getCountyId() > 0 ) {
				param.put("countyId" , eduinst.getCountyId() ) ;
			}
			
		}
		
		//查询填报数据
		param.put("reportingId", reporting.getId() ) ;
		RemotePage<SchoolAuditing> pageData = auditingService.findReporting( param , modal.getPageParam() ) ;
		if( pageData == null || pageData.isEmpty() )
			return success( null ) ;
		JoinUtil.join( pageData, new String[]{"eduinstName"}, "eduinstId" , eduinstService.findListBy("id", 
				JoinUtil.fieldsValue(pageData, "eduinstId"), Eduinst.class , "id" , "name"), new String[]{"name"}, "id");
		trans.transDict("SCHOOL_PROCESS_STATUS", pageData , "processStatus", "processStatusName" , "0")
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
	@Get("/reportingData/{schoolId}/export")
	public void findEduinstReportingData(  @PathVariable("schoolId") int schoolId , 
			HttpServletRequest request , HttpServletResponse response  ){
		if( schoolId == 0 ) {
			schoolId = Subject.getUser().getInstId() ;
		}
		School school = null ;
		List<SchoolReportingData> dataList = new  ArrayList<SchoolReportingData>();
		Reporting reporting = null ;
		if( (school = schoolService.findBy("id", schoolId , School.class )  ) != null && 
				( reporting = reportingService.getPresent() ) != null ){
			dataList = auditingService.exportReportingData(schoolId,school.getType() , reporting.getId() ) ;
		}
		handleExportResult( dataList ) ; //处理导出结果
		
		List<SchoolClassesNumBean> classes = schoolClassesNumsService.getClasses(schoolId, reporting.getId());
		Context context = new VelocityContext() ;
		context.put("data", dataList ) ;
		context.put("instName", school.getName() ) ;
		context.put("dataClasses", classes);
		try {
			VelocityUtil.download(String.format("%s填报数据.doc", school.getName() ),
					"/vm/reporting.vm", context, request, response);
		} catch (IOException e) {
			logger.error("导出填报数据出错!" , e );
		}
	}
	
	private void handleExportResult( List<SchoolReportingData> dataList ){
		if( dataList == null || dataList.isEmpty() )
			return ;
		for( SchoolReportingData data : dataList ){
			if(StringUtil.isEmpty(data.getContent())) {
				continue ;
			}
			//导出Group类型,暂时取第一个文本类型的展示
			if( data.getValueType().toLowerCase().equals("group") ){
				JSONArray options = null ;
				JSONObject content = null ;
				try{
					content = new JSONObject( data.getContent() ) ;
					options = new JSONArray( data.getValueOpt() ) ;
					for( int i = 0 ; i < options.length() ; i ++ ){
						if( !Arrays.asList("text","number","checkbox","radio")
								.contains( options.getJSONObject(i).getString("valueType").toLowerCase() ) )
							continue ;
						data.setContent( content.getJSONObject( 
								options.getJSONObject(i).getString("name") ).getString("value") );
						break ;
					}
				}catch(Exception e){
					data.setContent("");
				}
				continue ;
			}
			if( Arrays.asList("text","number","checkbox","radio").contains( data.getValueType().toLowerCase() ) )
				continue ;
			data.setContent("已上传"); //只允许导出文本类型数据
		}
	}
	
	/**
	 * 查看填报原数据
	 * 
	 * @param schoolId
	 * @return
	 */
	@Get("/{schoolId}/reportinData")
	public String getReportingDetail( @PathVariable("schoolId") int schoolId ){
		//获取学校类型所有填报项
		School school = schoolService.findBy("id", schoolId, School.class ) ;
		Reporting reporting = reportingService.getPresent() ;
		if( school == null || reporting == null )
			return null ;
		List<Collection> collections =  schoolCollectionService.findSchoolTypeCollection(
				school.getType() , reporting.getId() ) ;
		if( collections == null || collections.isEmpty() )
			return success( null ) ;
		//查询二级指标
		Map<String,Object> param = new HashMap<String,Object>() ;
		param.put("id", JoinUtil.fieldsValue( collections , "quotaId" ) ) ;
		List<Quota> quota2List = quotaService.findList( param ,
				new PageParam().orderBy("rank", "ASC") , Quota.class ) ;
		if( quota2List == null || quota2List.isEmpty() )
			return success( null ) ;
		//查询学校填报数据
		List<SchoolCollection> schoolCollections = schoolCollectionService.findList(
				new MapParam<String,Object>().push("reportId" , reporting.getId() )
				.push("schoolId" , schoolId )
				.push("collectionId" , JoinUtil.fieldsValue( collections , "id") ), null , SchoolCollection.class ) ;
		JoinUtil.join(collections, new String[]{"content"}, "id", schoolCollections , 
				new String[]{"content"}, "collectionId");
		
		//结构化结果返回
		Map<Integer,List<Collection>> tempMap = new HashMap<Integer,List<Collection>>() ;
		for( Collection collection : collections  ){
			if( !tempMap.containsKey( collection.getQuotaId() ) )
				tempMap.put(collection.getQuotaId() , new ArrayList<Collection>() ) ;
			tempMap.get( collection.getQuotaId() ).add( collection ) ;
		}
		for( Quota quota : quota2List ){
			quota.setCollections( tempMap.get( quota.getId() ) );
		}
		return success( new MapParam<String,Object>().push("quota" , quota2List ) ) ;
	}
	
	/**
	 * 填报审核,获取详情
	 * 
	 * @param schoolId
	 * @return
	 */
	@Get("/{schoolId}/reporting")
	public String getReporting( @PathVariable("schoolId") int schoolId ){
		//区县管理员、子账号 、市主账号可审核 
		SysRole role = roleService.findBy("code", Subject.getUser().getRoleCode() , SysRole.class  ) ;
		RoleCode roleCode = null ;
		if( role == null ||!Arrays.asList(
				RoleCode.COUNTY ,RoleCode.COUNTY_SUBACC , RoleCode.CITY )
					.contains( ( roleCode = RoleCode.parse( role.getCode() ) ) ) )
			return success( null ) ;
		
		Reporting reporting = null ;
		School school = null ;
		if( ( reporting = reportingService.getPresent() )== null
				|| ( school = schoolService.findBy("id", schoolId , School.class ) ) == null )
			return success( null ) ;
		
		//获取学校类型填报项
		List<Collection> schoolTypeCollections = schoolCollectionService.findSchoolTypeCollection(
				school.getType() , reporting.getId() ) ;
		if( schoolTypeCollections == null || schoolTypeCollections.isEmpty() )
			return success( null ) ;
		
		//如果当前账号为子账号,获取当前子账号审核指标
		List<Integer> quotaList = new ArrayList<Integer>() ;
		if( roleCode == RoleCode.COUNTY_SUBACC ){
			quotaList = quotaService.findSubaccQuota( Subject.getUserId() ) ;
			if( quotaList == null || quotaList.isEmpty() )
				return error(1 , "当前账号暂未分配审核指标" ) ;
		}
		
		List<Integer> auditQuotaList = new ArrayList<Integer>() ;
		for( Collection item : schoolTypeCollections ){
			if( roleCode == RoleCode.COUNTY || roleCode == RoleCode.CITY || quotaList.contains( item.getQuotaId() ) )
				auditQuotaList.add( item.getQuotaId() ) ;
		}
		//查询二级指标
		Map<String,Object> param = new HashMap<String,Object>() ;
		param.put("id", auditQuotaList ) ;
		List<Quota> quota2List = quotaService.findList( param ,
				new PageParam().orderBy("rank", "ASC") , Quota.class ) ;
		if( quota2List == null || quota2List.isEmpty() )
			return success( null ) ;
		
		//查询学校填报数据
		List<SchoolCollection> schoolCollections = schoolCollectionService.findList(
				new MapParam<String,Object>().push("reportId" , reporting.getId() )
				.push("schoolId" , schoolId )
				.push("collectionId" , JoinUtil.fieldsValue( schoolTypeCollections , "id") ), null , SchoolCollection.class ) ;
		JoinUtil.join(schoolTypeCollections, new String[]{"content"}, "id", schoolCollections , 
				new String[]{"content"}, "collectionId");
		
		//结构化结果返回
		Map<Integer,List<Collection>> tempMap = new HashMap<Integer,List<Collection>>() ;
		for( Collection collection : schoolTypeCollections ){
			if( !tempMap.containsKey( collection.getQuotaId() ) )
				tempMap.put(collection.getQuotaId() , new ArrayList<Collection>() ) ;
			tempMap.get( collection.getQuotaId() ).add( collection ) ;
		}
		for( Quota quota : quota2List ){
			quota.setCollections( tempMap.get( quota.getId() ) );
		}
		
		SchoolProcess process = schoolCollectionService.getProcess(schoolId, reporting.getId() ) ;
		
		Map<String,Object> result = new HashMap<String,Object>() ;
		result.put("quota", quota2List ) ;
		//是否可审核
		result.put("audit", process != null 
				&& ( ( roleCode == RoleCode.COUNTY && Arrays.asList(1,2,3).contains( process.getStatus() ) ) 
				|| ( RoleCode.COUNTY_SUBACC == roleCode && Arrays.asList(1,2).contains( process.getStatus() ) )
				|| (roleCode == RoleCode.CITY && process.getStatus() == 5 ) ) ) ;
		//是否可提交审核
		/*result.put("submit", ( roleCode == RoleCode.COUNTY 
				&& Arrays.asList(3).contains( process.getStatus() ) )  ) ;*/
		result.put("submit", false ) ;
		return success( result ) ;
	}
	
	private List<Integer> getUserAuditCollections( int schoolId ){
		Reporting reporting = verifyReporting() ;
		School school = schoolService.findBy("id", schoolId, School.class , "id" , "type" ) ;
		if( school == null )
			return null ;
		
		//获取当前用户角色类型
		RoleCode role = RoleCode.parse( Subject.getUser().getRoleCode() ) ;
		if(! Arrays.asList( RoleCode.COUNTY , RoleCode.CITY, RoleCode.COUNTY_SUBACC ).contains( role ) )
			throw new BusinessException("当前用户无权审核!") ;
		Integer userId = Arrays.asList(RoleCode.COUNTY , RoleCode.CITY).indexOf( role ) != -1  ? null : Subject.getUserId() ;
		
		//获取当前学校所有采集项
		List<Collection> schoolTypeCollections = schoolCollectionService.findSchoolTypeCollection(
				school.getType() , reporting.getId() ) ;
		if( schoolTypeCollections == null || schoolTypeCollections.isEmpty() )
			return null ;
		
		//如果当前账号为子账号,获取当前子账号审核指标
		List<Integer> quotaList = new ArrayList<Integer>() ;
		if( userId != null && 
				(quotaList = quotaService.findSubaccQuota( userId ) ) == null )
			return null ;

		List<Integer> result = new ArrayList<Integer>() ;
		for( Collection collection : schoolTypeCollections )
			if( userId == null || quotaList.contains( collection.getQuotaId() ) )
				result.add( collection.getId() ) ;
		
		return result ;
	}
	
	
	/**
	 * 学校填报审核
	 * 
	 * @param schoolId
	 * @param param
	 * @return
	 */
	@Put("/{schoolId}/audit")
	public String audit( @PathVariable("schoolId") int schoolId , @RequestBody SchoolAuditingParam param ){
		RoleCode role = RoleCode.parse( Subject.getUser().getRoleCode() ) ;
		if( role == null ||
				!Arrays.asList( RoleCode.COUNTY , RoleCode.CITY , RoleCode.COUNTY_SUBACC ).contains( role ) )
			throw new BusinessException("当前账号无法审核学校填报!") ;
		
		param.setSchoolId( schoolId );
		param.setUserId( Subject.getUserId() );
		param.setCollectionsId( StringUtil
				.join( getUserAuditCollections( schoolId ), "," ) );
		if( param.getCollectionsId() == null )
			throw new BusinessException("当前账没有分配指标,无法审核!") ; 
		int reportingId = verifyReporting().getId() ;
		param.setReportId( reportingId );
		param.setStatus( param.getStatus() != null && param.getStatus() == 1 ? 1 : 0 );
		if( param.getStatus() == 0 && StringUtil.isEmpty( param.getAuditNote() ) )
			throw new BusinessException("返回修改请填写修改意见!") ;
		auditingService.audit(  param , role ) ;
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
	@Put("/{schoolId}/submitAuditing")
	public String submitToAudit(  @PathVariable("schoolId") int schoolId ){
		auditingService.submitAuditing( schoolId , verifyReporting().getId() ) ;
		return success( null ) ;
	}
}
