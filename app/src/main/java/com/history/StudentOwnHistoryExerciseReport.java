package com.history;

import java.util.List;

public class StudentOwnHistoryExerciseReport extends History {
	private String className;
	private String nLessonNo;
	
	private String paperName;
	private String Accuracy;
	private String CostTime;

	@Override
	public String toString() {
		return "StudentOwnHistoryExerciseReport [className=" + className
				+ ", nLessonNo=" + nLessonNo + ", paperName=" + paperName
				+ ", Accuracy=" + Accuracy + ", CostTime=" + CostTime + "]";
	}
	
	public StudentOwnHistoryExerciseReport(String paperName, String accuracy,
			String costTime) {
		
		this.paperName = paperName;
		Accuracy = accuracy;
		CostTime = costTime;
		super.type = 0;
	}
	public StudentOwnHistoryExerciseReport(String className, String nLessonNo,
			List<ExerciseReport> exerciseReport) {
		super();
		this.className = className;
		this.nLessonNo = nLessonNo;
		
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
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getnLessonNo() {
		return nLessonNo;
	}
	public void setnLessonNo(String nLessonNo) {
		this.nLessonNo = nLessonNo;
	}
	
	
}
