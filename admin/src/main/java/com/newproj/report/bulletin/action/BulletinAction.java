package com.newproj.report.bulletin.action;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.newproj.core.beans.MapParam;
import com.newproj.core.exception.BusinessException;
import com.newproj.core.page.RemotePage;
import com.newproj.core.rest.annotation.Api;
import com.newproj.core.rest.annotation.Delete;
import com.newproj.core.rest.annotation.Get;
import com.newproj.core.rest.annotation.Post;
import com.newproj.core.rest.annotation.Put;
import com.newproj.core.rest.support.ParamModal;
import com.newproj.core.rest.support.RestActionSupporter;
import com.newproj.core.util.NumberUtil;
import com.newproj.core.util.TimeUtil;
import com.newproj.report.bulletin.constant.BulletinConstant.Type;
import com.newproj.report.bulletin.dto.Bulletin;
import com.newproj.report.bulletin.dto.BulletinParam;
import com.newproj.report.bulletin.service.BulletinService;
import com.newproj.report.context.Subject;
import com.newproj.report.context.trans.TransContext;
import com.newproj.report.eduinst.service.EduinstService;
import com.newproj.report.school.service.SchoolService;
import com.newproj.report.sys.constant.UserConstant.InstType;
@Api("/bulletin")
public class BulletinAction extends RestActionSupporter{

	@Autowired
	private BulletinService bulletinService ;
	
	@Autowired
	private SchoolService schoolService ;
	
	@Autowired
	private EduinstService eduinstService ;
	
	@Autowired
	private TransContext trans ;
	
	@Get("")
	public String findBulletin( ParamModal modal ){
		Map<String,Object> param = modal.getParam("?title" , "type" , "status","categoryCode") ;
		InstType instType = InstType.parse( Subject.getUser().getInstType() ) ;
		if( instType != null ){
			if( instType == InstType.SCHOOL )
				param.put("schoolId", Subject.getUser().getInstId() ) ;
			else if ( instType == InstType.EDUINST )
				param.put("eduinstId" , Subject.getUser().getInstId() ) ;
		}
		RemotePage<Bulletin> pageData = bulletinService.findBulletin(param, modal.getPageParam() ) ;
		if( pageData != null && !pageData.isEmpty() ){
			trans.transDict("BULLETIN_TYPE", pageData, "type", "type") 
			.transUser(pageData, Arrays.asList("createId" ,"publishId"), Arrays.asList("createName","publishName"))
			.transDict("BULLETIN_CATEGORY", pageData, "categoryCode", "categoryCode" );
		}
		return success( pageData , pageData == null ? 0 : pageData.getTotal() ) ;
	}
	
	@Get("/{id}")
	public String getBulletin( @PathVariable("id") int id ,
			@RequestParam( defaultValue = "none" , value="_flat") String flat ){
		Bulletin bulletin = bulletinService.findBy("id", id, Bulletin.class ) ;
		if( bulletin == null || !flat.equals("num") ) 
			return success( bulletin ) ;
		
		boolean updateReadNum = false ;
		if( Type.NOTICE.name().equalsIgnoreCase( bulletin.getType() ) ){
			InstType instType = InstType.parse( Subject.getUser().getInstType() ) ;
			int instId = NumberUtil.parseInt( Subject.getUser().getInstId() , 0 ) ;
			if( ( instType == InstType.EDUINST &&  instId == bulletin.getEduinstId() ) 
						|| (instType == InstType.SCHOOL && instId == bulletin.getSchoolId() ) ){
				//更新已读状态
				updateReadNum = true ;
			}
		}
		if( Type.PUBLIC.name().equalsIgnoreCase( bulletin.getType() ) )
			updateReadNum = true ;
		if( updateReadNum )
			bulletinService.update("id", bulletin.getId() , new MapParam<String,Object>()
					.push("readNum" , NumberUtil.parseInt( bulletin.getReadNum() , 0 )+1 ) ) ;
		
		return success( bulletin ) ;
	}
	
	/**
	 * 添加公告 .
	 * 
	 * @param param
	 * @return
	 */
	@Post("")
	public String createPublictBulletin( @RequestBody @Validated BulletinParam param ){
		param.setCreateId( Subject.getUserId() );
		param.setCreateTime( TimeUtil.now() );
		param.setPublishId( Subject.getUserId() );
		param.setPublishTime(TimeUtil.now() );
		param.setStatus(1);
		return success( bulletinService.create( param , BulletinParam.class  ) ) ;
	}
	
	@Put("/{id}")
	public String updateBulletin( @PathVariable("id") int id  , 
			 @RequestBody @Validated BulletinParam param  ){
		if(bulletinService.findBy("id", id, Bulletin.class , "id") == null )
			throw new BusinessException("数据不存在!") ;
		bulletinService.update("id", id , param ) ;
		return success(null) ;
	}
	
	/**
	 * 删除公告 .
	 * 
	 * @param id
	 * @return
	 */
	@Delete("/{id}")
	public String deletePublicBulletin( @PathVariable("id") int id ){
		bulletinService.delete( new MapParam<String,Object>().push("id" , id ) ) ;
		return success( null ) ;
	}
	
}
