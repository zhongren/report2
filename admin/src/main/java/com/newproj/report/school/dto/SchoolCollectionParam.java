package com.newproj.report.school.dto;

public class SchoolCollectionParam {
	
	private Integer id ; 	//ID 
	private Integer schoolId ; 	//学校ID 
	private Integer quotaId ;	//指标ID
	private Integer collectionId ; 	//采集项 
	private Integer reportId ; 	//填报记录ID 
	private String content ; 	//填报内容 
	private String createTime ; 	//填报日期 
	private Integer createId ; 	//填报人 

	
	public void setId ( Integer id ){ 
		this.id = id ; 
 	}
	public Integer getId ( ) { 
		return this.id ; 
	}
	public void setSchoolId ( Integer schoolId ){ 
		this.schoolId = schoolId ; 
 	}
	public Integer getSchoolId ( ) { 
		return this.schoolId ; 
	}
	public void setCollectionId ( Integer collectionId ){ 
		this.collectionId = collectionId ; 
 	}
	public Integer getCollectionId ( ) { 
		return this.collectionId ; 
	}
	public Integer getQuotaId() {
		return quotaId;
	}
	public void setQuotaId(Integer quotaId) {
		this.quotaId = quotaId;
	}
	public void setReportId ( Integer reportId ){ 
		this.reportId = reportId ; 
 	}
	public Integer getReportId ( ) { 
		return this.reportId ; 
	}
	public void setContent ( String content ){ 
		this.content = content ; 
 	}
	public String getContent ( ) { 
		return this.content ; 
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

}