package com.newproj.report.school.dto ;


public class School {
	
	private Integer id ; 	//ID 
	private String code ; 	//标识码 
	private String name ; 	//学校名称 
	private String city ; 	//所属市 
	private String county ; 	//所属区县 
	private String regionType ; 	//城乡分类 
	private String runBy ; 	//办学者类型 
	private Integer type ; 	//办学类型 
	private String typeName ;//类型ID
	private String createTime ; 	//添加日期 
	private Integer createId ; 	//创建人 
	private String createName ;
	private String updateTime ; 	//更新日期 
	private Integer updateId ;		//更新人
	private String updateName ;
	private Integer eduinstId ;		//所属教育局
	private String eduinstName; 	
	private String masterName ;
	private String masterPhone ;
	
	private Integer reportStatus ; //填报状态
	
	//登陆账号信息
	private String loginName 	; //登陆账号
	private Integer accountId 	; //账号ID
	private Integer accountStatus ;//账号状态
	private String accountStatusName ;//状态描述
	
	public Integer getUpdateId() {
		return updateId;
	}
	public void setUpdateId(Integer updateId) {
		this.updateId = updateId;
	}
	public void setId ( Integer id ){ 
		this.id = id ; 
 	}
	public Integer getId ( ) { 
		return this.id ; 
	}
	public void setCode ( String code ){ 
		this.code = code ; 
 	}
	public Integer getType() {
		return type;
	}
	public String getMasterName() {
		return masterName;
	}
	public void setMasterName(String masterName) {
		this.masterName = masterName;
	}
	public String getMasterPhone() {
		return masterPhone;
	}
	public void setMasterPhone(String masterPhone) {
		this.masterPhone = masterPhone;
	}
	public String getCode ( ) { 
		return this.code ; 
	}
	public void setName ( String name ){ 
		this.name = name ; 
 	}
	public String getName ( ) { 
		return this.name ; 
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public void setCity ( String city ){ 
		this.city = city ; 
 	}
	public String getCity ( ) { 
		return this.city ; 
	}
	public void setCounty ( String county ){ 
		this.county = county ; 
 	}
	public String getCounty ( ) { 
		return this.county ; 
	}
	public void setRegionType ( String regionType ){ 
		this.regionType = regionType ; 
 	}
	public String getRegionType ( ) { 
		return this.regionType ; 
	}
	public void setRunBy ( String runBy ){ 
		this.runBy = runBy ; 
 	}
	public String getRunBy ( ) { 
		return this.runBy ; 
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
	public Integer getEduinstId() {
		return eduinstId;
	}
	public void setEduinstId(Integer eduinstId) {
		this.eduinstId = eduinstId;
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
	public Integer getReportStatus() {
		return reportStatus;
	}
	public void setReportStatus(Integer reportStatus) {
		this.reportStatus = reportStatus;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public Integer getAccountId() {
		return accountId;
	}
	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}
	public Integer getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(Integer accountStatus) {
		this.accountStatus = accountStatus;
	}
	public String getAccountStatusName() {
		return accountStatusName;
	}
	public void setAccountStatusName(String accountStatusName) {
		this.accountStatusName = accountStatusName;
	}
	public String getEduinstName() {
		return eduinstName;
	}
	public void setEduinstName(String eduinstName) {
		this.eduinstName = eduinstName;
	}
}