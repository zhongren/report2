package com.newproj.report.sys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newproj.core.rest.support.AbstractBaseService;
import com.newproj.report.sys.dto.DictDao;
@Service
public class DictService extends AbstractBaseService{

	@Autowired
	private DictDao dictDao ;

	@Override
	public void init() {
		// TODO Auto-generated method stub
		setMapper( dictDao ) ;
	}
	
}
