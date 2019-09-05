package com.newproj.report.school.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.newproj.report.sys.dto.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.newproj.core.beans.MapParam;
import com.newproj.core.exception.BusinessException;
import com.newproj.core.rest.annotation.Api;
import com.newproj.core.rest.annotation.Get;
import com.newproj.core.rest.annotation.Post;
import com.newproj.core.rest.annotation.Put;
import com.newproj.core.rest.support.RestActionSupporter;
import com.newproj.core.util.NumberUtil;
import com.newproj.core.util.ScriptUtil;
import com.newproj.report.collection.dto.Collection;
import com.newproj.report.collection.service.CollectionService;
import com.newproj.report.context.Subject;
import com.newproj.report.eduinst.dto.EduinstCollectionParam;
import com.newproj.report.eduinst.dto.EduinstProcess;
import com.newproj.report.eduinst.service.EduinstCollectionService;
import com.newproj.report.quota.dto.Quota;
import com.newproj.report.quota.service.QuotaService;
import com.newproj.report.reporting.dto.Reporting;
import com.newproj.report.reporting.service.ReportingService;
import com.newproj.report.school.dal.dao.SchoolProcessDao;
import com.newproj.report.school.dto.CollectionParam;
import com.newproj.report.school.dto.School;
import com.newproj.report.school.dto.SchoolCollection;
import com.newproj.report.school.dto.SchoolCollectionParam;
import com.newproj.report.school.dto.SchoolProcess;
import com.newproj.report.school.dto.SchoolReportingData;
import com.newproj.report.school.service.SchoolAuditingService;
import com.newproj.report.school.service.SchoolCollectionService;
import com.newproj.report.school.service.SchoolService;
import com.newproj.report.sys.constant.RoleConstant.RoleCode;
import com.newproj.report.sys.constant.UserConstant.InstType;

@Api("/collection")
public class SchoolCollectionAction extends RestActionSupporter{

	@Autowired
	private CollectionService collectionService ;

	@Autowired
	private QuotaService quotaService ;

	@Autowired
	private SchoolCollectionService scollectionService;

	@Autowired
	private SchoolService schoolService ;

	@Autowired
	private ReportingService reportingService ;

	@Autowired
	private SchoolCollectionService schoolCollectionService ;

	@Autowired
	private ReportingService reportService ;

	@Autowired
	private EduinstCollectionService eduinstCollectionService ;

	@Autowired
	private SchoolProcessDao schoolProcessDao;

	@Get("/quotas")
	public String findQuotas( @RequestParam( value="process" , required = false ) String process ){
		include(Quota.class, "id","name","subQuota","finished") ;
		User user=Subject.getUser();
		if( InstType.parse( Subject.getUser().getInstType() ) == InstType.EDUINST && Subject.getUser().getRoleCode().equals(RoleCode.COUNTY.name()))
			return success( quotaService.quotaRank(findEduinstQuotas()) ) ;
		else if( InstType.parse( Subject.getUser().getInstType() ) == InstType.SCHOOL )
			return success( quotaService.quotaRank(findSchoolQuotas()) ) ;
		return success( null ) ;
	}

	private List<Quota> findSchoolQuotas(){
		Reporting reporting = reportService.getPresent() ;
		if( reporting == null )
			return null ;

		Integer schoolId = Subject.getUser().getInstId() , reportId = reporting.getId() ;
		School school = schoolService.findBy("id", schoolId, School.class ) ;
		if( school == null )
			return null ;

		List<Quota> dataList = quotaService.findSchoolQuota( school.getType() , reportId ) ;
		if( dataList == null || dataList.isEmpty() ){
			return null ;
		}
		return quotaService.structSubQuota(
				storeSchoolQuotaProcess(school.getType()+"", reportId , schoolId , dataList)  ) ;
	}

	private List<Quota> findEduinstQuotas( ){
		Reporting reporting = reportService.getPresent() ;
		if( reporting == null )
			return null ;
		List<Quota> dataList = quotaService
				.findEduinstQuota( Subject.getUser().getRoleCode() , reporting.getId() ) ;
		if( dataList == null || dataList.isEmpty() ){
			return null ;
		}
		return quotaService.structSubQuota( dataList ) ;
	}

	private List<Quota> storeSchoolQuotaProcess(String schoolType, int reportId , int schoolId , List<Quota> dataList ){
		if( dataList == null || dataList.isEmpty() )
			return null ;
		List<SchoolCollection> collections = schoolCollectionService.findSchoolCollection(schoolId, reportId) ;
		if( collections == null || collections.isEmpty() )
			return dataList ;
		Set<Integer> quotasId = new HashSet<Integer>() ;
		for( SchoolCollection item : collections ){
			quotasId.add( item.getQuotaId() ) ;
		}
		List<Map<Integer, Integer>> list = schoolProcessDao.findCollections(schoolId,reportId,schoolType);
		Map<Integer, Boolean> resMap = new HashMap<>();
		for(Map<Integer, Integer> map : list) {
			int allcount = Integer.valueOf(map.get("all_count")+"");
			int havecount = Integer.valueOf(map.get("school_count")+"");
			boolean res =  (allcount-havecount) > 0 ? false : true ;
			resMap.put(map.get("quota_id"), res);
		}

		for( Quota quota : dataList ){
			quota.setFinished(resMap.get(quota.getId()));
		}
		return dataList ;
	}

	/**
	 * 获取学校填报指标 .
	 *
	 * @param quotaId
	 * @return
	 */
	@Get("/{quotaId}/quotaCollection")
	public String findCollectionByQuota( @PathVariable("quotaId") int quotaId ){
		Reporting reporting = null ;
		if( ( reporting = reportingService.getPresent() ) == null )
			return success( null ) ;
		include(Collection.class , "id","code","title","quotaId","memo","valueType","valueOpt","validation","note","rank","content");

		if( InstType.parse( Subject.getUser().getInstType() ) == InstType.EDUINST )
			return success( findEduinstCollectionByQuota(
					quotaId , Subject.getUser().getInstId(),Subject.getUser().getRoleCode() , reporting.getId() ) ) ;
		else if( InstType.parse( Subject.getUser().getInstType() ) == InstType.SCHOOL )
			return success( findSchoolCollectionByQuota(
					quotaId , Subject.getUser().getInstId() , reporting.getId() ) ) ;
		return success( null ) ;
	}

	private List<Collection> findSchoolCollectionByQuota( int quotaId , int schoolId , int reportId ){
		School school = schoolService.findBy("id", schoolId , School.class ) ;
		if( school == null )
			return null ;
		List<Collection> dataList = collectionService.findSchoolCollectionWithQuota(
				reportId , schoolId , school.getType() , quotaId ) ;
		if(quotaId==30) {
			SchoolCollection sc = getCollection(schoolId, reportId, 10);
			if(null!=sc && sc.getContent().equals("0")) {
				int j = -1 ;
				for(int i = 0 ; i<dataList.size() ; i++) {
					 if(dataList.get(i).getId()==59) {
						  j = i ;
					 }
				}
				if(j > -1  ) {
					dataList.remove(j);
				}
			}
		}
		return dataList ;
	}


	private SchoolCollection getCollection(int schoolId,int reportId , int cid) {
		MapParam<String, Object> pamMap = new MapParam<>();
		pamMap.push("schoolId",schoolId);
		pamMap.push("reportId", reportId);
		pamMap.push("collectionId", cid);
		SchoolCollection sc = scollectionService.findOne(pamMap, SchoolCollection.class);
		return sc;
	}

	private List<Collection> findEduinstCollectionByQuota( int quotaId ,int instId , String eduinstType , int reportId ){
		return eduinstCollectionService.getEduinstCollection( quotaId ,instId , eduinstType, reportId) ;
	}

	/**
	 * 保存学校填报数据 .
	 *
	 * @param quotaId
	 * @param params
	 * @return
	 */
	@Post("/{quotaId}/saveCollection")
	public String saveCollection( @PathVariable("quotaId") int quotaId ,
			@RequestBody List<CollectionParam> params ){
		Reporting reporting = reportingService.getPresent() ;
		if( reporting == null )
			throw new BusinessException("填报已关闭!") ;

		if( InstType.parse( Subject.getUser().getInstType() ) == InstType.EDUINST )
			return success( saveEduinstCollection(  quotaId , reporting.getId() , params  ) ) ;
		else if( InstType.parse( Subject.getUser().getInstType() ) == InstType.SCHOOL )
			return success( saveSchoolCollection( quotaId , reporting.getId() , params ) ) ;

		throw new BusinessException("操作失败!") ;
	}

	private List<Integer> saveEduinstCollection( int quotaId , int reportId , List<CollectionParam> params ){
		EduinstProcess process = eduinstCollectionService.getProcess( Subject.getUser().getInstId() , reportId) ;
		if( process != null && !Arrays.asList( 0 , 4 ).contains( process.getStatus() ) )
			throw new BusinessException("当前填报已提交,无法修改!") ;

		List<EduinstCollectionParam> collectionParams = new ArrayList<EduinstCollectionParam>() ;
		for( CollectionParam param : params ){
			EduinstCollectionParam collectionParam = new EduinstCollectionParam() ;
			collectionParam.setCollectionId( param.getCollectionId() );
			collectionParam.setContent( param.getContent() );
			collectionParam.setCreateId( Subject.getUserId() );
			collectionParam.setInstId( Subject.getUser().getInstId() );
			collectionParam.setReportId( reportId );
			collectionParams.add( collectionParam ) ;
		}
		return eduinstCollectionService.saveCollection(collectionParams) ;
	}

	private List<Integer> saveSchoolCollection( int quotaId , int reportId , List<CollectionParam> params ){
		SchoolProcess process = schoolCollectionService.getProcess( Subject.getUser().getInstId() , reportId) ;
		if( process != null && !Arrays.asList( 0 , 4 ).contains( process.getStatus() ) )
			throw new BusinessException("当前填报已提交,无法修改!") ;
		List<SchoolCollectionParam> collectionParams = new ArrayList<SchoolCollectionParam>() ;
		for( CollectionParam param : params ){
			if(param.getCollectionId()==10 ) {
				if(param.getContent().equals("0")) {
					SchoolCollectionParam c1 = new SchoolCollectionParam() ;
					String  co1 = "{\"radio\":{\"value\":\"是\",\"name\":\"radio\"},\"img\":{\"name\":\"img\",\"value\":[\"\"]}}";
					c1.setCollectionId( 59 );
					c1.setContent( co1 );
					c1.setCreateId(-100);
					c1.setSchoolId( Subject.getUser().getInstId() );
					c1.setReportId( reportId );
					collectionParams.add(c1);
				}else {
					SchoolCollection sc = getCollection(Subject.getUser().getInstId(), reportId, 59);
					if(null!=sc && sc.getCreateId()==-100) {
						MapParam<String, Object> dp = new MapParam<>();
						dp.push("id", sc.getId());
						schoolCollectionService.delete(dp);
					}
				}
			}
			SchoolCollectionParam collectionParam = new SchoolCollectionParam() ;
			collectionParam.setCollectionId( param.getCollectionId() );
			collectionParam.setContent( param.getContent() );
			collectionParam.setCreateId( Subject.getUserId() );
			collectionParam.setSchoolId( Subject.getUser().getInstId() );
			collectionParam.setReportId( reportId );
			collectionParams.add( collectionParam ) ;
		}

		return schoolCollectionService.saveSchoolCollection( collectionParams ) ;
	}

	/**
	 * 获取当前填报进度 .
	 *
	 * @return
	 */
	@Get("/getReportingProcess")
	public String getReportingProcess(){
		Reporting reporting = reportingService.getPresent() ;
		if( reporting == null )
			throw new BusinessException("填报已关闭!");
		if( InstType.parse( Subject.getUser().getInstType() ) == InstType.EDUINST )
			return success( eduinstCollectionService.getProcess( Subject.getUser().getInstId()  , reporting.getId() )  ) ;
		else if( InstType.parse( Subject.getUser().getInstType() ) == InstType.SCHOOL )
			return success( schoolCollectionService.getProcess( Subject.getUser().getInstId()  , reporting.getId() ) ) ;
		return success( null ) ;
	}

	/**
	 * 填报提交审核
	 *
	 * @return
	 */
	@Put("/submitAudit")
	public String submitAudit(){
		Reporting reporting = reportingService.getPresent() ;
		if( reporting == null )
			throw new BusinessException("填报已关闭!") ;
		if( InstType.parse( Subject.getUser().getInstType() ) == InstType.EDUINST )
			eduinstCollectionService.submitAudit( Subject.getUser().getInstId() ,
					reporting.getId() , Subject.getUserId() );
		else if( InstType.parse( Subject.getUser().getInstType() ) == InstType.SCHOOL )
			schoolCollectionService.submitAudit(Subject.getUser().getInstId() ,
				reporting.getId() , Subject.getUserId() );
		else
			throw new BusinessException("操作失败!") ;
		return success( null ) ;
	}
}
