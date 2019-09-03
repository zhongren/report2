package com.newproj.report.export.bean;

import java.util.HashMap;
import java.util.Map;

public class SchoolClassesNumBean {
	private int id ;
	private String name ;
	private int classes ;
	private int nums;
	private int  schoolId;
	private int year ;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getClasses() {
		return classes;
	}
	public void setClasses(int classes) {
		this.classes = classes;
	}
	public int getNums() {
		return nums;
	}
	public void setNums(int nums) {
		this.nums = nums;
	}
	public int getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(int schoolId) {
		this.schoolId = schoolId;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	
	public static int compareByName(SchoolClassesNumBean h1, SchoolClassesNumBean h2) {
		if (h1.getName().equals(h2.getName())) {
			return Integer.compare(h1.getClasses(), h2.getClasses());
		}
		Map<String, Integer> fileds = new HashMap<>();
		fileds.put("一", 1);
		fileds.put("二", 2);
		fileds.put("三", 3);
		fileds.put("四", 4);
		fileds.put("五", 5);
		fileds.put("六", 6);
		fileds.put("七", 7);
		fileds.put("八", 8);
		fileds.put("九", 9);
		fileds.put("十", 10);
		fileds.put("十一", 11);
		fileds.put("十二", 12);
		int n1 = fileds.get(h1.getName()) == null ? 300 : fileds.get(h1.getName());
		int n2 = fileds.get(h2.getName()) == null ? 301 : fileds.get(h2.getName());
		return Integer.compare(n1,n2);
	}
	
}
