package com.newproj.report.export;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.newproj.report.context.trans.TransContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.newproj.core.page.RemotePage;
import com.newproj.core.rest.annotation.Api;
import com.newproj.core.rest.annotation.Get;
import com.newproj.core.rest.support.ParamModal;
import com.newproj.core.rest.support.RestActionSupporter;
import com.newproj.core.rest.support.StatusCode;
import com.newproj.core.util.ExcelUtil;
import com.newproj.core.util.StringUtil;
import com.newproj.report.context.Subject;
import com.newproj.report.eduinst.dto.Eduinst;
import com.newproj.report.eduinst.service.EduinstService;
import com.newproj.report.export.bean.SchoolClassesNumBean;
import com.newproj.report.export.bean.SchoolParmBean;
import com.newproj.report.reporting.dto.Reporting;
import com.newproj.report.reporting.service.ReportingService;
import com.newproj.report.school.dal.dao.SchoolTypeDao;
import com.newproj.report.school.dto.School;
import com.newproj.report.school.dto.SchoolCollection;
import com.newproj.report.school.dto.SchoolParam;
import com.newproj.report.school.dto.SchoolType;
import com.newproj.report.school.service.SchoolClassesNumsService;
import com.newproj.report.school.service.SchoolCollectionService;
import com.newproj.report.school.service.SchoolService;
import com.newproj.report.sys.dto.City;
import com.newproj.report.sys.dto.Dict;
import com.newproj.report.sys.dto.User;
import com.newproj.report.sys.security.Security;
import com.newproj.report.sys.service.CityService;
import com.newproj.report.sys.service.DictService;
import com.newproj.report.sys.service.UserService;


@Api("/import")
public class SchoolImport extends RestActionSupporter{
	@Value("${import.xiao.collection.id}")
	private String xiaoCollectionId;
	@Value("${import.middle.collection.id}")
	private String middleCollectionId;
	@Value("${import.high.collection.id}")
	private String highCollectionId;
	@Value("${report.id}")
	private String reportId;

	@Autowired
	private SchoolClassesNumsService schoolClassesNumsService;
	@Autowired
	private Security security;
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private CityService cityService ;
	@Autowired
	private SchoolCollectionService schoolCollectionService;
	@Autowired
	private SchoolTypeDao schoolTypeDao;
	@Autowired
	private EduinstService eduinstService;
	@Autowired
	private  DictService dictService ;
	@Autowired
	private UserService userService ;
	@Autowired
	private TransContext transContext ;
	private static final String PASSWORD = "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92";

	@Autowired
	private ReportingService reportingService ;

	@RequestMapping(value="/{collectionId}/schoolClasses",  produces = { "application/json;charset=UTF-8" })
	public String importSchoolClasses(@RequestParam("schoolClassFile")  MultipartFile uploadFile ,
			@PathVariable("collectionId") int collectionId )  {
		Reporting reporting = reportingService.getPresent() ;
		if( reporting == null )
			return error( 1 , "填报已关闭!") ;

		String name = uploadFile.getOriginalFilename();
		User user = security.getLoginUser();
		try {
			if(!ExcelUtil.isExcel(name)) {
				return error(StatusCode.NOTEXCEL);
			}
			String type = "2003" ;
			if(!name.endsWith(".xls")) {
				type = "2007";
			}
			ExcelUtil excel = new ExcelUtil(uploadFile.getInputStream(), type);
	    	List<List<String>> lists =  excel.read(0, 1, 3);
	    	if(StringUtil.isEmpty(lists)) {
	    		return error(StatusCode.NULLEXCEL);
	    	}
	    	SchoolParmBean spb = getSchool(user.getInstId());
			if(StringUtil.isEmpty(lists.get(0)) || !lists.get(0).get(2).equals(spb.getCityName())) {
				return error(1 , "大市名称和当前用户不符合");
			}
			if(StringUtil.isEmpty(lists.get(1)) || !lists.get(1).get(2).equals(spb.getAreaName())) {
				return error(1 , "区县名称和当前用户不符合");
			}
			if(StringUtil.isEmpty(lists.get(2)) || !lists.get(2).get(2).equals(spb.getSchoolName())) {
				return error(1 , "学校名称和当前用户不符合");
			}
			List<List<String>>  lists2 = excel.read(0, 5, Integer.MAX_VALUE);
			List<SchoolClassesNumBean> scnms = new ArrayList<SchoolClassesNumBean>();
			int num = 0;
      			if(StringUtil.isEmpty(lists2)) {
				return error(1, "数据格式有误");
			}
			for(List<String> list : lists2) {
				if(StringUtil.isEmpty(list.get(0)) && StringUtil.isEmpty(list.get(1)) &&  StringUtil.isEmpty(list.get(2)) && StringUtil.isEmpty(list.get(3))) {
					  break ;
				}
				if( StringUtil.isEmpty(list.get(1)) || StringUtil.isEmpty(list.get(2)) || StringUtil.isEmpty(list.get(3))) {
					  return error(1 , "所填写数据不完整(每一行年级,班级号,人数都不能有空值)！");
				}
				SchoolClassesNumBean scnm = new SchoolClassesNumBean();
				scnm.setName(list.get(1));
				scnm.setClasses(Integer.parseInt(list.get(2)));
				scnm.setNums(Integer.parseInt(list.get(3)));
				//scnm.setYear(Integer.parseInt(getSysYear()));
				scnm.setYear( reporting.getId() ); //填报年份用填报年份的ID .
				scnm.setSchoolId(user.getInstId());
				scnms.add(scnm);
				num += scnm.getNums();
			}
			if(spb.getNums()<=0) {
				return error(1 , "请先填报绝对班额");
			}
			if(num<=0 || num!=spb.getNums()) {
				return error(1 , "导入总人数和所填报总人数不符合");
			}
			schoolClassesNumsService.createClassesNums(reporting.getId(), user.getInstId(), collectionId , scnms );

		}catch(Exception e) {
			return error(StatusCode.ERROR);
		}
		return success(null);
	}


	public static String getSysYear() {
        Calendar date = Calendar.getInstance();
        String year = String.valueOf(date.get(Calendar.YEAR));
        return year;
	}

	public SchoolParmBean getSchool(int sid) {
		SchoolParmBean spb = new SchoolParmBean();
		Map<String,Object> params = new HashMap<>();
		params.put("id", sid);
		School school = schoolService.findOne(params, School.class);
		spb.setSchoolName(school.getName());
		params.put("id", school.getCity());
		City city = cityService.findOne(params, City.class);
		spb.setCityName(city.getName());
		params.put("id", school.getCounty());
		City area = cityService.findOne(params, City.class);
		spb.setAreaName(area.getName());

		params.put("id", school.getType());
		SchoolType st = schoolTypeDao.findBean(params, SchoolType.class);
	//	double high =  st.getHighRate().doubleValue();
		double middle = st.getMiddleRate().doubleValue();
		double xiao = st.getPrimaryRate().doubleValue();
		int xiaoNum = 0 ;
		int middleNum = 0;
	//	int highNum = 0;
		Map<String,Object> params2 = new HashMap<>();
		if(xiao > 0 ) {
			params2.put("school_id", sid);
			params2.put("collection_id", xiaoCollectionId);
			params2.put("report_id", reportId);
			SchoolCollection sc = schoolCollectionService.findOne(params2, SchoolCollection.class);
			if(null!=sc) {
				xiaoNum = Integer.valueOf(sc.getContent());
			}
		}
		if(middle > 0 ) {
			params2.put("school_id", sid);
			params2.put("collection_id", middleCollectionId);
			params2.put("report_id", reportId);
			SchoolCollection sc = schoolCollectionService.findOne(params2, SchoolCollection.class);
			if(null!=sc) {
				middleNum = Integer.valueOf(sc.getContent());
			}
		}
//		if(high > 0 ) {
//			params2.put("school_id", sid);
//			params2.put("collection_id", highCollectionId);
//			params2.put("report_id", reportId);
//			SchoolCollection sc = schoolCollectionService.findOne(params2, SchoolCollection.class);
//			if(null!=sc) {
//				highNum = Integer.valueOf(sc.getContent());
//			}
//		}
//		int sumNum = xiaoNum+middleNum+highNum;
		int sumNum = xiaoNum+middleNum;
		spb.setNums(sumNum);
		return spb;
	}

	@Get("/getSchoolClasses")
	public String getClasses( ParamModal modal ) {
		Reporting reporting = reportingService.getPresent() ;
		if( reporting == null )
			return success( null ) ;
		modal.put("schoolId" , Subject.getUser().getInstId() ) ;
		modal.put("year", reporting.getId() ) ;
		modal.put("page", 0) ;
		RemotePage<SchoolClassesNumBean> scs = schoolClassesNumsService.findList( modal.getParam("schoolId","year"),
				modal.getPageParam().orderBy("name", "ASC").orderBy("classes", "ASC") , SchoolClassesNumBean.class ) ;
		if(null!=scs && null!=scs.getData()&&!scs.getData().isEmpty()) {
			sort(scs.getData());
		}
		return success(scs);
	}

	private  void sort(List<SchoolClassesNumBean> list) {
		//list.sort(SchoolClassesNumBean::compareByName);
	}


	@RequestMapping(value="/importSchoolUser",  produces = { "application/json;charset=UTF-8" })
	public String importSchoolUser(@RequestParam  MultipartFile file) {
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
		   	String[] constHader = {"学校标识码","学校名称","举办者类型","办学类型","所属区县教育局"};
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
				if(records.size()<6) {
					return error(1, "excel模板不正确");
				}
				String schoolCode = records.get(0);
				String schoolName = records.get(1);
				//String city = records.get(2);
				//String country = records.get(3);
				String jubantype = records.get(4);
				String banxueType = records.get(5);
				String eduName = records.get(6);


				if (StringUtils.isNotBlank(schoolCode) && StringUtils.isNotBlank(eduName)
						&& StringUtils.isNotBlank(jubantype) && StringUtils.isNotBlank(banxueType)
						) {
					if (getSchool("code", schoolCode)) {
						msgList.add(String.format("第%s行,学校标识码: %s已存在,未导入 <br>",i + "", schoolCode));
						errorcount ++ ;
						continue;
					}
					if (getSchool("name", schoolName)) {
						msgList.add( String.format("第%s行,学校名称: %s已存在,未导入 <br>", i + "", schoolName));
						errorcount ++  ;
						continue;
					}
					if (getEdu(eduName) == -1) {
						msgList.add(String.format("第%s行,所属区县教育局: %s不存在,未导入 <br>", i + "", schoolName));
						errorcount ++ ;
						continue;
					}
					if (getSchoolType(banxueType) == -1) {
						msgList.add(String.format("第%s行,办学类型: %s不存在,未导入 <br>", i + "", banxueType));
						errorcount ++ ;
						continue;
					}
					/*
					if (StringUtils.isBlank(getCountry(country))) {
						msgList.add( String.format("第%s行,所在地城乡分类" + ": %s不存在,未导入 <br>", i + "", country));
						errorcount ++ ;
						continue;
					}
*/
					Eduinst eduinst = getEduAll(eduName);
					SchoolParam schoolParam = new SchoolParam();
					schoolParam.setCode(schoolCode);
					schoolParam.setCreateId(security.getLoginUser().getId());
					schoolParam.setEduinstId(eduinst.getId());
					schoolParam.setName(schoolName);
					schoolParam.setCity(eduinst.getCityId() + "");
					schoolParam.setCounty(eduinst.getCountyId() + "");
					//schoolParam.setRegionType(getCountry(country));
					schoolParam.setRunBy(transContext.getCode(jubantype,"RUN_BY"));
					schoolParam.setType(getSchoolType(banxueType));
					schoolParam.setLoginName(schoolCode);
					schoolService.createSchool(schoolParam);
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



	private boolean  getSchool(String parmaName,Object value) {
		boolean res =  false ;
		Map<String, Object> params = new HashMap<>();
		params.put(parmaName, value);
		School school = schoolService.findOne(params, School.class);
		if(null!=school) {
			res = true ;
		}
		return  res;
	}

	private int getEdu(String name) {
		int res =  -1 ;
		Map<String, Object> params = new HashMap<>();
		params.put("name", name);
		Eduinst edu = eduinstService.findOne(params, Eduinst.class);
		if(null!=edu) {
			res = edu.getId() ;
		}
		return  res;
	}
	private Eduinst getEduAll(String name) {
		Map<String, Object> params = new HashMap<>();
		params.put("name", name);
		Eduinst edu = eduinstService.findOne(params, Eduinst.class);
		return  edu;
	}

	private int getSchoolType(String name) {
		int res =  -1 ;
		Map<String, Object> params = new HashMap<>();
		params.put("name", name);
		SchoolType stype = schoolTypeDao.findBean(params, SchoolType.class);
		if(null!=stype) {
			res = stype.getId() ;
		}
		return  res;
	}

	private String getCountry(String name) {
		String res =  "" ;
		Map<String, Object> params = new HashMap<>();
		params.put("type", "REGION_TYPE");
		params.put("name" , name) ;
		Dict dict = dictService.findOne(params, Dict.class);
		if(null!=dict) {
			res = dict.getCode() ;
		}
		return  res;
	}


}
