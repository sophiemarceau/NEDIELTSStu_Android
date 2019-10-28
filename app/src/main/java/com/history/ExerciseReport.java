package com.history;

public class ExerciseReport {
	private String paperName;
	private String Accuracy;
	private String CostTime;
	
	public ExerciseReport() {
		super();
	}
	public ExerciseReport(String paperName, String accuracy, String costTime) {
		super();
		this.paperName = paperName;
		Accuracy = accuracy;
		CostTime = costTime;
	}
	public String getPaperName() {
		return paperName;
	}
	public void setPaperName(String paperName) {
		this.paperName = paperName;
	}
	public String getAccuracy() {
		return Accuracy;
	}
	public void setAccuracy(String accuracy) {
		Accuracy = accuracy;
	}
	public String getCostTime() {
		return CostTime;
	}
	public void setCostTime(String costTime) {
		CostTime = costTime;
	}
}
