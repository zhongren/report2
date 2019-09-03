package com.newproj.report.eduinst.service;

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
import com.newproj.report.eduinst.dal.dao.EduinstAuditingDao;
import com.newproj.report.eduinst.dal.dao.EduinstCollectionDao;
import com.newproj.report.eduinst.dal.dao.EduinstProcessDao;
import com.newproj.report.eduinst.dto.EduinstAuditing;
import com.newproj.report.eduinst.dto.EduinstAuditingParam;
import com.newproj.report.eduinst.dto.EduinstCollection;
import com.newproj.report.eduinst.dto.EduinstProcess;
import com.newproj.report.eduinst.dto.EduinstReportingData;
@Service
public class EduinstAuditingService extends AbstractBaseService{

	@Autowired
	private EduinstAuditingDao auditingDao ;
	
	@Autowired
	private EduinstProcessDao processDao ;
	
	@Autowired
	private EduinstCollectionDao eduinstCollectionDao ;
	
	@Autowired
	private BulletinDao bulletinDao ;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		setMapper( auditingDao ) ;
	}
	
	public RemotePage<EduinstAuditing> findAuditing( Map<String,Object> param , PageParam pageParam ){
		Page<?> page = PageUtil.startPage(pageParam) ;
		List<EduinstAuditing> dataList = auditingDao.findAuditing(param, EduinstAuditing.class );
		if( dataList == null || dataList.isEmpty() )
			return null ;
		return new RemotePage<EduinstAuditing>( dataList , page ) ;
	}
	
	public RemotePage<EduinstAuditing> findSubaccAuditing( Map<String,Object> param , PageParam pageParam ){
		Page<?> page = PageUtil.startPage(pageParam) ;
		List<EduinstAuditing> dataList = auditingDao.findSubaccAuditing(param, EduinstAuditing.class );
		if( dataList == null || dataList.isEmpty() )
			return null ;
		return new RemotePage<EduinstAuditing>( dataList , page ) ;
	}
	
	public RemotePage<EduinstAuditing> findReporting( Map<String,Object> param , PageParam pageParam ){
		Page<?> page = PageUtil.startPage(pageParam) ;
		List<EduinstAuditing> dataList = auditingDao.findReporting(param, EduinstAuditing.class );
		if( dataList == null || dataList.isEmpty() )
			return null ;
		return new RemotePage<EduinstAuditing>( dataList , page ) ;
	}

	@Transactional( propagation = Propagation.REQUIRED )
	public void audit( EduinstAuditingParam param , boolean isAutoSubmit ){
		//获取填报进度
		EduinstProcess process = processDao.findBean(new MapParam<String,Object>()
				.push("instId" , param.getEduinstId() )
				.push("reportId" , param.getReportId() ), EduinstProcess.class ) ;
		if( process == null )
			throw new BusinessException("审核失败,更新学校填报进度出错!") ;
		if( !Arrays.asList( 1 , 2 , 3 ).contains( process.getStatus() ) )
			throw new BusinessException("当前填报无法审核!") ;
		
		//返回修改删除其他审核记录,重新审核 .
		if( param.getStatus() == 0 )
			auditingDao.delete(new MapParam<String,Object>()
					.push("reportId" , param.getReportId() )
					.push("eduinstId" , param.getEduinstId() ) ) ;
		//更新审核记录 .
		EduinstAuditing auditing = auditingDao.findBean(new MapParam<String,Object>()
				.push("reportId" , param.getReportId() )
				.push("eduinstId" , param.getEduinstId() )
				.push("userId" , param.getUserId() ), EduinstAuditing.class ) ;
		param.setAuditTime( TimeUtil.now() );
		if( param.getStatus() == 1 && param.getAuditNote() == null )
			param.setAuditNote("");
		if( auditing == null ){ //添加审核记录
			auditingDao.createBean( param , EduinstAuditingParam.class ) ;
		}else{
			auditingDao.updateBean("id", auditing.getId() , param ) ;
		}
		
		boolean completeAudit = false  ;
		//更新学校填报进度 .
		Map<String,Object> editable = new HashMap<String,Object>() ;
		if( param.getStatus() == 0 ){//返回修改
			editable.put("status", 0 ) ; //返回修改
		}else{//审核中/审核完成
			editable.put("status", ( completeAudit = 
					checkCompleteAudit(param.getEduinstId() , param.getReportId() ) )  ? 3 : 2 )  ;
		}
		//添加审核通知
		if( param.getStatus() == 0 ){
			String title = "审核通知" ,
					content = String.format("您的填报审核已被市教育局返回修改,请参考修改意见修改后重新提交审核.<br>【审核意见】:%s", param.getAuditNote() ) ;
			createAuditNotice( param.getEduinstId() , title , content ) ;
		}
		processDao.updateMap("id", process.getId() , editable ) ;
		
		//审核完成自动提交
		if( isAutoSubmit && completeAudit )
			submitAuditing( param.getEduinstId() , param.getReportId() );
	}
	
	private boolean checkCompleteAudit( int eduinstId , int reportId ){
		List<EduinstAuditing> auditingList = auditingDao.findBeanList( new MapParam<String,Object>()
				.push("eduinstId" , eduinstId )
				.push("reportId" , reportId )
				.push("status" , 1 ), EduinstAuditing.class );
		if( auditingList == null || auditingList.isEmpty() )
			return false ;
		Set<Integer> collectionsId = new HashSet<Integer>() ;
		for( EduinstAuditing auditing : auditingList ){
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
		List<EduinstCollection> collections = eduinstCollectionDao.findBeanList(
			new MapParam<String,Object>()
				.push("reportId" , reportId )
				.push("instId" , eduinstId ), EduinstCollection.class ) ;
		if( collections == null || collections.isEmpty() )
			return true ;
		
		for( EduinstCollection collection : collections ){
			if( !collectionsId.contains( collection.getCollectionId() ) )
				return false ;
		}
		return true ;
	}
	
	private void createAuditNotice( int instId , String title , String content ){
		BulletinParam bulletin = new BulletinParam() ;
		bulletin.setEduinstId(instId);
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
	
	public void submitAuditing( int eduinstId , int reportingId ){
		EduinstProcess process = processDao.findBean(new MapParam<String,Object>()
				.push("instId" , eduinstId )
				.push("reportId" , reportingId ), EduinstProcess.class ) ;
		if( process == null || process.getStatus() == null || 
				process.getStatus() != 3 )
			throw new BusinessException("当前填报无法提交上级审核!") ;
		processDao.updateMap("id", process.getId() , new MapParam<String,Object>() 
				.push("status" , 5 )
				.push("auditTime" , TimeUtil.now() )) ;
		
		//审核通过发送审核通知 .
		String title = "审核通知" ,
				content = "您所填报数据经市教育局审核，已通过！" ;
		createAuditNotice( eduinstId ,  title , content  ) ;
	}
	
	
	public List<EduinstReportingData> exportReportingData( int eduinstId , int reportingId ){
		return auditingDao.exportReportingData(eduinstId, reportingId, EduinstReportingData .class ) ;
	}
}
