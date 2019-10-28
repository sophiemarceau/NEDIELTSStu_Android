package com.history;

import java.util.List;

public class OutClass {
	//投票
	private String ID;// 投票ID,voteId,
	private String ActiveClassID;// = 互动课堂ID,
	private String Subject;// = 互动课堂ID,
	private List<InClass> opts;
	
	public OutClass() {
		super();
	}

	public OutClass(String iD, String activeClassID, String subject,
			List<InClass> opts) {
		super();
		ID = iD;
		ActiveClassID = activeClassID;
		Subject = subject;
		this.opts = opts;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getActiveClassID() {
		return ActiveClassID;
	}

	public void setActiveClassID(String activeClassID) {
		ActiveClassID = activeClassID;
	}

	public String getSubject() {
		return Subject;
	}

	public void setSubject(String subject) {
		Subject = subject;
	}

	public List<InClass> getOpts() {
		return opts;
	}

	public void setOpts(List<InClass> opts) {
		this.opts = opts;
	}
}
