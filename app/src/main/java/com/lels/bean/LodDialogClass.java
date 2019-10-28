package com.lels.bean;

import android.content.Context;

import com.lels.costum_widget.CustomProgressDialog;

public class LodDialogClass {
	
	private static CustomProgressDialog customDialog;
	
	// 显示自定义加载对话框
	public static CustomProgressDialog showCustomCircleProgressDialog(Context context,String title, String msg) {
		System.out.println("peoiijvkjvjdkjgkdslhglds");
		if (customDialog != null) {
			try {
				customDialog.cancel();
				customDialog = null;
			} catch (Exception e) {
			}
		}

		customDialog = CustomProgressDialog.createDialog(context);
		// dialog.setIndeterminate(false);
		// dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		customDialog.setCancelable(false);// 是否可用用"返回键"取消
		customDialog.setTitle(title);
		customDialog.setMessage(msg);

		try {
			customDialog.show();
		} catch (Exception e) {
		}
		
		customDialog.setCanceledOnTouchOutside(false);//设置dilog点击屏幕空白处不消失
		return customDialog;
	}
	
	
	
	// 关闭自定义加载对话框
	public static void closeCustomCircleProgressDialog() {
		if (customDialog != null) {
			try {
				customDialog.cancel();
				customDialog = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
