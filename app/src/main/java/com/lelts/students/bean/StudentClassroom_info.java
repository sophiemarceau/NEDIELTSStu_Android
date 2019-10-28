package com.lelts.students.bean;


public class StudentClassroom_info {
	private String name;
	private String pic;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public StudentClassroom_info() {
		super();
	}
	public StudentClassroom_info(String name, String pic) {
		super();
		this.name = name;
		this.pic = pic;
	}
	

}
