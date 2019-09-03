package com.newproj.report.export.bean;

import java.util.List;

import com.newproj.report.collection.dto.Collection;
import com.newproj.report.school.dto.School;
import com.newproj.report.school.dto.SchoolCollection;

public class SchoolCollectionEx {
	 private School school;
	 private List<SchoolCollection> scs;
	public School getSchool() {
		return school;
	}
	public void setSchool(School school) {
		this.school = school;
	}
	
	public SchoolCollectionEx(School school, List<SchoolCollection> scs) {
		super();
		this.school = school;
		this.scs = scs;
	}
	public List<SchoolCollection> getScs() {
		return scs;
	}
	public void setScs(List<SchoolCollection> scs) {
		this.scs = scs;
	}
	public SchoolCollectionEx() {
	}
	
	 
}
