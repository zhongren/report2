package com.newproj.report.export.bean;

import java.util.List;

public class EduCollectionBean {
	private String code ;
	private String name ;
	
	private List<String>  collections;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getCollections() {
		return collections;
	}

	public void setCollections(List<String> collections) {
		this.collections = collections;
	}
	
	
	
}
