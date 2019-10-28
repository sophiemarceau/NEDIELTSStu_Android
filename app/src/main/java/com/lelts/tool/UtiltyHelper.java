/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015年9月15日 
 * 
 *******************************************************************************/ 
package com.lelts.tool;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2015年9月15日
 * 作者:	 于耀东
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class UtiltyHelper {
	
	/**
	 * 是否为空白,包括null、""和空格
	 * 
	 * @param o
	 * @return
	 */
	public static boolean isEmpty(Object o) {
		return o == null || o.toString().trim().length() == 0;
	}


}
