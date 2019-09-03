package com.newproj.report.quotasituation.dto;

public class CalculateProcessInfo {

	private int total ;
	private int current ;
	private long startTimestamp ;
	private String startTime ;
	private float secondsPreTime ;
	
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getCurrent() {
		return current;
	}
	public void setCurrent(int current) {
		this.current = current;
	}
	public long getStartTimestamp() {
		return startTimestamp;
	}
	public void setStartTimestamp(long startTimestamp) {
		this.startTimestamp = startTimestamp;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public float getSecondsPreTime() {
		return secondsPreTime;
	}
	public void setSecondsPreTime(float secondsPreTime) {
		this.secondsPreTime = secondsPreTime;
	}
}
