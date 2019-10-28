package com.lelts.students.bean;

public class LessonDataInfo {
	String lessondata;

	public String getLessondata() {
		return lessondata;
	}

	public void setLessondata(String lessondata) {
		this.lessondata = lessondata;
	}

	@Override
	public String toString() {
		return "LessonDataInfo [lessondata=" + lessondata
				+ ", getLessondata()=" + getLessondata() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}

}
