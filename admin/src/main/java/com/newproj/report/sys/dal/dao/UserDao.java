package com.newproj.report.sys.dal.dao;

import org.springframework.stereotype.Repository;

import com.newproj.core.mybatis.AbstractBaseMapper;
import com.newproj.core.mybatis.Condition;
import com.newproj.core.mybatis.Condition.SYMBOL;
import com.newproj.core.mybatis.Table;

@Repository
@Table("tb_user")
public class UserDao extends AbstractBaseMapper{

	@Override
	public void init() {
		setUnityFlterCondition(new Condition[]{new Condition("is_deleted" , 0 , SYMBOL.EQ )});
	}

}
