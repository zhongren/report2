package com.newproj.report.bulletin.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.newproj.core.page.PageParam;
import com.newproj.core.page.PageUtil;
import com.newproj.core.page.RemotePage;
import com.newproj.core.rest.support.AbstractBaseService;
import com.newproj.core.util.NumberUtil;
import com.newproj.core.util.TimeUtil;
import com.newproj.report.bulletin.dal.dao.BulletinDao;
import com.newproj.report.bulletin.dto.Bulletin;
import com.newproj.report.bulletin.dto.BulletinParam;

@Service
public class BulletinService extends AbstractBaseService {

	@Autowired
	private BulletinDao bulletinDao ;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		setMapper(bulletinDao) ;
	}
	
	public RemotePage<Bulletin> findBulletin( Map<String,Object> param , PageParam pageParam ){
		Page<?> page = PageUtil.startPage(pageParam) ;
		List<Bulletin> dataList = bulletinDao.findBulletin(param, Bulletin.class ) ;
		return new RemotePage<Bulletin>( dataList, page ) ;
	}

}
