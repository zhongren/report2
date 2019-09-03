package com.newproj.report.sys.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newproj.core.exception.BusinessException;
import com.newproj.core.rest.support.AbstractBaseService;
import com.newproj.core.util.NumberUtil;
import com.newproj.core.util.TimeUtil;
import com.newproj.report.sys.dal.dao.CityDao;
import com.newproj.report.sys.dto.City;
import com.newproj.report.sys.dto.CityParam;

@Service
public class CityService extends AbstractBaseService{

	@Autowired
	private CityDao cityDao ;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		setMapper( cityDao ) ;
	}
	
	public int createCity( CityParam param ){
		param.setCreateTime( TimeUtil.now() );
		return NumberUtil.parseInt(cityDao.createBean( param, CityParam.class ), 0 ) ;
	}
	
	public void updateCity( int id , CityParam param ){
		if( cityDao.findMapBy("id", id , "id") == null )
			throw new BusinessException("数据不存在,更新失败!") ;
		param.setUpdateTime( TimeUtil.now() );
		cityDao.updateBean("id", id , param , "name" , "note" , "updateId","updateTime") ;
	}
	
	public void deleteCity(int id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id",id);
		cityDao.delete(map);
	}
	
	public List<City> findCityByParent( int parentId , boolean withSbuCity ){
		List<City> cityList = cityDao.findBeanListBy( "parentId" , parentId , City.class ) ;
		if( cityList == null || cityList.isEmpty() )
			return null ;
		if( !withSbuCity ) return cityList ;
		for( City city : cityList ){
			city.setSubCitys( findCityByParent( city.getId() , true ) );
		}
		return cityList ;
	}
	
}
