package com.lels.student.studyonline;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
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
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.drocode.swithcer.GuideGallery;
import com.drocode.swithcer.ImageAdapter;
import com.example.strudentlelts.R;
import com.lels.constants.Constants;
import com.lels.student.studyonline.fragment.StudentonlineDataListFragment;
import com.lels.student.studyonline.fragment.StudentonlineListFragment;
import com.lelts.tool.PrintTool;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class StudyOnlineActivity extends FragmentActivity implements OnClickListener {

	private static final String TAG = "StudyOnlineActivity";
	private View mView;
	private List<HashMap<String, Object>> list;
	private TextView textview_data_screening;
	private String token;
	// 广告图片的 数组
	private List<HashMap<String, Object>> l_images = new ArrayList<HashMap<String, Object>>();
	private String[] str_images;
	// 内容类 iewpager
	private ViewPager viewPager;// 页卡内容
	private ImageView imageView;// 动画图片
	private TextView voiceAnswer, healthPedia;// 选项名称
	private List<Fragment> fragments;// Tab页面列表
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度
	private int selectedColor, unSelectedColor;
	/** 页卡总数 **/
	private static final int pageSize = 2;
	private PrintTool print;
	private TextView textview_screen;
	private int pagenum = 0;
	private StudentonlineListFragment solcFragment;
	private StudentonlineDataListFragment soldFragment;

	// 循环滚动 广告位设置
	public List<String> urls;
	public GuideGallery images_ga;
	private int positon = 0;
	private Thread timeThread = null;
	public boolean timeFlag = true;
	private boolean isExit = false;
	public ImageTimerTask timeTaks = null;
	Uri uri;
	Intent intent;
	int galleryPosition = 0;

	private LinearLayout pointLinear;
	// 返回键
	private ImageView image_back;

	private SharedPreferences stuShare;
	private String tagUsername;

	private String nameCode = "0";
	private String roleId = "0";
	private String timeType = "0";
	private String beginDate;
	private String endDate;

	private String nameCode1 = "0";
	private String fileType1 = "0";
	private String timeType1 = "0";

	private String beginDate1;
	private String endDate1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_studyonline_main);
		getDataFromShare();
		initAdv();

		initView();
		getDataFromNetForImages();

	}

	private void getDataFromShare() {
		SharedPreferences share = StudyOnlineActivity.this.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		token = share.getString("Token", "");
		Log.d("StudyPlanActivity", "获取的token数值为=====" + token);
	}

	private void initAdv() {
		timeTaks = new ImageTimerTask();
		autoGallery.scheduleAtFixedRate(timeTaks, 5000, 5000);
		timeThread = new Thread() {
			public void run() {
				while (!isExit) {
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					synchronized (timeTaks) {
						if (!timeFlag) {
							timeTaks.timeCondition = true;
							timeTaks.notifyAll();
						}
					}
					timeFlag = true;
				}
			};
		};
		timeThread.start();
	}

	@Override
	protected void onStart() {
		super.onStart();
		init();
	}

	private void init() {
		images_ga = (GuideGallery) findViewById(R.id.image_wall_gallery);
		images_ga.setImageActivity(this);
		pointLinear = (LinearLayout) findViewById(R.id.gallery_point_linear);
		// pointLinear.setBackgroundColor(Color.argb(200, 135, 135, 152));
	}

	private void initView() {
		print = new PrintTool(StudyOnlineActivity.this);
		viewPager = (ViewPager) findViewById(R.id.vPager);
		textview_screen = (TextView) findViewById(R.id.textview_screen);
		imageView = (ImageView) findViewById(R.id.cursor);
		voiceAnswer = (TextView) findViewById(R.id.tab_1);
		healthPedia = (TextView) findViewById(R.id.tab_2);

		textview_screen.setOnClickListener(this);

		selectedColor = getResources().getColor(R.color.font_color);
		unSelectedColor = getResources().getColor(R.color.tab_title_normal_color);
		image_back = (ImageView) findViewById(R.id.imgview_back);
		image_back.setOnClickListener(this);

		stuShare = getSharedPreferences("stushare", MODE_PRIVATE);
		tagUsername = stuShare.getString("username", "");
		InitImageView();
		InitTextView();
		InitViewPagerButton(tagUsername);
	}

	/**
	 * 初始化Viewpager页
	 */
	private void InitViewPagerButton(String tagusername) {

		fragments = new ArrayList<Fragment>();
		// 添加 内容 页
		solcFragment = new StudentonlineListFragment(tagusername);
		soldFragment = new StudentonlineDataListFragment(tagusername);
		fragments.add(solcFragment);
		fragments.add(soldFragment);
		viewPager.setAdapter(new myPagerAdapter(getSupportFragmentManager(), fragments));
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	/**
	 * 初始化头标
	 */
	private void InitTextView() {
		voiceAnswer.setTextColor(selectedColor);
		healthPedia.setTextColor(unSelectedColor);
		voiceAnswer.setText("雅思云课堂");
		healthPedia.setText("学习资料");
		voiceAnswer.setOnClickListener(new MyOnClickListener(0));
		healthPedia.setOnClickListener(new MyOnClickListener(1));
	}

	/**
	 * 初始化动画，这个就是页卡滑动时，下面的横线也滑动的效果，在这里需要计算一些数据
	 */

	private void InitImageView() {
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.tab_selected_bg).getWidth();// 获取图片宽度
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
				voiceAnswer.setTextColor(selectedColor);
				healthPedia.setTextColor(unSelectedColor);
				pagenum = 0;
				break;
			case 1:
				healthPedia.setTextColor(selectedColor);
				voiceAnswer.setTextColor(unSelectedColor);
				pagenum = 1;
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
		// int two = one * 2;// 页卡1 -> 页卡3 偏移量

		public void onPageScrollStateChanged(int index) {
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		public void onPageSelected(int index) {
			Animation animation = new TranslateAnimation(one * currIndex, one * index, 0, 0);// 显然这个比较简洁，只有一行代码。
			currIndex = index;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			imageView.startAnimation(animation);

			switch (index) {
			case 0:
				voiceAnswer.setTextColor(selectedColor);
				healthPedia.setTextColor(unSelectedColor);
				pagenum = 0;
				break;
			case 1:
				healthPedia.setTextColor(selectedColor);
				voiceAnswer.setTextColor(unSelectedColor);
				pagenum = 1;
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
			return (fragmentList == null || fragmentList.size() == 0) ? null : fragmentList.get(arg0);
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

	@Override
	public void onClick(View v) {

		print.printforLog(TAG, "" + String.valueOf(pagenum));

		switch (v.getId()) {
		case R.id.textview_screen:
			Intent intent = new Intent();
			if (pagenum == 0) {
				intent.setClass(StudyOnlineActivity.this, StudyOnlineScreenClassActivity.class);
				Bundle b = new Bundle();
				b.putString("nameCode", nameCode);
				b.putString("roleId", roleId);
				b.putString("timeType", timeType);
				b.putString("beginDate", beginDate);
				b.putString("endDate", endDate);
				intent.putExtras(b);
				startActivityForResult(intent, 10);
			} else {
				intent.setClass(StudyOnlineActivity.this, StudyOnlineScreenDataActivity.class);
				Bundle b = new Bundle();
				b.putString("fileType1", nameCode1);
				b.putString("roleId1", fileType1);
				b.putString("uploadYear1", timeType1);
				b.putString("beginDate", beginDate);
				b.putString("endDate", endDate);
				intent.putExtras(b);
				startActivityForResult(intent, 11);
			}
			break;
		case R.id.imgview_back:
			// IntentUtlis.sysStartActivity(StudyOnlineActivity.this,
			// MainActivity.class);
			finish();
		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null) {
			return;
		} else {
			switch (requestCode) {
			case 10:
				Bundle b = data.getExtras();
				nameCode = b.getString("nameCode");
				roleId = b.getString("roleId");
				timeType = b.getString("timeType");
				beginDate = b.getString("beginDate");
				endDate = b.getString("endDate");
				solcFragment.getDataFromNetForScreen(nameCode, roleId, timeType, beginDate, endDate, 1);
				System.out
						.println("--" + nameCode + "--" + roleId + "--" + timeType + "--" + beginDate + "--" + endDate);
				break;
			case 11:
				Bundle bb = data.getExtras();
				nameCode1 = bb.getString("fileType1");
				fileType1 = bb.getString("roleId1");
				timeType1 = bb.getString("uploadYear1");
				beginDate1 = bb.getString("beginDate1");
				endDate1 = bb.getString("endDate1");
				System.out.println(nameCode1 + "    " + fileType1 + "    " + timeType1 + "    " + beginDate1);
				soldFragment.getDataFromNetForScreen(nameCode1, fileType1, timeType1, beginDate1, endDate1, 1);
				break;
			default:
				break;
			}
		}
	}

	public void changePointView(int cur) {
		View view = pointLinear.getChildAt(positon);
		View curView = pointLinear.getChildAt(cur);
		if (view != null && curView != null) {
			ImageView pointView = (ImageView) view;
			ImageView curPointView = (ImageView) curView;
			pointView.setBackgroundResource(R.drawable.feature_point);
			curPointView.setBackgroundResource(R.drawable.feature_point_cur);
			positon = cur;
		}
	}

	final Handler autoGalleryHandler = new Handler() {
		public void handleMessage(Message message) {
			super.handleMessage(message);
			switch (message.what) {
			case 1:
				images_ga.setSelection(message.getData().getInt("pos"));
				break;
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		timeFlag = false;
	}

	@Override
	protected void onPause() {
		super.onPause();
		timeTaks.timeCondition = false;
	}

	public class ImageTimerTask extends TimerTask {
		public volatile boolean timeCondition = true;

		// int gallerypisition = 0;
		public void run() {
			synchronized (this) {
				while (!timeCondition) {
					try {
						Thread.sleep(100);
						// wait();
					} catch (InterruptedException e) {
						Thread.interrupted();
					}
				}
			}
			try {
				galleryPosition = images_ga.getSelectedItemPosition() + 1;
				System.out.println(galleryPosition + "");
				Message msg = new Message();
				Bundle date = new Bundle();
				date.putInt("pos", galleryPosition);
				msg.setData(date);
				msg.what = 1;
				autoGalleryHandler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	Timer autoGallery = new Timer();

	private void getDataFromNetForImages() {

		@SuppressWarnings("static-access")
		String url = new Constants().URL_MYSELF_LOADADVERTISEMENTS;
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);// 添加保密的东西
		// type = [用户类型，1001:教师、1002:学生、1003:预测,举例:"1003",不可为空]
		params.addBodyParameter("type", "1002");
		// params.addBodyParameter("pageIndex", "1");
		HttpUtils http = new HttpUtils();
		// http.configCurrentHttpCacheExpiry(1000 * 10);
		http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				Log.d("DataDFm", "获取资料广告的数据" + responseInfo.result);
				try {
					JSONObject str = new JSONObject(responseInfo.result);// 获取请求的数据信息
					String Result = str.getString("Result");
					String Infomation = str.getString("Infomation");
					String Data = str.getString("Data");
					JSONObject obj_d = new JSONObject(Data);
					String llist = obj_d.getString("list");
					if (!llist.equalsIgnoreCase("")) {
						JSONArray array = new JSONArray(llist);
						str_images = new String[array.length()];
						for (int i = 0; i < array.length(); i++) {
							JSONObject obj = array.optJSONObject(i);
							HashMap<String, Object> map = new HashMap<String, Object>();
							// Sort = 广告序号;
							// Interval = 时间间隔，每隔多少秒切换广告;
							// Title = 广告标题;
							// Picture = 广告封面，图片名称;
							// Link = 广告地址;
							// Content = 广告正文;
							map.put("Sort", obj.getString("Sort"));
							map.put("Interval", obj.getString("Interval"));
							map.put("Title", obj.getString("Title"));
							map.put("Picture", obj.getString("Picture"));
							map.put("Link", obj.getString("Link"));
							map.put("Content", obj.getString("Content"));
							// if (!obj.getString("Picture").toString()
							// .equalsIgnoreCase("null")) {
							// str_images[i] = new
							// Constants().URL_IMAGE_ADVERT
							// + obj.getString("Picture")
							// .toString();
							str_images[i] = obj.getString("Picture").toString();
							// } else {
							// str_images[i] =
							// "http://www.apkbus.com/template/devc/style/logo.png";
							// }
							l_images.add(map);
						}
						System.out.println("viewpaper的图片路径====" + l_images.toString());
					}
					InitViewPager(l_images);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				System.out.println("onFailure");
				System.out.println(error.toString());
			}
		});
	}

	protected void InitViewPager(final List<HashMap<String, Object>> l_images2) {

		ImageAdapter imageAdapter = new ImageAdapter(this, str_images);
		images_ga.setAdapter(imageAdapter);
		for (int i = 0; i < l_images2.size(); i++) {
			ImageView pointView = new ImageView(this);
			if (i == 0) {
				pointView.setBackgroundResource(R.drawable.feature_point_cur);
			} else
				pointView.setBackgroundResource(R.drawable.feature_point);
			pointLinear.addView(pointView);
		}
		images_ga.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Intent intent = new Intent();
				String ss = l_images2.get(position % l_images2.size()).get("Link").toString();
				System.out.println("Link.toString()===" + ss);
				if (!ss.equalsIgnoreCase("")) {
					intent.setClass(StudyOnlineActivity.this, StudyADVDetailActivity.class);
				} else {
					intent.setClass(StudyOnlineActivity.this, StudyADVDetailActivity.class);
				}
				Bundle b = new Bundle();
				b.putString("url", l_images2.get(position % l_images2.size()).get("Link").toString());
				b.putString("Title", l_images2.get(position % l_images2.size()).get("Title").toString());
				b.putString("Content", l_images2.get(position % l_images2.size()).get("Content").toString());
				intent.putExtras(b);
				startActivity(intent);
			}
		});
	}
}
