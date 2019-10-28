package com.lelts.students.bean;

public class LessonInfo {
	// "id": 29908,
	// "sCode": "YA10113",
	// "SectEnd": 1436508000000,
	// "sNameBr": "朝阳国贸校区瑞赛商务楼6层VIP3",
	// "sNameBc": "IELTS1对1班（基础）",
	// "sAddress": "北京市朝阳区建国路128号中航工业大厦",
	// "SectBegin": 1436508000000,
	// "nowLessonId": 29905,
	// "nLessonNo":

	String id;
	String sCode;
	String SectEnd;
	String sNameBr;
	String sNameBc;
	String sAddress;
	String SectBegin;
	String nowLessonId;
	String nLessonNo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getsCode() {
		return sCode;
	}

	public void setsCode(String sCode) {
		this.sCode = sCode;
	}

	public String getSectEnd() {
		return SectEnd;
	}

	public void setSectEnd(String sectEnd) {
		SectEnd = sectEnd;
	}

	public String getsNameBr() {
		return sNameBr;
	}

	public void setsNameBr(String sNameBr) {
		this.sNameBr = sNameBr;
	}

	public String getsNameBc() {
		return sNameBc;
	}

	public void setsNameBc(String sNameBc) {
		this.sNameBc = sNameBc;
	}

	public String getsAddress() {
		return sAddress;
	}

	public void setsAddress(String sAddress) {
		this.sAddress = sAddress;
	}

	public String getSectBegin() {
		return SectBegin;
	}

	public void setSectBegin(String sectBegin) {
		SectBegin = sectBegin;
	}

	public String getNowLessonId() {
		return nowLessonId;
	}

	public void setNowLessonId(String nowLessonId) {
		this.nowLessonId = nowLessonId;
	}

	public String getnLessonNo() {
		return nLessonNo;
	}

	public void setnLessonNo(String nLessonNo) {
		this.nLessonNo = nLessonNo;
	}

	@Override
	public String toString() {
		return "LessonInfo [id=" + id + ", sCode=" + sCode + ", SectEnd="
				+ SectEnd + ", sNameBr=" + sNameBr + ", sNameBc=" + sNameBc
				+ ", sAddress=" + sAddress + ", SectBegin=" + SectBegin
				+ ", nowLessonId=" + nowLessonId + ", nLessonNo=" + nLessonNo
				+ "]";
	}

}
