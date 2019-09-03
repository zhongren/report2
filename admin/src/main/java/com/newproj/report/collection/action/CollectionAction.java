package com.newproj.report.collection.action;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.newproj.core.page.RemotePage;
import com.newproj.core.rest.annotation.Api;
import com.newproj.core.rest.annotation.Get;
import com.newproj.core.rest.annotation.Post;
import com.newproj.core.rest.annotation.Put;
import com.newproj.core.rest.support.ParamModal;
import com.newproj.core.rest.support.RestActionSupporter;
import com.newproj.core.util.JoinUtil;
import com.newproj.core.validation.groups.CGroup;
import com.newproj.core.validation.groups.UGroup;
import com.newproj.report.collection.dto.Collection;
import com.newproj.report.collection.dto.CollectionParam;
import com.newproj.report.collection.service.CollectionService;
import com.newproj.report.context.Subject;
import com.newproj.report.context.trans.TransContext;
import com.newproj.report.quota.dto.Quota;
import com.newproj.report.quota.service.QuotaService;
@Api("/collection")
public class CollectionAction extends RestActionSupporter{

	@Autowired
	private CollectionService collectionService ;
	
	@Autowired
	private QuotaService quotaService ;
	
	@Autowired
	private TransContext trans ;
	
	@Get("")
	public String findCollection( ParamModal modal ){
		Map<String,Object> param = modal.getParam("id","?title" , "quotaId") ; 
		RemotePage<Collection> pageData = collectionService.findList( param , modal.getPageParam() , Collection.class 	) ;
		if( pageData == null || pageData.isEmpty() )
			return success( null ) ;
		transQuota( pageData ) ;
		trans.transDict("COLLECTION_TYPE", pageData, "valueType", "valueType")
		.transUser(pageData, Arrays.asList("createId"), Arrays.asList("createName") );
		return success( pageData , pageData.getTotal() );
	}
	
	@Get("/type/{type}")
	public String findScoolTypeCollection( @PathVariable("type") int type ){
		return success( collectionService.findCollectionBySchoolType( type ) ) ;
	}
	
	private void transQuota( List<Collection> dataList ){
		if( dataList == null || dataList.isEmpty() )
			return ;
		JoinUtil.join(dataList, new String[]{"quotaName"}, "quotaId",
				quotaService.findListBy("id", JoinUtil.fieldsValue(dataList, "quotaId"), 
						Quota.class , "id","name"), new String[]{"name"}, "id");
	}
	
	@Post("")
	public String createCollection( @RequestBody @Validated(CGroup.class) CollectionParam param ){
		param.setCreateId( Subject.getUserId() );
		return success( collectionService.createCollection( param ) ) ;
	}
	
	@Get("/{id}")
	public String getCollection( @PathVariable( "id") int id ){
		return success( collectionService.findBy("id", id , Collection.class ) ) ;
	}
	
	@Put("/{id}")
	public String updateCollection( @PathVariable( "id") int id ,  
			@RequestBody @Validated(UGroup.class) CollectionParam param ){
		param.setUpdateId(Subject.getUserId() );
		collectionService.updateCollection(id, param);
		return success( null ) ;
	}
	
	public String findQuotaCollection(){
		
		return success( null ) ;
	}
}
