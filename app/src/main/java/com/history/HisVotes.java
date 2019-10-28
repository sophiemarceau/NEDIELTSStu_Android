package com.history;

import java.util.List;

public class HisVotes extends History {

	
	public String ID;

	public String ActiveClassID;

	public String Subject;

	public List<InClass> opts;

	

	public HisVotes( String iD, String activeClassID,
			String subject, List<InClass> opts) {
		ID = iD;
		ActiveClassID = activeClassID;
		Subject = subject;
		this.opts = opts;
		super.type=2;
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
