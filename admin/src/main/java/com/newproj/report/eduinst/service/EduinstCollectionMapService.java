package com.newproj.report.eduinst.service;

import com.github.pagehelper.Page;
import com.newproj.core.beans.MapParam;
import com.newproj.core.exception.BusinessException;
import com.newproj.core.page.PageParam;
import com.newproj.core.page.PageUtil;
import com.newproj.core.page.RemotePage;
import com.newproj.core.rest.support.AbstractBaseService;
import com.newproj.core.util.NumberUtil;
import com.newproj.core.util.StringUtil;
import com.newproj.core.util.TimeUtil;
import com.newproj.report.bulletin.constant.BulletinConstant;
import com.newproj.report.bulletin.dal.dao.BulletinDao;
import com.newproj.report.bulletin.dto.BulletinParam;
import com.newproj.report.eduinst.dal.dao.EduinstAuditingDao;
import com.newproj.report.eduinst.dal.dao.EduinstCollectionDao;
import com.newproj.report.eduinst.dal.dao.EduinstCollectionMapDao;
import com.newproj.report.eduinst.dal.dao.EduinstProcessDao;
import com.newproj.report.eduinst.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class EduinstCollectionMapService extends AbstractBaseService{

	@Autowired
	private EduinstCollectionMapDao eduinstCollectionMapDao ;



	@Override
	public void init() {
		// TODO Auto-generated method stub
		setMapper( eduinstCollectionMapDao ) ;
	}


}
