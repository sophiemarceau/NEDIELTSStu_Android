package com.lelts.tool;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Gallery;

@SuppressWarnings("deprecation")
public class MyGallery extends Gallery {

	@SuppressLint("NewApi")
	public MyGallery(Context context, AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {
		super(context, attrs, defStyleAttr);
	}
	public MyGallery(Context context) {
		super(context);
	}

	public MyGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
	float velocityY) {

	Log.v("velocityX========="+velocityX, "velocityY  ===="+velocityY);

	velocityX = velocityX>0 ? 400 : -400;

	return super.onFling(e1, e2, velocityX, velocityY);
	}
}
