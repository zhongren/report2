package com.newproj.report.quota.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.newproj.core.beans.MapParam;
import com.newproj.core.rest.support.AbstractBaseService;
import com.newproj.core.util.JoinUtil;
import com.newproj.core.util.TimeUtil;
import com.newproj.report.context.Subject;
import com.newproj.report.eduinst.dal.dao.EduinstCollectionDao;
import com.newproj.report.eduinst.dto.EduinstCollection;
import com.newproj.report.quota.dal.dao.QuotaDao;
import com.newproj.report.quota.dal.dao.SubaccountQuotaDao;
import com.newproj.report.quota.dto.Quota;
import com.newproj.report.quota.dto.SubaccountQuota;
import com.newproj.report.quota.dto.SubaccountQuotaParam;
import org.springframework.util.CollectionUtils;

@Service
public class QuotaService extends AbstractBaseService{

	@Autowired
	private QuotaDao quotaDao ;

	@Autowired
	private SubaccountQuotaDao subaccQuotaDao ;

	@Autowired
	private EduinstCollectionDao eduinstCollectionDao ;

	@Override
	public void init() {
		setMapper( quotaDao ) ;
	}

	public List<Quota> quotaRank( List<Quota> quotas ){
		if( quotas == null || quotas.isEmpty() ){
			return null ;
		}
		Quota [] array = quotas.toArray( new Quota [] {} ) ;
		for( int i = 0 ; i < array.length - 1 ; i ++ ){
			if( array[i].getSubQuota() != null && !array[i].getSubQuota().isEmpty() ){
				quotaRank( array[i].getSubQuota() ) ;
			}
			for( int j = 0 ; j < array.length - 1 ; j ++ ){
				if( array[j].getRank() < array[j+1].getRank() ) continue ;
				if( array[j].getRank() .equals( array[j+1].getRank() ) && array[j].getId() < array[j+1].getId() ) continue ;
				Quota tmp = array[j] ;
				array[j] = array[j+1] ;
				array[j+1] = tmp ;
			}
		}
		quotas = Arrays.asList( array ) ;
		return quotas ;
	}

	public List<Quota> findSchoolQuota( Integer schoolType , int reportId ){
		return quotaDao.findSchoolQuota( schoolType , reportId , Quota.class ) ;
	}

	public List<Quota> findEduinstQuota( String eduinstType , int reportId ){

		List<Quota>  list = quotaDao.findEduinstQuota(eduinstType, reportId, Quota.class );
		List<Map<Integer, Integer>> countList = eduinstCollectionDao.findEduinstIsEnd( Subject.getUser().getInstId(), eduinstType, reportId);
		Map<Integer, Boolean> countMap = new HashMap<>();
		for(Map<Integer, Integer> mp : countList) {
			 int all =  Integer.parseInt(mp.get("allCount")+"");
			 int has = Integer.parseInt(mp.get("hasCount")+"");
			 int qid = mp.get("qid");
			 countMap.put(qid, all >has?false:true );
		}
		if(!CollectionUtils.isEmpty(list)){
		for(Quota q : list) {
			boolean res = countMap.get(q.getId());
			q.setFinished(res);
		}}
		return  list;
	}

	public List<Quota> structSubQuota( List<Quota> dataList ){
		if( dataList == null || dataList.isEmpty() ){
			return null ;
		}
		List<Quota> quota1List = quotaDao.findBeanListBy("id",
				JoinUtil.fieldsValue( dataList , "parentId"), Quota.class  ) ;
		if( quota1List == null || quota1List.isEmpty() ){
			return dataList ;
		}
		for( Quota quota : quota1List ){
			for( Quota quota2 : dataList ){
				if( !quota2.getParentId() .equals( quota .getId() ) )
					continue ;
				if( quota.getSubQuota() == null )
					quota.setSubQuota( new ArrayList<Quota>() );
				quota.getSubQuota().add( quota2 ) ;
			}
		}
		return quota1List ;
	}

	public List<Integer> findSubaccQuota( Integer ... usersId ){
		if( usersId == null || usersId.length == 0 )
			return null ;
		List<SubaccountQuota> subaccQuotas = subaccQuotaDao.findBeanListBy(
				"userId", Arrays.asList(usersId ) , SubaccountQuota.class , "userId" , "quotaId") ;
		if( subaccQuotas == null || subaccQuotas.isEmpty() )
			return null ;
		List<Integer> quotasId = new ArrayList<Integer>() ;
		for( SubaccountQuota subaccQuota : subaccQuotas )
			quotasId.add( subaccQuota.getQuotaId() ) ;

		return quotasId ;
	}

	/**
	 * 更新子账号二级指标关系 .
	 *
	 * @param subaccId
	 * @param params
	 */
	@Transactional( propagation = Propagation.REQUIRED )
	public  void updateSubaccQuota( int subaccId , List<SubaccountQuotaParam> params ){
		//删除已存在的关系
		subaccQuotaDao.delete( new MapParam<String,Object>().push("userId" , subaccId ) ) ;
		//更新关系
		if( params == null || params.isEmpty() )
			return ;
		for( SubaccountQuotaParam param : params ){
			param.setCreateTime(TimeUtil.now() );
			param.setUserId( subaccId );
			subaccQuotaDao.createBean( param , SubaccountQuotaParam.class ) ;
		}
	}
}
