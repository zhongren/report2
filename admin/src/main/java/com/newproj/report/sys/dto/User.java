package com.newproj.report.sys.dto ;
public class User {
	
	private Integer id ; 	//ID 
	private String username ; 	//账号 
	private String realName ; 	//用户姓名 
	private String email ; 	//邮箱 
	private String roleCode ; 	//角色 
	private Integer instId ; 	//机构ID 
	private String instType ;	//机构类型
	private String tokenTime ; 	//上次登陆日期 
	private String createTime ; 	//添加日期 
	private Integer createId ; 	//添加人 
	private String createName ;	//添加人姓名
	private String updateTime ; 	//更新日期 
	private Integer status ; 	//状态(0:正常,1:禁用) 
	private String statusName ;	
	
	public void setId ( Integer id ){ 
		this.id = id ; 
 	}
	public Integer getId ( ) { 
		return this.id ; 
	}
	public void setUsername ( String username ){ 
		this.username = username ; 
 	}
	public String getUsername ( ) { 
		return this.username ; 
	}
	public void setRealName ( String realName ){ 
		this.realName = realName ; 
 	}
	public String getRealName ( ) { 
		return this.realName ; 
	}
	public void setEmail ( String email ){ 
		this.email = email ; 
 	}
	public String getEmail ( ) { 
		return this.email ; 
	}
	public String getInstType() {
		return instType;
	}
	public void setInstType(String instType) {
		this.instType = instType;
	}
	public void setInstId ( Integer instId ){ 
		this.instId = instId ; 
 	}
	public Integer getInstId ( ) { 
		return this.instId ; 
	}
	public void setTokenTime ( String tokenTime ){ 
		this.tokenTime = tokenTime ; 
 	}
	public String getTokenTime ( ) { 
		return this.tokenTime ; 
	}
	public void setCreateTime ( String createTime ){ 
		this.createTime = createTime ; 
 	}
	public String getCreateTime ( ) { 
		return this.createTime ; 
	}
	public void setCreateId ( Integer createId ){ 
		this.createId = createId ; 
 	}
	public Integer getCreateId ( ) { 
		return this.createId ; 
	}
	public void setUpdateTime ( String updateTime ){ 
		this.updateTime = updateTime ; 
 	}
	public String getUpdateTime ( ) { 
		return this.updateTime ; 
	}
	public void setStatus ( Integer status ){ 
		this.status = status ; 
 	}
	public Integer getStatus ( ) { 
		return this.status ; 
	}
	public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getRoleCode() {
		return roleCode;
	}
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	
}