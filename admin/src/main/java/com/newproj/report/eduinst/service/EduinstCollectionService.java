package com.newproj.report.eduinst.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.newproj.core.beans.MapParam;
import com.newproj.core.exception.BusinessException;
import com.newproj.core.rest.support.AbstractBaseService;
import com.newproj.core.util.NumberUtil;
import com.newproj.core.util.TimeUtil;
import com.newproj.report.bulletin.constant.BulletinConstant;
import com.newproj.report.bulletin.dto.BulletinParam;
import com.newproj.report.collection.dal.dao.CollectionDao;
import com.newproj.report.collection.dto.Collection;
import com.newproj.report.context.Subject;
import com.newproj.report.eduinst.dal.dao.EduinstCollectionDao;
import com.newproj.report.eduinst.dal.dao.EduinstDao;
import com.newproj.report.eduinst.dal.dao.EduinstProcessDao;
import com.newproj.report.eduinst.dto.EduinstCollectionParam;
import com.newproj.report.eduinst.dto.EduinstProcess;
import com.newproj.report.eduinst.dto.EduinstProcessParam;

@Service
public class EduinstCollectionService extends AbstractBaseService{

	@Autowired
	private EduinstCollectionDao eduinstCollectionDao ;
	
	@Autowired
	private CollectionDao collectionDao ;
	
	@Autowired
	private EduinstDao eduinstDao ;
	
	@Autowired
	private EduinstProcessDao eduinstProcessDao ;
	
	public void init() {
		setMapper( eduinstCollectionDao ) ;
	}
	
	public List<Collection> getEduinstCollection( Integer quotaId , int eduinstId , String eduinstType , int reportId ){
		return eduinstCollectionDao.findEduinstCollection( quotaId ,eduinstId, eduinstType, reportId, Collection.class) ;
	}

	/**
	 * 批量保存填报数据 .
	 * 
	 * @param params
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED , timeout=10000 )
	public List<Integer> saveCollection( List<EduinstCollectionParam> params ){
		if( params == null || params.isEmpty() )
			throw new BusinessException("保存填报数据失败,无填报数据!") ;
		
		//保存填报数据.
		List<Integer> ids = new ArrayList<Integer>() ;
		for( EduinstCollectionParam item : params ){
			ids.add( saveCollection( item ) ) ;
		}
		EduinstCollectionParam param = params.get( 0 ) ;
		updateProcess( param.getReportId() , param.getInstId() ) ;
		return ids ;
	}
	
	public int saveCollection( EduinstCollectionParam param ){
		if( param.getInstId() == null || 
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
		
		List<EduinstCollectionParam> oldRow = eduinstCollectionDao.findBeanList(
				new MapParam<String,Object>().push("instId", param.getInstId() )
					.push("collectionId", param.getCollectionId() )
					.push("reportId", param.getReportId() ), EduinstCollectionParam.class ) ;
		if( oldRow != null && !oldRow.isEmpty() ){//更新记录
			eduinstCollectionDao.updateBean("id" , oldRow.get(0).getId() , param ) ;
			return oldRow.get(0).getId() ;
		}
		//添加新记录
		return NumberUtil.parseInt( eduinstCollectionDao
				.createBean( param , EduinstCollectionParam.class ) , 0 ) ;	
	}
	
	/**
	 * 更新填报进度 .
	 * 
	 * @param reportId
	 * @param schoolId
	 */
	public void updateProcess( int reportId , int instId ){
		//更新填报进度 .
		List<?> dataList = eduinstCollectionDao.findMapList(new MapParam<String,Object>()
				.push("reportId", reportId )
				.push("instId", instId ), "id" ) ;
		EduinstProcessParam editable = new EduinstProcessParam() ;
		editable.setFinishNum( dataList == null ? 0 : dataList.size() ) ;
		
		eduinstProcessDao.updateBean("id", getProcess( instId , reportId ).getId() , editable ) ;
	}
	
	public EduinstProcess getProcess( int instId , int reportId ){
		EduinstProcess process = eduinstProcessDao.findBean( new MapParam<String,Object>()
				.push("instId", instId )
				.push("reportId" ,reportId ), EduinstProcess.class ) ;
		if( process == null ){//创建进度记录 .
			EduinstProcessParam editable = new EduinstProcessParam() ;
			editable.setInstId( instId );
			editable.setReportId( reportId );
			List<Collection> collections = eduinstCollectionDao.findEduinstCollection( null ,instId, Subject.getUser().getRoleCode(), reportId, Collection.class );
			editable.setTotalNum( collections == null ? 0 : collections.size() );
			eduinstProcessDao.createBean(editable, EduinstProcessParam.class ) ;
			process = eduinstProcessDao.findBean( new MapParam<String,Object>()
					.push("instId", instId )
					.push("reportId" ,reportId ), EduinstProcess.class ) ;
		}
		return process ;
	}
	
	/**
	 * 提交审核 .
	 * 
	 * @param schoolId
	 * @param reportId
	 * @param userId
	 */
	public void submitAudit( int instId , int reportId , int userId ){
		EduinstProcess process = eduinstProcessDao.findBean(new MapParam<String,Object>()
				.push("instId", instId )
				.push("reportId", reportId ), EduinstProcess.class ) ;
		if( !hasFinished( instId , reportId ) )
			throw new BusinessException("提交审核失败,当前填报未完成!") ;
		if( ! Arrays.asList(0 , 4).contains( process.getStatus() )  )
			throw new BusinessException("提交审核失败,当前填报已提交!") ;
		
		EduinstProcessParam editable = new EduinstProcessParam() ;
		editable.setStatus( 1 );//待审核
		editable.setSubmitTime(TimeUtil.now() );
		editable.setSubmitId( userId );
		eduinstProcessDao.updateBean("id", process.getId(), editable  ) ;
	}
	
	public boolean hasFinished( int instId , int reportId ){
		EduinstProcess process = eduinstProcessDao.findBean(new MapParam<String,Object>()
				.push("instId", instId )
				.push("reportId", reportId ), EduinstProcess.class ) ;
		return process != null && process.getTotalNum() == process.getFinishNum() ;
	}
	
	public List<Collection> findEduinstCollection(int eduinstId , String eduinstType , int reportingId ){
		return eduinstCollectionDao.findEduinstCollection( null ,eduinstId, eduinstType, reportingId, Collection.class ) ;
	}
}
