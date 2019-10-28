package com.history;

public class Groups {
	private String IconUrl;
	private String sName;
	public String getIconUrl() {
		return IconUrl;
	}
	public Groups() {
		super();
	}
	public Groups(String iconUrl, String sName) {
		super();
		IconUrl = iconUrl;
		this.sName = sName;
	}
	public void setIconUrl(String iconUrl) {
		IconUrl = iconUrl;
	}
	public String getsName() {
		return sName;
	}
	public void setsName(String sName) {
		this.sName = sName;
	}
}
