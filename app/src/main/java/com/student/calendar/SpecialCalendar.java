package com.student.calendar;

import java.util.Calendar;

import android.util.Log;

public class SpecialCalendar {

	private int daysOfMonth = 0;      
	private int dayOfWeek = 0;        

	
	
	/**
	 * is leapyear
	 * */
	public boolean isLeapYear(int year) {
		if (year % 100 == 0 && year % 400 == 0) {
			return true;
		} else if (year % 100 != 0 && year % 4 == 0) {
			return true;
		}
		return false;
	}
/**
 * get day of this month
 * */
	public int getDaysOfMonth(boolean isLeapyear, int month) {
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			daysOfMonth = 31;
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			daysOfMonth = 30;
			break;
		case 2:
			if (isLeapyear) {
				daysOfMonth = 29;
			} else {
				daysOfMonth = 28;
			}

		}
		return daysOfMonth;
	}
	/**
	 * 具体某一天是星期几
	 * */
	public int getWeekdayOfMonth(int year, int month){
		Calendar cal = Calendar.getInstance();
		cal.set(year, month-1, 1);
		dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)-1;
		return dayOfWeek;
	}
	
	
}