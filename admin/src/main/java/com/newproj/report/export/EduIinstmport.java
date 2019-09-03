package com.newproj.report.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.newproj.core.rest.annotation.Api;
import com.newproj.core.rest.support.RestActionSupporter;
import com.newproj.core.rest.support.StatusCode;
import com.newproj.core.util.ExcelUtil;
import com.newproj.report.eduinst.dto.Eduinst;
import com.newproj.report.eduinst.dto.EduinstParam;
import com.newproj.report.eduinst.service.EduinstService;
import com.newproj.report.school.dto.SchoolParam;
import com.newproj.report.sys.dto.City;
import com.newproj.report.sys.security.Security;
import com.newproj.report.sys.service.CityService;
import com.newproj.report.sys.service.DictService;
import com.newproj.report.sys.service.UserService;

@Api("/import")
public class EduIinstmport extends RestActionSupporter{
	
	@Autowired
	private EduinstService eduinstService;
	@Autowired
	private CityService cityService ;
	@Autowired
	private Security security;

	@RequestMapping(value="/importEduinstlUser",  produces = { "application/json;charset=UTF-8" })
	public String importEduinstlUser(@RequestParam  MultipartFile file) {
		String name = file.getOriginalFilename();
		if(!ExcelUtil.isExcel(name)) {
			return error(StatusCode.NOTEXCEL);
		}
		String type = "2003" ; 
		if(!name.endsWith(".xls")) {
			type = "2007";
		}
		List<String> msgList = new ArrayList<>();
		
		try {
			ExcelUtil excel = new ExcelUtil(file.getInputStream(), type);
			List<List<String>> list = excel.read(0);
		   	String[] constHader = {"教育局标识码","教育局名称","教育局类型","教育局归属地"};
			if(null==list) {
				return error(1, "excel 为空");
			}
			List<String> header = list.get(0);
			for(int i=0 ; i< header.size(); i++) {
				if(!header.get(i).equals(constHader[i])) {
					return error(1, "excel模板不正确");
				}
			}
			int successcout = 0 ;
			int errorcount = 0 ;
			for(int i=1 ; i<list.size() ; i++) {
				List<String>  records = list.get(i);
				if(records.size()<4) {
					return error(1, "excel模板不正确");
				}
				String eduCode = records.get(0);
				String eduName = records.get(1);
				String eduType = records.get(2);
				String eduArea = records.get(3);
				if (StringUtils.isNotBlank(eduCode) && StringUtils.isNotBlank(eduCode)
						&& StringUtils.isNotBlank(eduType) && StringUtils.isNotBlank(eduArea)
						) {
					if (getEdu("code", eduCode)) {
						msgList.add(String.format("第%s行,教育局标识码: %s已存在,未导入 <br>",i + "", eduCode));
						errorcount ++ ;
						continue;
					}
					if (getEdu("name", eduName)) {
						msgList.add( String.format("第%s行,教育局名称: %s已存在,未导入 <br>", i + "", eduName));
						errorcount ++  ;
						continue;
					}
					String stype = getSchoolType(eduType);
					if (StringUtils.isBlank(stype)) {
						msgList.add(String.format("第%s行,教育局类型: %s未匹配到,未导入 <br>", i + "", eduType));
						errorcount ++ ;
						continue;
					}
					City city = getArea(stype, eduArea);
					if (null==city) {
						msgList.add(String.format("第%s行,教育局归属地: %s填写不正确或不存在,未导入 <br>", i + "", eduArea));
						errorcount ++ ;
						continue;
					}
					EduinstParam edupParam = new EduinstParam();
					edupParam.setCode(eduCode);
					edupParam.setLoginName(eduCode);
					edupParam.setName(eduName);
					edupParam.setType(stype);
					edupParam.setCreateId(security.getLoginUser().getId());
					if(stype.equals("CITY")) {
						edupParam.setCityId(city.getId());
					}
					if(stype.equals("COUNTY")) {
						edupParam.setCityId(city.getParentId());
						edupParam.setCountyId(city.getId());
					}
					eduinstService.createEduinst(edupParam);
					successcout++;
				} else {
					msgList.add(String.format("第%s行,有空值,未导入 <br>", i + ""));
					errorcount ++ ;
				}
			}
			msgList.add(String.format("<font color='red'>处理结果：本次导入成功%s条,失败%s条</font>", successcout+"",errorcount+""));
		} catch (Exception e) {
			return error(1, "服务器异常");
		}
		return success(msgList);
	}
	
	private boolean getEdu(String key , Object value) {
		boolean res =  false ;
		Map<String, Object> params = new HashMap<>();
		params.put(key, value);
		Eduinst eduinst = eduinstService.findOne(params, Eduinst.class);
		if(null!=eduinst) {
			res = true ;
		}
		return res ;
	}
	
	private String getSchoolType(String name){
		String res = "";
		if(name.contains("区") || name.contains("县")) {
			res = "COUNTY";
		}else if(name.contains("市")) {
			res = "CITY" ;
		}else if(name.contains("省")) {
			 res = "PROVINCE";
		}
		return res ;
	}
	
	private Map<String, String> getArea(String name) {
		Map<String, String>  map = new HashMap<>();
		int pindex = name.indexOf("省")+1;
		int cindex = name.indexOf("市")+1;
		int quindex = name.indexOf("区")+1;
		int xian = name.indexOf("县")+1;
		int areaIndex = 0 ;
		if(quindex > 0 ){
			areaIndex = quindex ;
		}
		if(xian > 0 ){
			areaIndex = xian ;
		}
		int start = 0 ;
		String province = "";
		String city = "";
		String area = "" ;
		if(start< pindex) {
			province = name.substring(start,pindex);
			start = pindex;
		}
		if(start < cindex) {
			city = name.substring(start,cindex);
			start = cindex;
		}
		if(start < areaIndex) {
			area = name.substring(start,areaIndex);
		}
		map.put("PROVINCE", province);
		map.put("CITY", city);
		map.put("COUNTY", area);
		return map;
	}
	
	private City getArea(String type , String name) {
		City city = null ;
		Map<String, String>  map = getArea(name);
		String res = map.get(type);
		if(StringUtils.isNotBlank(res)) {
			Map<String, Object> params = new HashMap<>();
			params.put("name", res);
			city =  cityService.findOne(params, City.class);
		}
		return city;
	}
	
	
}
