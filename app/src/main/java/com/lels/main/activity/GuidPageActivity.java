package com.lels.main.activity;

import yanbin.switchLayout.OnViewChangeListener;
import yanbin.switchLayout.SwitchLayout;
import com.example.strudentlelts.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class GuidPageActivity extends Activity {

	private static final String SHAREDPREFERENCES_NAME = "first_pref";
	private boolean isFirst = true;
	/** Called when the activity is first created. */
	SwitchLayout switchLayout;// 自定义的控件
	int mViewCount;// 自定义控件中子控件的个数
	int mCurSel;// 当前选中的imageView

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guidpage);
		SharedPreferences share = getSharedPreferences(SHAREDPREFERENCES_NAME, MODE_PRIVATE);
		isFirst = share.getBoolean(SHAREDPREFERENCES_NAME, true);
		// 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
		if (!isFirst) {
			Intent intent = new Intent();
			intent.setClass(GuidPageActivity.this, LoginActvity.class);
			startActivity(intent);
			finish();
		} else {
			share.edit().putBoolean(SHAREDPREFERENCES_NAME, false).commit();
		}
		init();
	}

	private void init() {
		switchLayout = (SwitchLayout) findViewById(R.id.switchLayoutID);
		// 得到子控件的个数
		mViewCount = switchLayout.getChildCount();
		// 设置第一个imageView不被激活
		switchLayout.setOnViewChangeListener(new mOnViewChangeListener());
	}

	// 自定义控件中View改变的事件监听
	private class mOnViewChangeListener implements OnViewChangeListener {
		@Override
		public void onViewChange(int view) {
			System.out.println("view:--" + view);
			if (view < 0 || mCurSel == view) {
				return;
			} else if (view > mViewCount - 1) {
				// 当滚动到第五个的时候activity会被关闭
				System.out.println("finish activity");
				finish();
			}
		}
	}
}
