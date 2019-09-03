package com.newproj.report.collection.service;

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
import com.newproj.report.collection.dal.dao.CollectionDao;
import com.newproj.report.collection.dal.dao.SchoolTypeCollectionDao;
import com.newproj.report.collection.dto.Collection;
import com.newproj.report.collection.dto.CollectionParam;
import com.newproj.report.collection.dto.SchoolTypeCollection;
import com.newproj.report.collection.dto.SchoolTypeCollectionParam;
@Service
public class CollectionService extends AbstractBaseService{

	@Autowired
	private CollectionDao collectionDao ;
	
	@Autowired
	private SchoolTypeCollectionDao typeCollectionDao ;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		setMapper( collectionDao ) ;
	}
	
	public List<Collection> findSchoolCollectionWithQuota( int reportId , int schoolId , int schoolType , int quotaId ){
		return collectionDao.findSchoolCollectionWithQuota(reportId, schoolId , schoolType, quotaId, Collection.class ) ;
	}
	
	public int createCollection( CollectionParam param ){
		param.setCreateTime( TimeUtil.now() );
		return NumberUtil.parseInt( collectionDao.createBean( param , CollectionParam.class ), 0 ) ;
	}
	
	public void updateCollection( int id , CollectionParam param ){
		if( collectionDao.findMapBy("id", id , "id") == null )
			throw new BusinessException("数据不存在,更新失败!") ;
		param.setUpdateTime( TimeUtil.now() );
		collectionDao.updateBean("id", id , param ) ;
	}
	
	public List<Collection> findCollectionBySchoolType( int type ){
		return collectionDao.findCollectionBySchoolType( type , Collection.class ) ;
	}
	
	public List<SchoolTypeCollection> findTypeCollection(
			String schoolType , Integer collectionId ){
		List<SchoolTypeCollection> dataList = typeCollectionDao.findBeanList( 
				new MapParam<String,Object>()
				.push("collectionId" , collectionId )
				.push("schoolType" , schoolType ), SchoolTypeCollection.class ) ;
		return dataList ;
	}
	
	@Transactional( propagation = Propagation.REQUIRED)
	public void createColectionType( int collectionId , List<SchoolTypeCollectionParam> params , int userId ){
		//Delete all .
		typeCollectionDao.delete( new MapParam<String,Object>()
				.push("collectionId" , collectionId ) ) ;
		for( SchoolTypeCollectionParam param : params ){
			param.setCollectionId(collectionId);
			param.setCreateTime(TimeUtil.now() );
			typeCollectionDao.createBean( param , SchoolTypeCollectionParam.class ) ;
		}
	}
}
