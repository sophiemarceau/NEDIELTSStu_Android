package com.lelts.student.db;

import java.io.Serializable;

public class ClockInfo extends BaseEntity implements Serializable{

	private static final long serialVersionUID = -6284598217068556706L;
	
	String title;
	String starttime;
	String endtime;
	String repeat;
	String sound;
	String remind;
	String note;
	String select_time;
	/*boolean isSelected;
	
	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}*/

	public String getSelect_time() {
		return select_time;
	}

	public void setSelect_time(String select_time) {
		this.select_time = select_time;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getRepeat() {
		return repeat;
	}

	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}

	public String getSound() {
		return sound;
	}

	public void setSound(String sound) {
		this.sound = sound;
	}

	public String getRemind() {
		return remind;
	}

	public void setRemind(String remind) {
		this.remind = remind;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
