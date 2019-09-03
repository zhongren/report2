package com.newproj.report.sys.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newproj.core.beans.MapParam;
import com.newproj.core.rest.support.AbstractBaseService;
import com.newproj.report.sys.constant.RoleConstant.RoleType;
import com.newproj.report.sys.dal.dao.SysPermissionDao;
import com.newproj.report.sys.dal.dao.SysRoleDao;
import com.newproj.report.sys.dto.SysPermission;
import com.newproj.report.sys.dto.SysRole;
@Service
public class SysRoleService extends AbstractBaseService{

	@Autowired
	private SysRoleDao roleDao ;
	
	@Autowired
	private SysPermissionDao permDao ;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		setMapper( roleDao ) ;
	}
	
	public List<SysRole> findUsableRole( Map<String,Object> params ){
		return roleDao.findBeanList( new MapParam<String,Object>()
				.push("status" , 0 ).push( params ), SysRole.class ) ;
	}
	
	public SysRole getSubaccRole( Integer roleId ){
		if( roleId == null )
			return null ;
		List<SysRole> roles = findUsableRole( new MapParam<String,Object>() .push("parentId" , roleId  ) ) ;
		return roles == null || roles.isEmpty() ? null : roles.get( 0 ) ;
	}

	
	/**
	 * 查询角色权限列表
	 * 
	 * @param roleId
	 * @return
	 */
	public List<SysPermission> findRolePermission( int roleId ){
		return permDao.findRolePermission(roleId, SysPermission.class ) ;
	}
}
