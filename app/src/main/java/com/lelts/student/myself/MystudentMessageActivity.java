package com.lelts.student.myself;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.strudentlelts.R;
import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.lels.constants.Constants;
import com.lelts.student.myself.adapter.MyselfMessageAdapter;
import com.lelts.tool.IntentUtlis;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class MystudentMessageActivity extends Activity implements
		OnClickListener {

	private SwipeListView listview_myself_message;

	private static final String TAG = "MystudentMessageActivity";

	private String token;

	private View mview;

	private String pageIndex = "1";

	private List<HashMap<String, Object>> l_map_message;

	public static int deviceWidth;
	private MyselfMessageAdapter adapter;

	private SharedPreferences shares;

	private Editor editor;
	private ImageView image_back;
	
	private String type = "0";// type=[用户类型，0学生1游客，不可为空]

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myself_mymessage);

		init();

		getdatafromshare();
		getdatafromnet();

		deviceWidth = getDeviceWidth();
		reload();

	}

	private void init() {

		image_back = (ImageView) findViewById(R.id.imageView_mystu_back);
		image_back.setOnClickListener(this);

		listview_myself_message = (SwipeListView) findViewById(R.id.listview_myself_message);

		listview_myself_message
				.setSwipeListViewListener(new TestBaseSwipeListViewListener());
		shares = getSharedPreferences("flag", MODE_PRIVATE);
		editor = shares.edit();

		// listview_myself_message
		// .setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// Intent intent = new Intent();
		// intent.setClass(MystudentMessageActivity.this,
		// MystudentMessageDetailActivity.class);
		// Bundle b = new Bundle();
		// b.putString("Body",
		// l_map_message.get(position).get("Body")
		// .toString());
		// b.putString("CreateTime", l_map_message.get(position)
		// .get("CreateTime").toString());
		// intent.putExtras(b);
		// startActivity(intent);
		//
		// }
		// });
	}

	private void getdatafromshare() {

		SharedPreferences share = MystudentMessageActivity.this
				.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		token = share.getString("Token", "");

		Log.d(TAG, "获取的token数值为=====" + token);

	}

	@SuppressWarnings("static-access")
	private void getdatafromnet() {

		String url = new Constants().URL_MYSELF_MYMESSAGE + "?pageIndex="
				+ pageIndex+"&type="+type;// 消息

		RequestParams params = new RequestParams();

		params.addHeader("Authentication", token);// 添加保密的东西

		// pageIndex=[第几页,从1开始,不可为空]
		//  number=[每页显示的条数,不可为空]
		// params.addBodyParameter("pageIndex", "1");

		HttpUtils http = new HttpUtils();
//		http.configCurrentHttpCacheExpiry(1000 * 10);

		http.send(HttpRequest.HttpMethod.GET, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						super.onStart();

					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {

						Log.d(TAG, "获取资料的数据" + responseInfo.result);

						try {

							JSONObject str = new JSONObject(responseInfo.result);// 获取请求的数据信息

							String Result = str.getString("Result");
							String Infomation = str.getString("Infomation");
							String Data = str.getString("Data");

							if (!Data.equalsIgnoreCase("null")) {

								// "ID": 1010,
								// "Body": "4444444444444",
								// "Type": 0,
								// "CreateTime": "2015-07-15"

								JSONObject obj_d = new JSONObject(Data);
								String totalCount = obj_d
										.getString("totalCount");
								String str_list = obj_d.getString("list");

								JSONArray array = new JSONArray(str_list);

								l_map_message = new ArrayList<HashMap<String, Object>>();

								for (int i = 0; i < array.length(); i++) {
									JSONObject obj = array.optJSONObject(i);
									HashMap<String, Object> map = new HashMap<String, Object>();
									map.put("ID", obj.getString("ID"));
									map.put("IsRead", obj.getString("IsRead"));
									map.put("Body", obj.getString("Body"));
									map.put("Type", obj.getString("Type"));
									map.put("Title", obj.getString("Title"));
									map.put("CreateTime",
											obj.getString("CreateTime"));
									l_map_message.add(map);
								}

								if (l_map_message.size() > 0) {

//									adapter = new MyselfMessageAdapter(
//											MystudentMessageActivity.this,
//											l_map_message,
//											listview_myself_message, token);

									listview_myself_message.setAdapter(adapter);
								}
								adapter.notifyDataSetChanged();
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						System.out.println(error.toString());
						Log.d(TAG, "错误信息====" + error.toString());

					}

				});

	}

	private int getDeviceWidth() {
		return getResources().getDisplayMetrics().widthPixels;
	}

	private void reload() {
		listview_myself_message.setSwipeMode(SwipeListView.SWIPE_MODE_LEFT);
		listview_myself_message
				.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_REVEAL);
		// mSwipeListView.setSwipeActionRight(settings.getSwipeActionRight());
		listview_myself_message.setOffsetLeft((float) (deviceWidth * 1 / 1.3));
		// mSwipeListView.setOffsetRight(convertDpToPixel(settings.getSwipeOffsetRight()));
		listview_myself_message.setAnimationTime(0);
		listview_myself_message.setSwipeOpenOnLongPress(false);
	}

	class TestBaseSwipeListViewListener extends BaseSwipeListViewListener {

		@Override
		public void onClickFrontView(int position) {

			Readmessage(l_map_message.get(position).get("ID").toString(),
					l_map_message.get(position).get("Type").toString());
			Intent intent = new Intent();
			intent.setClass(MystudentMessageActivity.this,
					MystudentMessageDetailActivity.class);
			Bundle b = new Bundle();
			editor.putInt("position", position);
			editor.commit();
			b.putString("CreateTime",
					l_map_message.get(position).get("CreateTime").toString());
			b.putString("Body", l_map_message.get(position).get("Body")
					.toString());

			intent.putExtras(b);
			startActivity(intent);
			adapter.notifyDataSetChanged();
		}

		@Override
		public void onDismiss(int[] reverseSortedPositions) {
			for (int position : reverseSortedPositions) {
				l_map_message.remove(position);
			}
			adapter.notifyDataSetChanged();
		}
	}

	private void Readmessage(String id, String type) {
		String url = Constants.URL_MessageReadOrDelStuMessage + "?messageId="
				+ id + "&type=" + type + "&optType=" + "read";
		HttpUtils util = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);// 添加保密的东西
		util.configCurrentHttpCacheExpiry(0);
		util.send(HttpMethod.GET, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {

			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {

				String result = arg0.result;
				System.out.println("执行了这个方法" + result);
			}
		});
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		getdatafromnet();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageView_mystu_back:
			finish();
			break;

		default:
			break;
		}
	}
}
