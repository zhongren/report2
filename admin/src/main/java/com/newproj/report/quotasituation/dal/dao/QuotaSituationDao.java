package com.newproj.report.quotasituation.dal.dao;

import org.springframework.stereotype.Repository;

import com.newproj.core.mybatis.AbstractBaseMapper;
import com.newproj.core.mybatis.Condition;
import com.newproj.core.mybatis.Condition.SYMBOL;
import com.newproj.core.mybatis.Table;

@Repository
@Table("tb_quota_situation")
public class QuotaSituationDao extends AbstractBaseMapper{

	@Override
	public void init() {
		// TODO Auto-generated method stub
		setUnityFlterCondition(new Condition[]{new Condition("is_deleted" , 0 , SYMBOL.EQ)});
	}

}
