package com.newproj.report.quota.dto ;

import java.util.List;

import com.newproj.report.collection.dto.Collection;

public class Quota {
	
	private Integer id ; 		//ID 
	private String name ; 		//名称 
	private Integer level ; 	//层级 
	private Integer parentId ; 	//父级指标 
	private String type ; 		//类型
	private String typeName ;	
	private String content ; 	//监测内容 
	private String summery ; 	//监测要点 
	private String note ; 		//备注 
	private String formula ; 	//计算公式 
	private String rule ; 		//合格标准 
	private Integer rank ; 		//排序 
	
	private List<Quota> subQuota ;		//下级指标
	private Boolean finished; 	//是否完成标识
	private Boolean subaccChecked ;	//子账号分配标识
	private Boolean disabledCheck;  //其他子账号已分配，当前子账号不可选择
	
	private List<Collection> collections ; 
	public void setId ( Integer id ){ 
		this.id = id ; 
 	}
	public Integer getId ( ) { 
		return this.id ; 
	}
	public void setName ( String name ){ 
		this.name = name ; 
 	}
	public String getName ( ) { 
		return this.name ; 
	}
	public void setLevel ( Integer level ){ 
		this.level = level ; 
 	}
	public Integer getLevel ( ) { 
		return this.level ; 
	}
	public Boolean getDisabledCheck() {
		return disabledCheck;
	}
	public void setDisabledCheck(Boolean disabledCheck) {
		this.disabledCheck = disabledCheck;
	}
	public void setParentId ( Integer parentId ){ 
		this.parentId = parentId ; 
 	}
	public Integer getParentId ( ) { 
		return this.parentId ; 
	}
	public void setContent ( String content ){ 
		this.content = content ; 
 	}
	public Boolean getFinished() {
		return finished;
	}
	public void setFinished(Boolean finished) {
		this.finished = finished;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getContent ( ) { 
		return this.content ; 
	}
	public void setSummery ( String summery ){ 
		this.summery = summery ; 
 	}
	public String getSummery ( ) { 
		return this.summery ; 
	}
	public void setNote ( String note ){ 
		this.note = note ; 
 	}
	public String getNote ( ) { 
		return this.note ; 
	}
	public void setFormula ( String formula ){ 
		this.formula = formula ; 
 	}
	public String getFormula ( ) { 
		return this.formula ; 
	}
	public List<Collection> getCollections() {
		return collections;
	}
	public void setCollections(List<Collection> collections) {
		this.collections = collections;
	}
	public void setRule ( String rule ){ 
		this.rule = rule ; 
 	}
	public String getRule ( ) { 
		return this.rule ; 
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setRank ( Integer rank ){ 
		this.rank = rank ; 
 	}
	public Integer getRank ( ) { 
		return this.rank ; 
	}
	public List<Quota> getSubQuota() {
		return subQuota;
	}
	public void setSubQuota(List<Quota> subQuota) {
		this.subQuota = subQuota;
	}
	public Boolean getSubaccChecked() {
		return subaccChecked;
	}
	public void setSubaccChecked(Boolean subaccChecked) {
		this.subaccChecked = subaccChecked;
	}

}