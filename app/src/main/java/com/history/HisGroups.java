package com.history;

import java.util.List;

public class HisGroups extends History {
	private String remind;
	private String sName ;
	private String nLessonNo;
	private String GroupCnt;
	private String GroupNum;
	private List<Groups> myGroup ;

	public List<Groups> getMyGroup() {
		return myGroup;
	}
	public void setMyGroup(List<Groups> myGroup) {
		this.myGroup = myGroup;
	}
	public HisGroups(String sName, String nLessonNo, String groupCnt,
			String groupNum,List<Groups> myGroup) {
		this.myGroup = myGroup;
		this.sName = sName;
		this.nLessonNo = nLessonNo;
		GroupCnt = groupCnt;
		GroupNum = groupNum;
		super.type = 1;
	}
	public String getsName() {
		return sName;
	}
	public void setsName(String sName) {
		this.sName = sName;
	}
	public String getnLessonNo() {
		return nLessonNo;
	}
	public void setnLessonNo(String nLessonNo) {
		this.nLessonNo = nLessonNo;
	}
	public String getGroupCnt() {
		return GroupCnt;
	}
	public void setGroupCnt(String groupCnt) {
		GroupCnt = groupCnt;
	}
	public String getGroupNum() {
		return GroupNum;
	}
	public void setGroupNum(String groupNum) {
		GroupNum = groupNum;
	}
	
}
