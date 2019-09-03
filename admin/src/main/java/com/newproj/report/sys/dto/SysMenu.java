package com.newproj.report.sys.dto;

import java.util.List;

public class SysMenu {
	private Integer id ;		//菜单ID
	private String name ;		//菜单名称
	private Integer parentId ;	//父菜单ID
	private String url ;		//菜单URL
	private Integer rank ;		//菜单排序
	private String icon ;		//图标
	private Integer status ;	//菜单状态（0：可用，1：不可用）
	
	private List<SysMenu> subMenus ;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public List<SysMenu> getSubMenus() {
		return subMenus;
	}
	public void setSubMenus(List<SysMenu> subMenus) {
		this.subMenus = subMenus;
	}
	
}
