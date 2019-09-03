package com.newproj.report.export;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.newproj.core.rest.annotation.Api;
import com.newproj.core.rest.support.RestActionSupporter;
import com.newproj.core.util.VelocityUtil;
import com.newproj.report.eduinst.dal.dao.EduinstCollectionDao;
import com.newproj.report.eduinst.service.EduinstCollectionService;
import com.newproj.report.export.bean.ClssSizeBean;
import com.newproj.report.export.bean.EduCollectionBean;

@Api("/export")
public  class ClassSize extends RestActionSupporter{
	
	@Value("${report.id}")
	public String reportId;
	
	@Autowired
	private EduinstCollectionDao esd ;
	@Autowired
	EduinstCollectionService ec ;
	
	@RequestMapping("/eduCollections")
	public String eduCollections(HttpServletRequest request , HttpServletResponse response) {
		List<Map<String, Object>> lists = esd.findEduCollection(Integer.valueOf(reportId));
		List<EduCollectionBean> ecbs = new ArrayList<>();
		Map<String, String> edus = new HashMap<>();
		for(Map<String, Object> map : lists) {
			edus.put(map.get("code")+"", map.get("name")+"");
		}
		for(String s : edus.keySet()) {
			EduCollectionBean eb = new EduCollectionBean();
			List<String> cols = new ArrayList<>();
			eb.setCode(s);
			eb.setName(edus.get(s));
			for(Map<String, Object> map : lists) {
				if(s.equals(map.get("code")+"")) {
					cols.add(map.get("content")+"");
				}
			}
			eb.setCollections(cols);
			ecbs.add(eb);
		}
		List<String> names = esd.finaEduAllCollection();
		 
		Context context = new VelocityContext();
		context.put("namses", names);
		context.put("euds", ecbs);
		System.out.println(new Gson().toJson(ecbs));
		try{
			VelocityUtil.download("教育局填报数据.xls", "/vm/educollections.vm", context, request, response);
		}catch(IOException e) {
			e.printStackTrace();
		}
		return reportId;
	}
	
	@RequestMapping("/classSize")
	public String exportClassSize(HttpServletRequest request , HttpServletResponse response) {
		List<ClssSizeBean> primary = esd.findPrimaryClassSize();
		List<ClssSizeBean> middle = esd.findMiddleClassSize();
		System.out.println(new Gson().toJson(primary));
		System.out.println(new Gson().toJson(middle));
		Context context = new VelocityContext();
		context.put("primary", primary);
		context.put("middle", middle);
		try{
			VelocityUtil.download("城乡分布与绝对班额.xls", "/vm/class_size.vm", context, request, response);
		}catch(IOException e) {
			e.printStackTrace();
		}
		return reportId;
	}
	
}
