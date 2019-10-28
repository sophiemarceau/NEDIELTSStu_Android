/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015年6月19日 
 * 
 *******************************************************************************/ 
package com.lelts.tool;

import android.content.Context;
import android.content.Intent;

/**
 * <pre>
 * 业务名:
 * 功能说明: Intent跳转
 * 编写日期:	2015年6月19日
 * 作者:	 于耀东
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class IntentUtlis {
	public static void sysStartActivity(Context context, Class<?> nameClass) {
		Intent intent = new Intent(context, nameClass);
		context.startActivity(intent);
	}

}
