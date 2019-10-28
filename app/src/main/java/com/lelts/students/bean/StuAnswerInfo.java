/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015年7月29日 
 * 
 *******************************************************************************/
package com.lelts.students.bean;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2015年7月29日
 * 作者:	 于耀东
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class StuAnswerInfo {
	// private int id;
	private String s_id;
	/**
	 * @return the s_id
	 */
	public String getS_id() {
		return s_id;
	}

	/**
	 * @param s_id the s_id to set
	 */
	public void setS_id(String s_id) {
		this.s_id = s_id;
	}

	private String code;
	private String answer;
	private String time;

	// public int getId() {
	// return id;
	// }
	// public void setId(int id) {
	// this.id = id;
	// }
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}


	/**
	 * @param s_id
	 * @param code
	 * @param answer
	 * @param time
	 */
	public StuAnswerInfo(String s_id, String code, String answer, String time) {
		super();
		this.s_id = s_id;
		this.code = code;
		this.answer = answer;
		this.time = time;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StuAnswerInfo [s_id=" + s_id + ", code=" + code + ", answer="
				+ answer + ", time=" + time + "]";
	}

}
