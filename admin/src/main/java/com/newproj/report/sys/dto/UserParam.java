package com.newproj.report.sys.dto ;
public class UserParam {
	
	private Integer id ; 	//ID 
	private String username ; 	//账号 
	private String passwd ; 	//密码 
	private String realName ; 	//用户姓名 
	private String email ; 	//邮箱 
	private String roleCode ; 	//角色 
	private String instType ; 	//机构类型 
	private Integer instId ; 	//机构ID 
	private String accessToken ; 	//登陆token 
	private String tokenTime ; 	//上次登陆日期 
	private String createTime ; 	//添加日期 
	private Integer createId ; 	//添加人 
	private String updateTime ; 	//更新日期 
	private Integer status ; 	//状态(0:正常,1:禁用) 

	
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
	public void setPasswd ( String passwd ){ 
		this.passwd = passwd ; 
 	}
	public String getPasswd ( ) { 
		return this.passwd ; 
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
	public void setInstType ( String instType ){ 
		this.instType = instType ; 
 	}
	public String getInstType ( ) { 
		return this.instType ; 
	}
	public void setInstId ( Integer instId ){ 
		this.instId = instId ; 
 	}
	public Integer getInstId ( ) { 
		return this.instId ; 
	}
	public void setAccessToken ( String accessToken ){ 
		this.accessToken = accessToken ; 
 	}
	public String getAccessToken ( ) { 
		return this.accessToken ; 
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
	public String getRoleCode() {
		return roleCode;
	}
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

}