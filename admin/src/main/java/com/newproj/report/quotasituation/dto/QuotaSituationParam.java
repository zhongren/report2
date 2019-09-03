package com.newproj.report.quotasituation.dto ;
public class QuotaSituationParam {
	
	private Integer id ; 	//ID 
	private String name ; 	//监测点名称 
	private Integer dictId ;
	private String instType ; 	//机构类型 
	private String schoolType ;
	private String dataType ; 	//数据类型 
	private String standard ; 	//目标值 
	private String standardNote ; 	//目标值描述 
	private String calculatedFormula ; 	//计算值公式 
	private String calculatedNote ; 	//计算值描述 
	private String eligibleFormula ; 	//是否合格公式 
	private String eligibleNote ; 	
	private String createTime ; 	//添加日期 
	private Integer createId ; 	//添加人 
	private String updateTime ; 	//更新日期 
	private Integer updateId ; 	//更新人 
	private Integer isDeleted ; 	//删除标识 

	
	public void setId ( Integer id ){ 
		this.id = id ; 
 	}
	public Integer getId ( ) { 
		return this.id ; 
	}
	public String getSchoolType() {
		return schoolType;
	}
	public void setSchoolType(String schoolType) {
		this.schoolType = schoolType;
	}
	public void setName ( String name ){ 
		this.name = name ; 
 	}
	public String getEligibleNote() {
		return eligibleNote;
	}
	public void setEligibleNote(String eligibleNote) {
		this.eligibleNote = eligibleNote;
	}
	public String getName ( ) { 
		return this.name ; 
	}
	public void setInstType ( String instType ){ 
		this.instType = instType ; 
 	}
	public Integer getDictId() {
		return dictId;
	}
	public void setDictId(Integer dictId) {
		this.dictId = dictId;
	}
	public String getInstType ( ) { 
		return this.instType ; 
	}
	public void setDataType ( String dataType ){ 
		this.dataType = dataType ; 
 	}
	public String getDataType ( ) { 
		return this.dataType ; 
	}
	public void setStandard ( String standard ){ 
		this.standard = standard ; 
 	}
	public String getStandard ( ) { 
		return this.standard ; 
	}
	public void setStandardNote ( String standardNote ){ 
		this.standardNote = standardNote ; 
 	}
	public String getStandardNote ( ) { 
		return this.standardNote ; 
	}
	public void setCalculatedFormula ( String calculatedFormula ){ 
		this.calculatedFormula = calculatedFormula ; 
 	}
	public String getCalculatedFormula ( ) { 
		return this.calculatedFormula ; 
	}
	public void setCalculatedNote ( String calculatedNote ){ 
		this.calculatedNote = calculatedNote ; 
 	}
	public String getCalculatedNote ( ) { 
		return this.calculatedNote ; 
	}
	public void setEligibleFormula ( String eligibleFormula ){ 
		this.eligibleFormula = eligibleFormula ; 
 	}
	public String getEligibleFormula ( ) { 
		return this.eligibleFormula ; 
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
	public void setIsDeleted ( Integer isDeleted ){ 
		this.isDeleted = isDeleted ; 
 	}
	public Integer getIsDeleted ( ) { 
		return this.isDeleted ; 
	}

}