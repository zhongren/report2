package com.newproj.report.collection.dto ;
public class Collection {

	private Integer id ; 	//ID
	private String code ; 	//标识码
	private String title ; 	//标题
	private Integer quotaId ; 	//指标ID
	private String quotaName; 	//指标名称
	private String memo ; 	//描述
	private String valueType ; 	//采集数据类型
	private String valueOpt ; 	//值选项
	private String validation ; 	//校验规则
	private String note ; 	//备注
	private Integer rank ; 	//排序
	private String createTime ; 	//添加日期
	private Integer createId ; 	//创建人
	private String createName ;
	private String updateTime ; 	//更新日期
	private Integer updateId ; 	//更新人
	private String updateName ;
	private Integer type ;
	private String content; 	//采集内容

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setId (Integer id ){
		this.id = id ;
 	}
	public Integer getId ( ) {
		return this.id ;
	}
	public void setCode ( String code ){
		this.code = code ;
 	}
	public String getCode ( ) {
		return this.code ;
	}
	public void setTitle ( String title ){
		this.title = title ;
 	}
	public String getTitle ( ) {
		return this.title ;
	}
	public void setQuotaId ( Integer quotaId ){
		this.quotaId = quotaId ;
 	}
	public Integer getQuotaId ( ) {
		return this.quotaId ;
	}
	public void setMemo ( String memo ){
		this.memo = memo ;
 	}
	public String getMemo ( ) {
		return this.memo ;
	}
	public String getQuotaName() {
		return quotaName;
	}
	public void setQuotaName(String quotaName) {
		this.quotaName = quotaName;
	}
	public void setValueType ( String valueType ){
		this.valueType = valueType ;
 	}
	public String getValueType ( ) {
		return this.valueType ;
	}
	public void setValueOpt ( String valueOpt ){
		this.valueOpt = valueOpt ;
 	}
	public String getValueOpt ( ) {
		return this.valueOpt ;
	}
	public void setValidation ( String validation ){
		this.validation = validation ;
 	}
	public String getValidation ( ) {
		return this.validation ;
	}
	public void setNote ( String note ){
		this.note = note ;
 	}
	public String getNote ( ) {
		return this.note ;
	}
	public void setRank ( Integer rank ){
		this.rank = rank ;
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
	public Integer getRank ( ) {
		return this.rank ;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

}
