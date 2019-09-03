package com.newproj.report.school.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.newproj.core.beans.MapParam;
import com.newproj.core.exception.BusinessException;
import com.newproj.core.page.PageParam;
import com.newproj.core.page.PageUtil;
import com.newproj.core.page.RemotePage;
import com.newproj.core.rest.support.AbstractBaseService;
import com.newproj.core.util.NumberUtil;
import com.newproj.core.util.StringUtil;
import com.newproj.core.util.TimeUtil;
import com.newproj.report.bulletin.constant.BulletinConstant;
import com.newproj.report.bulletin.dal.dao.BulletinDao;
import com.newproj.report.bulletin.dto.BulletinParam;
import com.newproj.report.eduinst.dal.dao.EduinstDao;
import com.newproj.report.eduinst.dto.Eduinst;
import com.newproj.report.school.dal.dao.SchoolAuditingDao;
import com.newproj.report.school.dal.dao.SchoolCollectionDao;
import com.newproj.report.school.dal.dao.SchoolDao;
import com.newproj.report.school.dal.dao.SchoolProcessDao;
import com.newproj.report.school.dto.School;
import com.newproj.report.school.dto.SchoolAuditing;
import com.newproj.report.school.dto.SchoolAuditingParam;
import com.newproj.report.school.dto.SchoolCollection;
import com.newproj.report.school.dto.SchoolProcess;
import com.newproj.report.school.dto.SchoolReportingData;
import com.newproj.report.sys.constant.RoleConstant.RoleCode;
import com.newproj.report.sys.constant.UserConstant;
@Service
public class SchoolAuditingService extends AbstractBaseService{

	@Autowired
	private SchoolAuditingDao auditingDao ;
	
	@Autowired
	private SchoolCollectionDao schoolCollectionDao ;
	
	@Autowired
	private SchoolProcessDao processDao ;
	
	@Autowired
	private BulletinDao bulletinDao ;
	
	@Autowired
	private SchoolDao schoolDao ;
	
	@Autowired
	private EduinstDao eduinstDao ;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		setMapper(auditingDao) ;
	}

	public RemotePage<SchoolAuditing> findSubaccAuditing( Map<String,Object> param , PageParam pageParam ){
		Page<?> page = PageUtil.startPage(pageParam) ;
		List<SchoolAuditing> dataList = auditingDao.findSubaccAuditing(param, SchoolAuditing.class );
		if( dataList == null || dataList.isEmpty() )
			return null ;
		return new RemotePage<SchoolAuditing>( dataList , page ) ;
	}
	
	public RemotePage<SchoolAuditing> findAuditing( Map<String,Object> param , PageParam pageParam ){
		Page<?> page = PageUtil.startPage(pageParam) ;
		List<SchoolAuditing> dataList = auditingDao.findAuditing(param, SchoolAuditing.class );
		if( dataList == null || dataList.isEmpty() )
			return null ;
		return new RemotePage<SchoolAuditing>( dataList , page ) ;
	}
	
	public RemotePage<SchoolAuditing> findReporting( Map<String,Object> param , PageParam pageParam ){
		Page<?> page = PageUtil.startPage(pageParam) ;
		List<SchoolAuditing> dataList = auditingDao.findReporting(param, SchoolAuditing.class );
		if( dataList == null || dataList.isEmpty() )
			return null ;
		return new RemotePage<SchoolAuditing>( dataList , page ) ;
	}
	
	public List<SchoolReportingData> exportReportingData( int schoolId , int schoolType ,  int reportingId ){
		return auditingDao.exportReportingData(schoolId, schoolType, reportingId, SchoolReportingData .class ) ;
	}
	
	@Transactional( propagation = Propagation.REQUIRED )
	public void audit( SchoolAuditingParam param , RoleCode role ){
		SchoolProcess process = processDao.findBean(new MapParam<String,Object>()
				.push("schoolId" , param.getSchoolId() )
				.push("reportId" , param.getReportId() ), SchoolProcess.class ) ;
		if( process == null )
			throw new BusinessException("审核失败,更新学校填报进度出错!") ;
		
		//审核状态校验
		if( ( Arrays.asList(RoleCode.COUNTY , RoleCode.COUNTY_SUBACC).contains( role ) 
				&& !Arrays.asList( 1 , 2 , 3 ).contains( process.getStatus() ) ) || (
						role == RoleCode.CITY && process.getStatus() != 5 ) )
			throw new BusinessException("当前填报无法执行审核操作!") ;
		
		//区县、市返回修改删除审核记录
		if( param.getStatus() == 0 )
			auditingDao.delete(new MapParam<String,Object>()
					.push("reportId" , param.getReportId() ) 
					.push("schoolId" , param.getSchoolId() ) ) ;
		
		//子账号审核更新子账号审核记录 .
		if( RoleCode.COUNTY_SUBACC == role ){
			SchoolAuditing auditing = auditingDao.findBean(new MapParam<String,Object>()
					.push("reportId" , param.getReportId() )
					.push("schoolId" , param.getSchoolId() )
					.push("userId" , param.getUserId() ), SchoolAuditing.class ) ;
			param.setAuditTime( TimeUtil.now() );
			if( param.getStatus() == 1 && param.getAuditNote() == null )
				param.setAuditNote("");
			if( auditing == null ){ //添加审核记录
				auditingDao.createBean( param , SchoolAuditingParam.class ) ;
			}else{
				auditingDao.updateBean("id", auditing.getId() , param ) ;
			}
		}
		
		boolean completeAudit = false  ;
		//更新学校填报进度 .
		Map<String,Object> editable = new HashMap<String,Object>() ;
		if( param.getStatus() == 0 ){//返回修改
			if( role == RoleCode.CITY )
				editable.put("status", 1 ) ; //市教育局返回修改更新为县教育局待审核
			else
				editable.put("status", 0 ) ; //否则修改为学校填报中（学校重新修改提交）
		}else{//审核中/审核完成
			if( RoleCode.COUNTY_SUBACC == role ){//县教育局子账号审核通过需要校验所有填报项是否全部审核通过
				completeAudit =  checkCompleteAudit( param.getSchoolId() , param.getReportId() ) ;
				editable.put( "status", completeAudit ? 3 : 2 ) ;
			}else if( RoleCode.COUNTY == role ){ //县、市主账号审核通过直接审核完成提交 .
				completeAudit = true ;
				editable.put("status", 3 ) ;
			}else if( RoleCode.CITY == role ){
				completeAudit = true ;
				editable.put("status", 6 ) ;
			}
		}
		
		//添加审核返回修改通知
		if( param.getStatus() == 0  ){
			School school = schoolDao.findBeanBy("id", param.getSchoolId() , School.class, "code","name","eduinstId") ;
			if(  Arrays.asList(RoleCode.COUNTY , RoleCode.COUNTY_SUBACC).contains( role ) ){
				String title = "审核通知" ,
						content = String.format("贵校的填报审核已被区县教育局返回修改,请参考修改意见修改后重新提交审核.<br>【审核意见】:%s", param.getAuditNote() ) ;
				createAuditNotice( param.getSchoolId() ,  title , content , UserConstant.InstType.SCHOOL ) ;
			}else if( RoleCode.CITY == role ){//市教育局返回修改添加修改通知
				Eduinst eduinst  = eduinstDao.findBeanBy("id", school.getEduinstId() , Eduinst.class , "id","name") ;
				if( eduinst != null ){
					String title = "审核通知" ,
							content = String.format("学校【"+school.getName()+"】的填报审核已被市教育局返回修改,请参考修改意见修改后重新审核提交.<br>【审核意见】:%s", param.getAuditNote() ) ;
					createAuditNotice( eduinst.getId() ,  title , content , UserConstant.InstType.EDUINST ) ;
				}
			}
		}
		processDao.updateMap("id", process.getId() , editable ) ;
		
		//县教育局主账号审核完成调用提交，发送审核通过通知 .
		if( RoleCode.COUNTY == role && completeAudit )
			submitAuditing( param.getSchoolId() , param.getReportId() );
	}
	
	private boolean checkCompleteAudit( int schoolId , int reportId ){
		List<SchoolAuditing> auditingList = auditingDao.findBeanList( new MapParam<String,Object>()
				.push("schoolId" , schoolId )
				.push("reportId" , reportId )
				.push("status" , 1 ), SchoolAuditing.class );
		if( auditingList == null || auditingList.isEmpty() )
			return false ;
		Set<Integer> collectionsId = new HashSet<Integer>() ;
		for( SchoolAuditing auditing : auditingList ){
			if( auditing.getCollectionsId() == null ) continue ;
			String [] strIds = StringUtil.explode(auditing.getCollectionsId() , ",") ;
			for( String strId : strIds ){
				int value ;
				if( (value = NumberUtil.parseInt( strId , 0 ) ) == 0 )
					continue ;
				collectionsId.add( value );
			}
		}
		if( collectionsId.isEmpty() ) return false;
		List<SchoolCollection> collections = schoolCollectionDao.findBeanList(
			new MapParam<String,Object>()
				.push("reportId" , reportId )
				.push("schoolId" , schoolId ), SchoolCollection.class ) ;
		if( collections == null || collections.isEmpty() )
			return true ;
		
		for( SchoolCollection collection : collections ){
			if( !collectionsId.contains( collection.getCollectionId() ) )
				return false ;
		}
		return true ;
	}
	
	private void createAuditNotice( int instId , String title , String content , UserConstant.InstType instType ){
		BulletinParam bulletin = new BulletinParam() ;
		if( UserConstant.InstType.SCHOOL == instType )
			bulletin.setSchoolId( instId );
		else if( UserConstant.InstType.EDUINST == instType )
			bulletin.setEduinstId(instId);
		else return ;
		bulletin.setType(BulletinConstant.Type.NOTICE.name());
		bulletin.setTitle( title );
		bulletin.setContent( content );
		bulletin.setCreateTime(TimeUtil.now() );
		bulletin.setCreateId(0);
		bulletin.setPublishId(0);
		bulletin.setPublishTime(TimeUtil.now() );
		bulletin.setStatus( 1 );
		bulletinDao.createBean(bulletin, BulletinParam.class);
	}
	
	@Transactional( propagation = Propagation.REQUIRED , rollbackFor = Exception .class )
	public void submitAuditing( int schoolId , int reportingId ){
		SchoolProcess process = processDao.findBean(new MapParam<String,Object>()
				.push("schoolId" , schoolId )
				.push("reportId" , reportingId ), SchoolProcess.class ) ;
		if( process == null || process.getStatus() == null || 
				process.getStatus() != 3 )
			throw new BusinessException("当前填报无法提交上级审核!") ;
		processDao.updateMap("id", process.getId() , new MapParam<String,Object>() 
				.push("status" , 5 )
				.push("auditTime" , TimeUtil.now() ) ) ;
		
		//审核通过发送审核通知 .
		String title = "审核通知" ,
				content = "贵校所填报数据经区县教育局审核，已通过！" ;
		createAuditNotice( schoolId ,  title , content , UserConstant.InstType.SCHOOL ) ;
	}

}
