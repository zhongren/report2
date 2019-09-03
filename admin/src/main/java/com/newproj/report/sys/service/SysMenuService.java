package com.newproj.report.sys.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newproj.core.rest.support.AbstractBaseService;
import com.newproj.report.sys.dal.dao.SysMenuDao;
import com.newproj.report.sys.dto.SysMenu;

@Service
public class SysMenuService extends AbstractBaseService{
	
	@Autowired
	private SysMenuDao menuDao ;
	
	public void init() {
		// TODO Auto-generated method stub
		setMapper( menuDao ) ;
	}
	
	public List<SysMenu> findRoleMenus( Integer roleId ){
		return menuDao.findRoleMenus(roleId, SysMenu.class ) ;
	}
	
	public List<SysMenu> constructMenu( List<SysMenu> menus , int parentId ){
		if( menus == null || menus.isEmpty() ){
			return null ;
		}
		
		List<SysMenu> temp = new ArrayList<SysMenu>() ;
		temp.addAll( menus ) ;
		for( SysMenu menu : temp ){
			findParentMenu( menus , menu.getParentId() ) ;
		}
		
		List< SysMenu > subMenus = new ArrayList<SysMenu>() ;
		for( SysMenu menu : menus ){
			if( parentId ==  menu.getParentId() ){
				List< SysMenu > tempMenus = constructMenu( menus , menu.getId() ) ;
				if( tempMenus != null && !tempMenus.isEmpty() ){ 
					menu.setSubMenus( tempMenus );
				}
				subMenus.add( menu ) ;
			}
		}
		return subMenus ;
	}
	
	private void findParentMenu( List<SysMenu> menus , Integer parentId  ){
		if( menus == null || menus.isEmpty() ){
			return  ;
		}
		if( parentId != null && parentId != 0 ){
			for( SysMenu menu : menus ){
				if( menu.getId().equals( parentId ) ){
					return ;
				}
			}
			SysMenu parentMenu = findBy( "id" ,  parentId , SysMenu.class ) ;
			if( parentMenu != null ) {
				menus.add( parentMenu ) ;
				findParentMenu( menus , parentMenu.getParentId() ) ;
			}
			return ;
		}
	}
	
}
