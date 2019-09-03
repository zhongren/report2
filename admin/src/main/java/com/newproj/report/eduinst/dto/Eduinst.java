package com.newproj.report.eduinst.dto ;
public class Eduinst {
	
	private Integer id ; 	//id 
	private String name ; 	//名称 
	private String code ; 	//标识 
	private String type ; 	//教育局类型 
	private String typeName ;
	private Integer cityId ;	//市ID
	private String cityName; 
	private Integer countyId ;	//县ID
	private String countyName ;
	private String createTime ; 	//添加日期 
	private Integer createId ; 	//添加人 
	private String createName ;
	private String updateTime ; 	//更新日期 
	private Integer updateId ; 	//更新人 
	private String updateName ;
	private Integer parentId ;
	private String parentName ;

	private String loginName ;
	private Integer accountStatus ;
	private String accountStatusName ;
	
	public void setId ( Integer id ){ 
		this.id = id ; 
 	}
	public Integer getId ( ) { 
		return this.id ; 
	}
	public String getAccountStatusName() {
		return accountStatusName;
	}
	public void setAccountStatusName(String accountStatusName) {
		this.accountStatusName = accountStatusName;
	}
	public void setName ( String name ){ 
		this.name = name ; 
 	}
	public String getName ( ) { 
		return this.name ; 
	}
	public void setCode ( String code ){ 
		this.code = code ; 
 	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public String getCode ( ) { 
		return this.code ; 
	}
	public String getTypeName() {
		return typeName;
	}
	public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
	}
	public String getUpdateName() {
		return updateName;
	}
	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public void setType ( String type ){ 
		this.type = type ; 
 	}
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	public Integer getCountyId() {
		return countyId;
	}
	public void setCountyId(Integer countyId) {
		this.countyId = countyId;
	}
	public String getType ( ) { 
		return this.type ; 
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
	public void setUpdateId ( Integer updateId ){ 
		this.updateId = updateId ; 
 	}
	public Integer getUpdateId ( ) { 
		return this.updateId ; 
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getCountyName() {
		return countyName;
	}
	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public Integer getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(Integer accountStatus) {
		this.accountStatus = accountStatus;
	}

}