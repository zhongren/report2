package com.newproj.report.school.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.newproj.core.exception.BusinessException;
import com.newproj.core.rest.support.AbstractBaseService;
import com.newproj.report.collection.dto.Collection;
import com.newproj.report.collection.service.CollectionService;
import com.newproj.report.export.bean.SchoolClassesNumBean;
import com.newproj.report.school.dal.dao.SchoolClassesNumsDao;
import com.newproj.report.school.dto.SchoolCollectionParam;

@Service
public class SchoolClassesNumsService extends AbstractBaseService{
	
	@Autowired
	private  SchoolClassesNumsDao schoolClassesNumsDao;
	
	@Autowired
	private CollectionService collectionService ;
	
	@Autowired
	private SchoolCollectionService schoolCollectionService;
	
	
	public void delete(int schoolId,int year) {
		Map<String, Object>  param = new HashMap<>();
		param.put("school_id", schoolId);
		param.put("year", year);
		schoolClassesNumsDao.delete(param);
	}
	
	public List<SchoolClassesNumBean> getClasses(int schoolId,int year){
		Map<String, Object>  params = new HashMap<>();
		params.put("school_id", schoolId);
		params.put("year", year);
		return schoolClassesNumsDao.findBeanList(params, SchoolClassesNumBean.class);
	}
	
	@Transactional( propagation = Propagation.REQUIRED )
	public void createClassesNums( int reportingId , int schoolId , int collectinId ,
			List<SchoolClassesNumBean> classNums ){
		delete( schoolId , reportingId );
		for( SchoolClassesNumBean classNum : classNums ){
			this.create( classNum , SchoolClassesNumBean.class, "name","classes","nums","schoolId","year" ) ;
		}
		createCollection( reportingId , schoolId , collectinId );
	}
	
	private void createCollection( int reportingId , int schoolId , int collectionId ){
		Collection colleciton = collectionService.findBy("id", collectionId, Collection.class ) ;
		if( colleciton == null )
			throw new BusinessException("采集项不存在!") ;
		
		SchoolCollectionParam param = new SchoolCollectionParam() ;
		param.setReportId( reportingId );
		param.setCollectionId( collectionId );
		param.setQuotaId( colleciton.getQuotaId() );
		param.setSchoolId( schoolId );
		param.setContent("绝对班额请查看班额表!");
		schoolCollectionService.saveSchoolCollection( Arrays.asList( param ) ) ;
	}
	

	@Override
	public void init() {
		setMapper(schoolClassesNumsDao);
	}

}
