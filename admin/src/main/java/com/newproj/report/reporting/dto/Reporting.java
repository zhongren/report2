package com.newproj.report.reporting.dto ;
public class Reporting {
	
	private Integer id ; 	//id 
	private String title ; 	//显示标题 
	private String startTime ; 	//起始日期 
	private String expireTime ; 	//结束日期 
	private String memo ; 	//描述 
	private String createTime ; 	//添加日期 
	private Integer createId ; 	//添加人 
	private String updateTime ;
	private Integer updateId ;
	
	private String createName ;
	private String updateName ;
	
	private String statusName ;

	
	public void setId ( Integer id ){ 
		this.id = id ; 
 	}
	public Integer getId ( ) { 
		return this.id ; 
	}
	public void setTitle ( String title ){ 
		this.title = title ; 
 	}
	public String getTitle ( ) { 
		return this.title ; 
	}
	public void setStartTime ( String startTime ){ 
		this.startTime = startTime ; 
 	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getStartTime ( ) { 
		return this.startTime ; 
	}
	public void setExpireTime ( String expireTime ){ 
		this.expireTime = expireTime ; 
 	}
	public String getExpireTime ( ) { 
		return this.expireTime ; 
	}
	public void setMemo ( String memo ){ 
		this.memo = memo ; 
 	}
	public String getMemo ( ) { 
		return this.memo ; 
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
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getUpdateId() {
		return updateId;
	}
	public void setUpdateId(Integer updateId) {
		this.updateId = updateId;
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

}