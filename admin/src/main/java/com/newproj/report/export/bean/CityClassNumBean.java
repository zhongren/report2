package com.newproj.report.export.bean;

import java.util.List;

import com.newproj.report.sys.dto.City;

public class CityClassNumBean extends City{
	
	private int  classesNums;
	
	private  int  schoolNums;
	
	private String cityName;
	
	private List<ClassNumsBean>  nums ;

	public List<ClassNumsBean> getNums() {
		return nums;
	}

	public void setNums(List<ClassNumsBean> nums) {
		this.nums = nums;
	}

	public int getClassesNums() {
		return classesNums;
	}

	public void setClassesNums(int classesNums) {
		this.classesNums = classesNums;
	}

	public int getSchoolNums() {
		return schoolNums;
	}

	public void setSchoolNums(int schoolNums) {
		this.schoolNums = schoolNums;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	
	
	
	
}
