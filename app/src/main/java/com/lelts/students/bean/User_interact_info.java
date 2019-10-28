 
package com.lelts.students.bean;


public class User_interact_info {
	private String classname;
	private String classcount;
	private String classtime;
	private String textcount;
	private String cc_id;
	private String exId;
	public String getCc_id() {
		return cc_id;
	}
	public void setCc_id(String cc_id) {
		this.cc_id = cc_id;
	}
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	public String getClasscount() {
		return classcount;
	}
	public void setClasscount(String classcount) {
		this.classcount = classcount;
	}
	public String getClasstime() {
		return classtime;
	}
	public void setClasstime(String classtime) {
		this.classtime = classtime;
	}
	public String getTextcount() {
		return textcount;
	}
	public void setTextcount(String textcount) {
		this.textcount = textcount;
	}
	
	public String getExId() {
		return exId;
	}
	public void setExId(String exId) {
		this.exId = exId;
	}
	public User_interact_info() {
		super();
	}
	@Override
	public String toString() {
		return "User_interact_info [classname=" + classname + ", classcount="
				+ classcount + ", classtime=" + classtime + ", textcount="
				+ textcount + "]";
	}
	
	
	
	

}
