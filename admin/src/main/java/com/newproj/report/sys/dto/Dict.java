package com.newproj.report.sys.dto ;
public class Dict {
	
	private Integer id ; 	//ID 
	private String code ; 	//标识码 
	private String name ; 	//名称 
	private String type ; 	//字典类型 
	private Integer level ; 	//层级 
	private Integer parentId ; 	//上级 
	private Integer rank ; 	//排序 
	private String note ; 	//备注 

	
	public void setId ( Integer id ){ 
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
	public void setName ( String name ){ 
		this.name = name ; 
 	}
	public String getName ( ) { 
		return this.name ; 
	}
	public void setType ( String type ){ 
		this.type = type ; 
 	}
	public String getType ( ) { 
		return this.type ; 
	}
	public void setLevel ( Integer level ){ 
		this.level = level ; 
 	}
	public Integer getLevel ( ) { 
		return this.level ; 
	}
	public void setParentId ( Integer parentId ){ 
		this.parentId = parentId ; 
 	}
	public Integer getParentId ( ) { 
		return this.parentId ; 
	}
	public void setRank ( Integer rank ){ 
		this.rank = rank ; 
 	}
	public Integer getRank ( ) { 
		return this.rank ; 
	}
	public void setNote ( String note ){ 
		this.note = note ; 
 	}
	public String getNote ( ) { 
		return this.note ; 
	}

}