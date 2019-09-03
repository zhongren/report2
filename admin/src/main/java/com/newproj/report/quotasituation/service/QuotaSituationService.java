package com.newproj.report.quotasituation.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newproj.core.page.PageParam;
import com.newproj.core.page.RemotePage;
import com.newproj.core.rest.support.AbstractBaseService;
import com.newproj.report.quotasituation.dal.dao.QuotaSituationDao;
import com.newproj.report.quotasituation.dal.dao.QuotaSituationDictDao;
import com.newproj.report.quotasituation.dto.QuotaSituationDict;
@Service
public class QuotaSituationService extends AbstractBaseService{

	@Autowired
	private QuotaSituationDao situationDao ;
	
	@Autowired
	private QuotaSituationDictDao dictDao ;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		setMapper( situationDao ) ;
	}
	
	public RemotePage<QuotaSituationDict> findDict( Map<String,Object> param , PageParam page , String ...properties ){
		return withMapper( dictDao ).findList(param, page, QuotaSituationDict.class , properties) ;
	}
	
	public QuotaSituationDict findDictBy( String by , Object value ,  String ...properties ){
		return withMapper( dictDao ).findBy(by, value, QuotaSituationDict.class , properties) ;
	}

}
