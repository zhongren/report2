package com.newproj.report.export;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.newproj.core.rest.annotation.Api;
import com.newproj.core.rest.support.RestActionSupporter;
import com.newproj.core.util.ExcelUtil;
import com.newproj.core.util.VMUtil;
import com.newproj.core.util.VelocityUtil;
import com.newproj.report.collection.dal.dao.CollectionDao;
import com.newproj.report.collection.dal.dao.SchoolTypeCollectionDao;
import com.newproj.report.collection.dto.Collection;
import com.newproj.report.collection.dto.SchoolTypeCollection;
import com.newproj.report.collection.service.CollectionService;
import com.newproj.report.export.bean.SchoolCollectionEx;
import com.newproj.report.school.dto.School;
import com.newproj.report.school.dto.SchoolCollection;
import com.newproj.report.school.service.SchoolCollectionService;
import com.newproj.report.school.service.SchoolService;
import com.newproj.report.sys.dto.City;
import com.newproj.report.sys.service.CityService;

import io.undertow.server.protocol.http.HttpServerConnection;

@Api("/export/sc")
public class SchoolCollectionExport  extends RestActionSupporter{
	@Autowired
	SchoolTypeCollectionDao schoolTypeCollectionDao;
	@Autowired
	SchoolCollectionService schoolCollectionService;
	@Autowired
	SchoolService schoolService;
	@Value("${report.id}")
	public String reportId;
	@Autowired
	CollectionDao collectionService;
	@Autowired
	CityService cityService;
	
	@RequestMapping(value="/all" ,produces = { "application/json;charset=UTF-8" })
	public String schoolCollection( @RequestParam("schoolType") String stype,@RequestParam("cityId") String cityId,@RequestParam("countyId") String countyId,HttpServletRequest request,HttpServletResponse response ){
		
		if(StringUtils.isBlank("schoolType")) {
			 return error(1,"请选择学校类型");
		 }
//		if(StringUtils.isBlank(cityId) && StringUtils.isBlank(countyId)) {
//			 return error(1,"请选择区域");
//		}
		List<Collection> lsc = collectionService.findSchoolCollection(Integer.valueOf(reportId), Integer.valueOf(stype), Collection.class);
		lsc.sort((c1,c2)->c1.getId()-c2.getId());
		List<Integer> cids = new ArrayList<>();
		for(Collection c : lsc) {
			cids.add(c.getId());
		}
		Map<String, Object>params = new HashMap<>();
		params.put("type", stype);
		if(StringUtils.isNotBlank(cityId)) {
			params.put("city", cityId);
		}
		if(StringUtils.isNotBlank(countyId)) {
			params.put("county", countyId);
		}
		List<School> slist = schoolService.findList(params, null, School.class);
		if(null==slist) {
			slist = new ArrayList<>();
		}
		List<SchoolCollectionEx> list = new ArrayList<>();
		params = new HashMap<>();
		Map<Integer, SchoolCollection> value = new HashMap<>();
		for(School s : slist) {
			s.setCity(cityService.findBy("id", s.getCity(), City.class, "name").getName());
			s.setCounty(cityService.findBy("id", s.getCounty(), City.class, "name").getName());
			params = new HashMap<>();
			params.put("school_id", s.getId());
			params.put("report_id", reportId);
			List<SchoolCollection> cls = schoolCollectionService.findList(params, null, SchoolCollection.class);
			if(null!=cls) {
				List<SchoolCollection> newcls = new ArrayList<>();
				for(SchoolCollection sc : cls) {
					 if(cids.contains(sc.getCollectionId())) {
						 value.put(sc.getCollectionId(), sc);
					 }
				}
				SchoolCollection nsc = null;
				for(Integer ci : cids) {
					if(value.get(ci)==null) {
						nsc = new SchoolCollection();
						nsc.setCollectionId(ci);
					}else {
						nsc = value.get(ci);
					}
					newcls.add(nsc);
				}
				for(SchoolCollection sc : newcls) {
					if(sc.getContent().startsWith("[")) {
						sc.setContent("已上传");
					}
					if(sc.getContent().startsWith("{\"radio")) {
						if(sc.getContent().contains("是")) {
							sc.setContent("是");
						}else {
							sc.setContent("否");
						}
					}
					
				}
				newcls.sort((c1,c2)->c1.getCollectionId()-c2.getCollectionId());
				list.add(new SchoolCollectionEx(s, newcls));
			}
			
		}
		Context context = new VelocityContext() ;
		context.put("collections", lsc ) ;
		context.put("schools", list) ;
		try {
			VelocityUtil.download("学校填报数据.xls", "/vm/sexcel.vm", context, request, response);
		} catch (IOException e) {
		}
		return success(list);
	}
	
}
