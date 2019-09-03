package com.newproj.report.quota.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.newproj.core.page.PageParam;
import com.newproj.core.rest.annotation.Api;
import com.newproj.core.rest.annotation.Get;
import com.newproj.core.rest.support.ParamModal;
import com.newproj.core.rest.support.RestActionSupporter;
import com.newproj.report.context.trans.TransContext;
import com.newproj.report.quota.dto.Quota;
import com.newproj.report.quota.service.QuotaService;

@Api("/quota")
public class QuotaAction extends RestActionSupporter{

	@Autowired
	private QuotaService quotaService ;
	
	@Autowired
	private TransContext trans ;
	
	@Get("/option")
	public String findQuotaOption( ParamModal modal ){
		include(Quota.class, "id","name","type","typeName","level") ;
		List<Quota> quotas = quotaService.findList( 
				modal.getParam("id" , "parentId" , "type" ,"level"),
					new PageParam().orderBy("rank", "DESC"), Quota.class ) ;
		trans.transDict("QUOTA_TYPE", quotas, "type", "typeName" ) ;
		return success( quotas ) ;
	}
}
