package com.newproj.report.sys.action;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.newproj.core.rest.annotation.Api;
import com.newproj.core.rest.annotation.Get;
import com.newproj.core.rest.support.RestActionSupporter;
import com.newproj.report.context.Subject;
import com.newproj.report.sys.dto.SysMenu;
import com.newproj.report.sys.dto.SysRole;
import com.newproj.report.sys.service.SysMenuService;
import com.newproj.report.sys.service.SysRoleService;

@Api("/menu")
public class SysMenuAction extends RestActionSupporter{

	@Autowired
	private SysMenuService menuService ;
	
	@Autowired
	private SysRoleService roleService ;
	
	/**
	 * 获取结构化系统菜单 .
	 * 
	 * @return
	 */
	@Get("")
	public String findUserMenu(){
		SysRole role = roleService.findBy("code", Subject.getUser().getRoleCode() , SysRole.class , "id")  ; 
		if( role == null )
			return success( null ) ;
		List<SysMenu> menus = menuService.findRoleMenus( role.getId() ) ;
		if( menus != null && !menus.isEmpty() ){
			menus = menuService.constructMenu( menus , 0 ) ;
		}
		menus = menuRank(menus) ;
		return success( menus );
	}
	
	private List<SysMenu> menuRank( List<SysMenu> menus ){
		if( menus == null || menus.isEmpty() ){
			return null ;
		}
		SysMenu [] array = menus.toArray( new SysMenu [] {} ) ;
		for( int i = 0 ; i < array.length - 1 ; i ++ ){
			if( array[i].getSubMenus() != null && !array[i].getSubMenus().isEmpty() ){
				menuRank( array[i].getSubMenus() ) ;
			}
			for( int j = 0 ; j < array.length - 1 ; j ++ ){
				if( array[j].getRank() < array[j+1].getRank() ) continue ;
				if( array[j].getRank() .equals( array[j+1].getRank() ) && array[j].getId() < array[j+1].getId() ) continue ;
				SysMenu tmp = array[j] ;
				array[j] = array[j+1] ;
				array[j+1] = tmp ;
			}
		}
		menus = Arrays.asList( array ) ;
		return menus ;
	}
	
}
