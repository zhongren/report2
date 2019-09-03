package com.newproj.report.export;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;

import com.newproj.core.cache.Cache;
import com.newproj.core.rest.annotation.Api;
import com.newproj.core.rest.support.RestActionSupporter;
import com.newproj.core.util.VelocityUtil;
import com.newproj.report.export.bean.CityClassNumBean;
import com.newproj.report.export.bean.CityCountryBean;
import com.newproj.report.export.bean.ClassNumsBean;
import com.newproj.report.school.dal.dao.SchoolClassesNumsDao;
import com.newproj.report.sys.dto.City;
import com.newproj.report.sys.service.CityService;

@Api("/export")
public class ClassesExport extends RestActionSupporter {

	@Autowired
	private Cache cache;

	@Autowired
	private SchoolClassesNumsDao numDao;

	@Autowired
	private CityService cityService;

	@Value("${report.id}")
	public String reportId;

	private static final int MIN_PRI = 45;
	private static final int MIN_MIDDLE = 50;

	@RequestMapping("/school/classes")
	public String exportClasses(HttpServletRequest request, HttpServletResponse response) {
		int maxPriValue = getMaxValue(1);
		int maxMiddleValue = getMaxValue(2);
		List<CityCountryBean> citys = getCityCountrys();
		String priKey = "select_primaryNums_all";
		String middleKey = "select_middleNums_all";
		List<CityCountryBean> primaryNums = getNums(priKey, citys, MIN_PRI, maxPriValue, 0, 7);
		List<CityCountryBean> middleNums = getNums(middleKey, citys, MIN_MIDDLE, maxMiddleValue, 6, 10);
		List<String> primaryMids = getMids(primaryNums);
		List<String> middleMids = getMids(middleNums);
		Context context = new VelocityContext();
		context.put("primaryMids", primaryMids);
		context.put("middleMids", middleMids);
		context.put("primaryNums", formatOut(primaryNums));
		context.put("middleNums", formatOut(middleNums));
		try {
			VelocityUtil.download("班额统计表.xls", "/vm/classesnums.vm", context, request, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	

	private List<CityClassNumBean> formatOut(List<CityCountryBean> nums) {
		if (null == nums || nums.isEmpty()) {
			return new ArrayList<>();
		}
		List<CityClassNumBean> list = new ArrayList<>();
		for (CityCountryBean ccb : nums) {
			ccb.getCoutrys().get(0).setCityName(ccb.getCityName());
			list.addAll(ccb.getCoutrys());
		}
		return list;
	}

	private List<String> getMids(List<CityCountryBean> primaryNums) {
		List<String> mids = new ArrayList<>();
		if (null != primaryNums && !primaryNums.isEmpty()) {
			for (ClassNumsBean cb : primaryNums.get(0).getCoutrys().get(0).getNums()) {
				mids.add(cb.getMid() + "");
			}
		}
		return mids;
	}

	private List<CityCountryBean> getNums(String cacheKey, List<CityCountryBean> citys, int minValue, int maxValue,
			int min_name_num, int max_name_num) {
		List<CityCountryBean> primaryNums = getCBtoCache(cacheKey);
		if (primaryNums != null && !primaryNums.isEmpty()) {
			return primaryNums;
		}
		primaryNums = new ArrayList<>();
		for (CityCountryBean cityb : citys) {
			for (CityClassNumBean c : cityb.getCoutrys()) {
				List<ClassNumsBean> list = new ArrayList<>();
				for (int i = minValue; i <= maxValue; i++) {
					ClassNumsBean cnb = new ClassNumsBean();
					Map<String, Object> params = new HashMap<>();
					params.put("cid", c.getId());
					params.put("year", reportId);
					params.put("min_name_num", min_name_num);
					params.put("max_name_num", max_name_num);
					params.put("nums", i);
					cnb.setMid(i);
					cnb.setClassNums(numDao.getClassesNums(params));
					cnb.setSchoolNums(numDao.getSchoolNums(params));
					list.add(cnb);
				}
				c.setNums(list);
				Map<String, Object> cp = new HashMap<>();
				cp.put("cid", c.getId());
				cp.put("year", reportId);
				cp.put("min_name_num", min_name_num);
				cp.put("max_name_num", max_name_num);
				c.setClassesNums(numDao.getAllClassesNums(cp));
				c.setSchoolNums(numDao.getAllSchoolNums(cp));
			}
			primaryNums.add(cityb);
			System.out.println("city  : " + cityb.getCityName());
		}
		cache.put(cacheKey, primaryNums, -1l);
		return primaryNums;
	}

	private List<CityCountryBean> getCityCountrys() {
		String key = "select_city_countrys";
		List<City> citys = getAllCity();
		List<CityCountryBean> citb = getCBtoCache(key);
		if (null != citb && !citb.isEmpty()) {
			return citb;
		}
		citb = new ArrayList<>();
		for (City c : citys) {
			CityCountryBean ctb = new CityCountryBean();
			ctb.setCityId(c.getId());
			ctb.setCityName(c.getName());
			ctb.setCoutrys(getCountyByCid(c.getId().intValue()));
			citb.add(ctb);
		}
		cache.put(key, citb, -1l);
		return citb;
	}

	private List<ClassNumsBean> getCnb(List<LinkedHashMap<Object, Object>> listMap) {
		List<ClassNumsBean> list = new ArrayList<>();
		if (null != listMap && !listMap.isEmpty()) {
			for (LinkedHashMap<Object, Object> mobj : listMap) {
				ClassNumsBean ccb = new ClassNumsBean();
				ccb.setMid((Integer) mobj.get("mid"));
				ccb.setClassNums((Integer) mobj.get("classNums"));
				ccb.setSchoolNums((Integer) mobj.get("schoolNums"));
				list.add(ccb);
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	private List<CityCountryBean> getCBtoCache(String key) {
		List<?> jsonarry = cache.get(key, List.class);
		List<CityCountryBean> citb = new ArrayList<>();
		if (null != jsonarry && !jsonarry.isEmpty()) {
			for (Object obj : jsonarry) {
				CityCountryBean ctb = new CityCountryBean();
				LinkedHashMap<Object, Object> map = (LinkedHashMap<Object, Object>) obj;
				ctb.setCityId((Integer) map.get("cityId"));
				ctb.setCityName((String) map.get("cityName"));
				List<LinkedHashMap<Object, Object>> listMap = (List<LinkedHashMap<Object, Object>>) map.get("coutrys");
				List<CityClassNumBean> countrys = new ArrayList<>();
				for (LinkedHashMap<Object, Object> mobj : listMap) {
					CityClassNumBean ccb = new CityClassNumBean();
					ccb.setId((Integer) mobj.get("id"));
					ccb.setName((String) mobj.get("name"));
					ccb.setParentId((Integer) mobj.get("parentId"));
					ccb.setClassesNums((Integer) mobj.get("classesNums"));
					ccb.setSchoolNums((Integer) mobj.get("schoolNums"));
					ccb.setNums(getCnb((List<LinkedHashMap<Object, Object>>) mobj.get("nums")));
					countrys.add(ccb);
				}
				ctb.setCoutrys(countrys);
				citb.add(ctb);
			}
			return citb;
		} else {
			return null;
		}

	}

	private List<City> getAllCity() {
		return cityService.findListBy("parent_id", 0, City.class);
	}

	private List<CityClassNumBean> getCountyByCid(int cid) {
		return cityService.findListBy("parent_id", cid, CityClassNumBean.class);
	}

	private int getMaxValue(int type) {
		int min_name_num = (type == 1 ? 0 : 6);
		int max_name_num = (type == 1 ? 7 : 10);
		Map<String, Object> param = new HashMap<>();
		param.put("min_name_num", min_name_num);
		param.put("max_name_num", max_name_num);
		int maxnums = numDao.getNumsMax(param);
//		if (maxnums > 200) {
//			maxnums = 200;
//		}
		return maxnums;

	}

}
