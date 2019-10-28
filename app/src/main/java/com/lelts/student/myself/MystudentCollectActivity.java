package com.lelts.student.myself;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.strudentlelts.R;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lels.student.studyonline.DataPublicclassDetailTestActivity;
import com.lelts.student.myself.fragment.DataFragment;
import com.lelts.student.myself.fragment.ProdictFragment;
import com.lelts.student.myself.fragment.PublicclassFragment;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import android.animation.ArgbEvaluator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

public class MystudentCollectActivity extends FragmentActivity implements OnClickListener {

	private ViewPager viewPager;// 页卡内容
	private ImageView imageView;// 动画图片
	private TextView voiceAnswer, healthPedia, pDected;// 选项名称
	private List<Fragment> fragments;// Tab页面列表
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度
	private int selectedColor, unSelectedColor;
	/** 页卡总数 **/
	private static final int pageSize = 3;

	private ImageView imageView_back;

	private PublicclassFragment publicClassFm;
	private DataFragment dataFm;
	private ProdictFragment prodictFm;
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myself_mycollect);
		initView();
	}

	private void initView() {
		imageView_back = (ImageView) findViewById(R.id.imageView_back);
		selectedColor = getResources().getColor(R.color.font_color);
		unSelectedColor = getResources().getColor(R.color.black);
		InitImageView();
		InitTextView();
		InitViewPager();
		imageView_back.setOnClickListener(this);
	}

	/**
	 * 初始化Viewpager页
	 */
	private void InitViewPager() {
		viewPager = (ViewPager) findViewById(R.id.vPager);
		fragments = new ArrayList<Fragment>();

		publicClassFm = new PublicclassFragment();
		dataFm = new DataFragment();
		prodictFm = new ProdictFragment();

		fragments.add(publicClassFm);
		fragments.add(dataFm);
		fragments.add(prodictFm);

		viewPager.setAdapter(new myPagerAdapter(getSupportFragmentManager(), fragments));
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	/**
	 * 初始化头标
	 * 
	 */
	private void InitTextView() {
		voiceAnswer = (TextView) findViewById(R.id.tab_1);
		healthPedia = (TextView) findViewById(R.id.tab_2);
		pDected = (TextView) findViewById(R.id.tab_3);

		voiceAnswer.setTextColor(selectedColor);
		healthPedia.setTextColor(unSelectedColor);
		pDected.setTextColor(unSelectedColor);

		voiceAnswer.setText("雅思云课堂");
		healthPedia.setText("资料");
		pDected.setText("预测");

		voiceAnswer.setOnClickListener(new MyOnClickListener(0));
		healthPedia.setOnClickListener(new MyOnClickListener(1));
		pDected.setOnClickListener(new MyOnClickListener(2));
	}

	/**
	 * 初始化动画，这个就是页卡滑动时，下面的横线也滑动的效果，在这里需要计算一些数据
	 */

	private void InitImageView() {
		imageView = (ImageView) findViewById(R.id.cursor);
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
				pDected.setTextColor(unSelectedColor);
				break;
			case 1:
				healthPedia.setTextColor(selectedColor);
				voiceAnswer.setTextColor(unSelectedColor);
				pDected.setTextColor(unSelectedColor);
				break;
			case 2:
				pDected.setTextColor(selectedColor);
				voiceAnswer.setTextColor(unSelectedColor);
				healthPedia.setTextColor(unSelectedColor);
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
			Animation animation = new TranslateAnimation(one * currIndex, one * index, 0, 0);// 显然这个比较简洁，只有一行代码。
			currIndex = index;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			imageView.startAnimation(animation);

			switch (index) {
			case 0:
				voiceAnswer.setTextColor(selectedColor);
				healthPedia.setTextColor(unSelectedColor);
				pDected.setTextColor(unSelectedColor);
				break;
			case 1:
				healthPedia.setTextColor(selectedColor);
				voiceAnswer.setTextColor(unSelectedColor);
				pDected.setTextColor(unSelectedColor);
				break;
			case 2:
				pDected.setTextColor(selectedColor);
				voiceAnswer.setTextColor(unSelectedColor);
				healthPedia.setTextColor(unSelectedColor);
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
		switch (v.getId()) {
		case R.id.imageView_back:
			finish();
			break;

		default:
			break;
		}
	}
	
	public void cancelPublicCollection(final int position) {
		System.out.println("long click cancelPublicCollection");
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("你确定要删除收藏吗？");
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				publicClassFm.cancelCollection(position);
			}
		});
		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	public void onItemClickPublicCollection(final int position){
		System.out.println("on item click cancelDataCollection");
		publicClassFm.onitemclick(position);
	}
	
	public void cancelProdictCollection(final int position) {
		System.out.println("long click cancelProdictCollection");
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("你确定要删除收藏吗？");
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				prodictFm.cancelCollection(position);
			}
		});
		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	public void onItemClickProdictCollection(final int position){
		System.out.println("on item click cancelDataCollection");
		prodictFm.onitemclick(position);
	}
	
	public void cancelDataCollection(final int position) {
		System.out.println("long click cancelDataCollection");
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("你确定要删除收藏吗？");
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dataFm.cancelCollection(position);
			}
		});
		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	public void onItemClickDataCollection(final int position){
		System.out.println("on item click cancelDataCollection");
		dataFm.onitemclick(position);
	}
	

}
