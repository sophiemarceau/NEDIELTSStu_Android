package com.example.dbutils;

public class AlarmInfo {

	String title;
	String starttime;
	String endtime;
	String repeat;
	String sound;
	String remind;
	String note;
	
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

	@Override
	public String toString() {
		return "AlarmInfo [title=" + title + ", starttime=" + starttime
				+ ", endtime=" + endtime + ", repeat=" + repeat + ", sound="
				+ sound + ", remind=" + remind + ", note=" + note + "]";
	}

	
	
}
