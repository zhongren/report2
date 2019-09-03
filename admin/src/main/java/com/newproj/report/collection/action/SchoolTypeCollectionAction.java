package com.newproj.report.collection.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.newproj.core.beans.MapParam;
import com.newproj.core.page.PageParam;
import com.newproj.core.rest.annotation.Api;
import com.newproj.core.rest.annotation.Get;
import com.newproj.core.rest.annotation.Post;
import com.newproj.core.rest.support.RestActionSupporter;
import com.newproj.core.util.JoinUtil;
import com.newproj.core.validation.groups.CGroup;
import com.newproj.report.collection.dto.Collection;
import com.newproj.report.collection.dto.SchoolTypeCollection;
import com.newproj.report.collection.dto.SchoolTypeCollectionParam;
import com.newproj.report.collection.service.CollectionService;
import com.newproj.report.context.Subject;
import com.newproj.report.quota.constant.QuotaConstant.QuotaType;
import com.newproj.report.quota.dto.Quota;
import com.newproj.report.quota.service.QuotaService;
@Api("/schoolTypeCollection")
public class SchoolTypeCollectionAction  extends RestActionSupporter{

	@Autowired
	private QuotaService quotaService ;
	
	@Autowired
	private CollectionService collectionService ;
	
	@Get("/collectionWithQuota")
	public String findQuotaCollection(){
		List<Quota> quotas = quotaService.findList( new MapParam<String,Object>().push("level" , 2 ).push("type" , QuotaType.SCHOOL ),
				new PageParam().orderBy("rank", "ASC"), Quota.class ) ;
		if( quotas == null || quotas.isEmpty() )
			return success( null ) ;
		List<Collection> collections = collectionService.findList(new MapParam<String,Object>()
				.push("quotaId" , JoinUtil.fieldsValue(quotas, "id") ) , null , Collection.class ) ;
		for( Quota quota : quotas ){
			quota.setCollections( new ArrayList<Collection>() );
			for( Collection collection : collections ){
				if( quota.getId() .equals( collection.getQuotaId() ) )
					quota.getCollections().add( collection ) ;
			}
		}
		return success( quotaService.quotaRank( quotaService.structSubQuota( quotas ) ) ) ;
	}
	
	@Get("/{collectionId}/types")
	public String findCollectionSchoolType( @PathVariable("collectionId") int collectionId ){
		List<SchoolTypeCollection> dataList = collectionService.findTypeCollection( null , collectionId ) ;
		return success( dataList );
	}
	
	@Post("/{collectionId}/types")
	public String createTypeCollection(  @PathVariable("collectionId") int collectionId  ,
			@RequestBody @Validated(CGroup.class ) List<SchoolTypeCollectionParam> params ){
		collectionService.createColectionType(collectionId, params , Subject.getUserId() );
		return success( null );
	}
}
