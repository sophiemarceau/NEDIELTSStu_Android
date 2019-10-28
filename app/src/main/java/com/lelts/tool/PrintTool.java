package com.lelts.tool;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class PrintTool {
	private Context mcontext;
	private boolean isprint = true;

	public PrintTool(Context context) {
		this.mcontext = context;
	}

	public void printforToast(String str) {
		if (isprint == true) {
			Toast.makeText(mcontext, str, Toast.LENGTH_SHORT).show();
		}
	}

	private void printforsys(String str) {

		if (isprint == true) {
			System.out.println(str);
		}
	}

	public void printforLog(String tag, String str) {
		if (isprint == true) {
			Log.d(tag, str);
		}
	}
}
