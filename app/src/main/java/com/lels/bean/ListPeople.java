/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015-8-18 
 * 
 *******************************************************************************/
package com.lels.bean;

import java.util.List;

public class ListPeople {
	private String date;
	private List<Learn> list;

	public ListPeople(String date, List<Learn> list) {
		super();
		this.date = date;
		this.list = list;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<Learn> getList() {
		return list;
	}

	public void setList(List<Learn> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "ListPeople [date=" + date + ", list=" + list + "]";
	}
}
