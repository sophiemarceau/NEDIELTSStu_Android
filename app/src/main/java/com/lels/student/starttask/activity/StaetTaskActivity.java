package com.lels.student.starttask.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.strudentlelts.R;
import com.lels.student.starttask.fragment.StuMokao_fragment;
import com.lels.student.starttask.fragment.StuStart_fragment;
import com.lels.student.starttask.fragment.StuTask_fragment;

import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class StaetTaskActivity extends FragmentActivity implements
		OnClickListener {
	private TextView tv_score;
	private SharedPreferences stushare;
	private int total;
	private ViewPager viewPager;// 页卡内容
	private ImageView imageView;// 动画图片
	private TextView task, start, mokao;// 选项名称
	private List<Fragment> fragments;// Tab页面列表
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度
	private int selectedColor, unSelectedColor;
	/** 页卡总数 **/
	private static final int pageSize = 3;
	private ImageButton stu_task_back_img;
	private StuTask_fragment stutask_fm = new StuTask_fragment();
	private StuStart_fragment start_fm = new StuStart_fragment();
	private StuMokao_fragment mokao_fm = new StuMokao_fragment();
	private int t,thread_t;
	private Handler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_starttask);

		initView();
		handler = new Handler() {
			public void handleMessage(Message msg) {
				int pro = (Integer) msg.obj;
				tv_score.setText(pro + "");
			};
		};
		thread.start();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initView() {
		stushare = getSharedPreferences("stushare", MODE_PRIVATE);
		total = stushare.getInt("FinishTaskCount", 0);
		System.out.println("total----"+total+"---FinishTaskCount---"+stushare.getInt("FinishTaskCount", 0)+"---DownMaterialsCount---"+stushare.getInt("DownMaterialsCount", 0));
		tv_score = (TextView) findViewById(R.id.tv_score);
		double tv_total = Math.random();// 0-9随机数
		System.out.println((int)(tv_total * (20 - 10) + 10));

		if (total == 1) {
			t = (int)(tv_total * (20 - 10) + 10);
		} else if (total == 2) {
			t = (int) (tv_total * (40 - 21) + 21);
		} else if (total >= 3 && total <= 5) {
			t = (int) (tv_total * (60 - 41) + 41);
		} else if (total > 5 && total <= 10) {
			t = (int) (tv_total * (80 - 61) + 61);
		} else if (total > 10) {
			t = (int) (tv_total * (99 - 81) + 81);
		}else{
			t=(int) (tv_total * (9 - 0) + 0);
		}
		selectedColor = getResources().getColor(R.color.font_color);
		unSelectedColor = getResources().getColor(R.color.black);
		stu_task_back_img = (ImageButton) findViewById(R.id.stu_task_back_img);
		stu_task_back_img.setOnClickListener(this);
		InitImageView();
		InitTextView();
		InitViewPager();
	}

	Thread thread = new Thread(){
		public void run() {
			while (thread_t<t) {
				thread_t+=2;
				Message mss = new Message();
				mss.obj = thread_t;
				handler.handleMessage(mss);
			}
		};
	};
	
	/**
	 * 初始化Viewpager页
	 */
	private void InitViewPager() {
		viewPager = (ViewPager) findViewById(R.id.vP);
		fragments = new ArrayList<Fragment>();
		fragments.add(stutask_fm);
		fragments.add(start_fm);
		fragments.add(mokao_fm);
		viewPager.setAdapter(new myPagerAdapter(getSupportFragmentManager(),
				fragments));
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		viewPager.setOffscreenPageLimit(0);
	}

	/**
	 * 初始化头标
	 * 
	 */
	private void InitTextView() {
		task = (TextView) findViewById(R.id.starttask_task_tv);
		start = (TextView) findViewById(R.id.starttask_start_tv);
		mokao = (TextView) findViewById(R.id.starttask_mokao_tv);

		task.setTextColor(selectedColor);
		start.setTextColor(unSelectedColor);
		mokao.setTextColor(unSelectedColor);

		task.setText("任务");
		start.setText("学习");
		mokao.setText("模考");

		task.setOnClickListener(new MyOnClickListener(0));
		start.setOnClickListener(new MyOnClickListener(1));
		mokao.setOnClickListener(new MyOnClickListener(2));
	}

	/**
	 * 初始化动画，这个就是页卡滑动时，下面的横线也滑动的效果，在这里需要计算一些数据
	 */

	private void InitImageView() {
		imageView = (ImageView) findViewById(R.id.starttask_cursor);
		bmpW = BitmapFactory.decodeResource(getResources(),
				R.drawable.tab_selected_bg).getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = (screenW / pageSize - bmpW) / 2;// 计算偏移量--(屏幕宽度/页卡总数-图片实际宽度)/2
													// = 偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		imageView.setImageMatrix(matrix);// 设置动画初始位置
	}

	/**
	 * 头标点击监听
	 */
	private class MyOnClickListener implements OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		public void onClick(View v) {

			switch (index) {
			case 0:
				task.setTextColor(selectedColor);
				start.setTextColor(unSelectedColor);
				mokao.setTextColor(unSelectedColor);
				break;
			case 1:
				task.setTextColor(unSelectedColor);
				start.setTextColor(selectedColor);
				mokao.setTextColor(unSelectedColor);
				break;
			case 2:
				task.setTextColor(unSelectedColor);
				start.setTextColor(unSelectedColor);
				mokao.setTextColor(selectedColor);
				break;
			}
			viewPager.setCurrentItem(index);
		}

	}

	/**
	 * 为选项卡绑定监听器
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		int two = one * 2;// 页卡1 -> 页卡3 偏移量

		public void onPageScrollStateChanged(int index) {
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		public void onPageSelected(int index) {
			Animation animation = new TranslateAnimation(one * currIndex, one
					* index, 0, 0);// 显然这个比较简洁，只有一行代码。
			currIndex = index;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			imageView.startAnimation(animation);

			switch (index) {
			case 0:
				task.setTextColor(selectedColor);
				start.setTextColor(unSelectedColor);
				mokao.setTextColor(unSelectedColor);
				break;
			case 1:
				task.setTextColor(unSelectedColor);
				start.setTextColor(selectedColor);
				mokao.setTextColor(unSelectedColor);
				break;
			case 2:
				task.setTextColor(unSelectedColor);
				start.setTextColor(unSelectedColor);
				mokao.setTextColor(selectedColor);
				break;
			}
		}
	}

	/**
	 * 定义适配器
	 */
	class myPagerAdapter extends FragmentPagerAdapter {
		private List<Fragment> fragmentList;

		public myPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
			super(fm);
			this.fragmentList = fragmentList;
		}

		/**
		 * 得到每个页面
		 */
		@Override
		public Fragment getItem(int arg0) {
			System.out.println("--------getitem");
			return (fragmentList == null || fragmentList.size() == 0) ? null
					: fragmentList.get(arg0);
		}

		/**
		 * 每个页面的title
		 */
		@Override
		public CharSequence getPageTitle(int position) {
			return null;
		}

		/**
		 * 页面的总个数
		 */
		@Override
		public int getCount() {
			return fragmentList == null ? 0 : fragmentList.size();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View) 按钮的监听
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.stu_task_back_img:
			finish();
			break;

		default:
			break;
		}
	}
}
