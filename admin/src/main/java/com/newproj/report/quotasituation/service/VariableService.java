package com.newproj.report.quotasituation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newproj.core.rest.support.AbstractBaseService;
import com.newproj.report.quotasituation.dal.dao.VariableDao;
@Service
public class VariableService extends AbstractBaseService{
	
	@Autowired
	private VariableDao variableDao ;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		setMapper( variableDao ) ;
	}

}
