package com.newproj.report.quotasituation.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.newproj.core.exception.BusinessException;
import com.newproj.core.page.RemotePage;
import com.newproj.core.rest.annotation.Api;
import com.newproj.core.rest.annotation.Get;
import com.newproj.core.rest.annotation.Post;
import com.newproj.core.rest.annotation.Put;
import com.newproj.core.rest.support.ParamModal;
import com.newproj.core.rest.support.RestActionSupporter;
import com.newproj.core.util.TimeUtil;
import com.newproj.report.context.Subject;
import com.newproj.report.context.trans.TransContext;
import com.newproj.report.quotasituation.dto.QuotaSituation;
import com.newproj.report.quotasituation.dto.QuotaSituationDict;
import com.newproj.report.quotasituation.dto.QuotaSituationParam;
import com.newproj.report.quotasituation.service.QuotaSituationService;
@Api("quotaSituation")
public class QuotaSituationAction extends RestActionSupporter{

	@Autowired
	private QuotaSituationService situationService ;
	
	@Autowired
	private TransContext trans ;
	
	@Get
	public String findList( ParamModal modal ){
		RemotePage<QuotaSituation> pageData = situationService.findList( 
				modal.getParam("id","?name","instType","schoolType"), modal.getPageParam() , QuotaSituation.class ) ;
		if( pageData != null ){
			trans.transDict("SITUATION_INST_TYPE", pageData , "instType", "instType" ) ;
			trans.transDict("SITUATION_SCHOOL_TYPE", pageData, "schoolType", "schoolType" ) ;
		}
		return success( pageData , pageData == null ? 0 : pageData.getTotal() );
	}
	
	@Get("/{id}")
	public String get( @PathVariable("id") int id ){
		return success( situationService.findBy("id", id , QuotaSituation.class ) ) ;
	}
	
	@Post
	public String create( @RequestBody QuotaSituationParam param ){
		QuotaSituationDict dict = situationService.findDictBy("id", param.getDictId() ) ;
		if( dict == null )
			throw new BusinessException("监测点不存在!") ;
		param.setName( dict.getName() );
		param.setCreateId( Subject.getUserId() );
		param.setCreateTime( TimeUtil.now() );
		return success( situationService.create( param , QuotaSituationParam.class ) ) ;
	}
	
	@Put("/{id}")
	public String update( @PathVariable("id") int id , @RequestBody QuotaSituationParam param ){
		if( situationService.findBy("id", id , QuotaSituation.class , "id") == null )
			throw new BusinessException("数据不存在!") ;
		
		QuotaSituationDict dict = situationService.findDictBy("id", param.getDictId() ) ;
		if( dict == null )
			throw new BusinessException("监测点不存在!") ;
		param.setName( dict.getName() );
		
		situationService.update("id", id , param ) ;
		return success( null ) ;
	}
	
	@Get("/dict")
	public String findDict( ParamModal modal ){
		RemotePage<QuotaSituationDict> pageData = situationService.findDict( modal.getParam("id" , "?name"), modal.getPageParam() ) ;
		return success( pageData , pageData == null ? 0 : pageData.getTotal() ) ;
	}
	
	@Get("/dict/{id}")
	public String getDict( @PathVariable("id") int id  ){
		return success( situationService.findDictBy("id", id ) ) ;
	}
	
}
