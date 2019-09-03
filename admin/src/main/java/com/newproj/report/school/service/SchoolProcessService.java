package com.newproj.report.school.service;

import org.springframework.stereotype.Service;

import com.newproj.core.rest.support.AbstractBaseService;
import com.newproj.report.school.dal.dao.SchoolProcessDao;

@Service
public class SchoolProcessService extends AbstractBaseService{

	private SchoolProcessDao processDao ;

	@Override
	public void init() {
		// TODO Auto-generated method stub
		setMapper( processDao ) ;
	}
	
	
}
