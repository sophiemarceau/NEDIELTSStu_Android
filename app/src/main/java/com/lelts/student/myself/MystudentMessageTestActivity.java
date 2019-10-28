package com.lelts.student.myself;

import java.util.ArrayList;
import java.util.List;

import me.com.demo.app.view.FastListView;
import me.com.demo.app.view.FastListView.Feedback;
import me.com.demo.app.view.FastListView.FreshOrLoadListener;
import me.maxwin.view.MessageItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.strudentlelts.R;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lels.student.studyonline.adapter.StudyOnlinepublicclassAdapter;
import com.lelts.student.myself.adapter.MyselfMessageAdapter;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class MystudentMessageTestActivity extends Activity implements OnClickListener, FreshOrLoadListener {

	private RelativeLayout studyOnLine_no_data;
	private TextView studyOnLine_text;

	private FastListView listview_myself_message;
	private final String TAG = "MystudentMessageActivity";
	private String token;
	private int pageIndex = 1;
	private List<MessageItem> l_message = new ArrayList<MessageItem>();
	private MyselfMessageAdapter adapter;
	private SharedPreferences shares;
	private Editor editor;
	private ImageView image_back;
	private String type = "0";// type=[用户类型，0学生1游客，不可为空]

	Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myself_mymessage_test);
		getDataFromShare();

		init();
		getDataFromNet();

	}

	private void init() {
		studyOnLine_no_data = (RelativeLayout) findViewById(R.id.studyOnLine_no_data);
		studyOnLine_text = (TextView) findViewById(R.id.studyOnLine_text);

		image_back = (ImageView) findViewById(R.id.imageView_mystu_back);
		image_back.setOnClickListener(this);
		listview_myself_message = (FastListView) findViewById(R.id.listview_myself_message);
		shares = getSharedPreferences("flag", MODE_PRIVATE);
		editor = shares.edit();
		listview_myself_message.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View child, int position, long id) {
				return true;
			}
		});

		listview_myself_message.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View child, int position, long id) {

				MessageItem message = l_message.get(position);
				try {
					int isread = Integer.parseInt(message.IsRead.trim());
					message.IsRead = String.valueOf(isread + 1);
				} catch (Exception e) {
					// TODO: handle exception
				}
				Readmessage(l_message.get(position).getID(), l_message.get(position).getType());
				Intent intent = new Intent();
				intent.setClass(MystudentMessageTestActivity.this, MystudentMessageDetailActivity.class);
				Bundle b = new Bundle();
				editor.putInt("position", position);
				editor.commit();
				b.putString("CreateTime", l_message.get(position).getCreateTime());
				b.putString("Body", l_message.get(position).getBody());

				intent.putExtras(b);
				startActivity(intent);
				// adapter.notifyDataSetChanged();
			}
		});
		listview_myself_message.setFreshOrLoadListener(this);
		LodDialogClass.showCustomCircleProgressDialog(MystudentMessageTestActivity.this, null, getString(R.string.common_Loading));
	}

	private void getDataFromShare() {
		SharedPreferences share = MystudentMessageTestActivity.this.getSharedPreferences("userinfo",
				Context.MODE_PRIVATE);
		token = share.getString("Token", "");
		Log.d(TAG, "获取的token数值为=====" + token);
	}

	@SuppressWarnings("static-access")
	public void getDataFromNet() {
		String url = new Constants().URL_MYSELF_MYMESSAGE + "?pageIndex=" + pageIndex + "&type=" + type;// 消息
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);// 添加保密的东西
		// pageIndex=[第几页,从1开始,不可为空]
		// number=[每页显示的条数,不可为空]
		// params.addBodyParameter("pageIndex", "1");
		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(0);
		http.configDefaultHttpCacheExpiry(0);
		http.send(HttpRequest.HttpMethod.GET, url, params, new RequestCallBack<String>() {

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
						totalCount = obj_d.getString("totalCount");
						String str_list = obj_d.getString("list");

						JSONArray array = new JSONArray(str_list);
						if (pageIndex == 1) {
							// l_map_message = new
							// ArrayList<HashMap<String, Object>>();
							l_message = new ArrayList<MessageItem>();
						}

						for (int i = 0; i < array.length(); i++) {
							JSONObject obj = array.optJSONObject(i);
							MessageItem item = new MessageItem();
							item.setID(obj.getString("ID"));
							item.setIsRead(obj.getString("IsRead"));
							item.setBody(obj.getString("Body"));
							item.setType(obj.getString("Type"));
							item.setTitle(obj.getString("Title"));
							item.setCreateTime(obj.getString("CreateTime"));
							// Log.e("ocean", "i = " + i + " +++++
							// obj.getString(ID) == " + obj.getString("ID"));
							l_message.add(item);
						}
					}
					if (pageIndex == 1) {
						if (l_message.size() == 0) {
							knowIfDataBackground();
						} else {
							adapter = new MyselfMessageAdapter(MystudentMessageTestActivity.this, l_message, token);
							listview_myself_message.setAdapter(adapter);
//							adapter.notifyDataSetChanged();
						}
					} else {
						adapter.notifyDataSetChanged();
					}
					if (l_message.size()/pageIndex<10) {
						
						listview_myself_message.noticeFreshDone(true, true);
					}else{
					listview_myself_message.noticeFreshDone(true, false);
					}
					// adapter.updataadapter(l_message);
					adapter.notifyDataSetChanged();
					LodDialogClass.closeCustomCircleProgressDialog();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				System.out.println(error.toString());
				Log.d(TAG, "错误信息====" + error.toString());
				LodDialogClass.closeCustomCircleProgressDialog();
			}
		});
	}

	// 如果没有数据则显示一张背景图
	private void knowIfDataBackground() {
		listview_myself_message.setAdapter(null);
		listview_myself_message.setVisibility(View.GONE);
		studyOnLine_no_data.setVisibility(View.VISIBLE);
		studyOnLine_text.setText("暂无消息");
	}

	private void Readmessage(String id, String type) {
		String url = Constants.URL_MessageReadOrDelStuMessage + "?messageId=" + id + "&type=" + type + "&optType="
				+ "read";
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
		if (!this.l_message.isEmpty() && null != listview_myself_message) {
			MyselfMessageAdapter adapter = (MyselfMessageAdapter) listview_myself_message.getAdapter();
			adapter.updataadapter(this.l_message);
		}
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

	// @Override
	// public void onItemClick(AdapterView<?> arg0, View view, int position,
	// long id) {
	//
	// Readmessage(l_message.get(position).getID(), l_message.get(position)
	// .getType());
	// Intent intent = new Intent();
	// intent.setClass(MystudentMessageTestActivity.this,
	// MystudentMessageDetailActivity.class);
	// Bundle b = new Bundle();
	// editor.putInt("position", position);
	// editor.commit();
	// b.putString("CreateTime", l_message.get(position).getCreateTime());
	// b.putString("Body", l_message.get(position).getBody());
	//
	// intent.putExtras(b);
	// startActivity(intent);
	// adapter.notifyDataSetChanged();
	//
	// }

	@Override
	public void onNeedFresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// freshData();
				pageIndex = 1;
				getDataFromNet();
			}
		}, 3000);
	}

	String totalCount;
	@Override
	public void onNeedLoad(Feedback listener) {
		int total = TextUtils.isEmpty(totalCount) ? Integer.MAX_VALUE : Integer.parseInt(totalCount.trim());
		pageIndex++;
		System.out.println("pageIndex : " + pageIndex + ", total : " + total 
				+ ", compare : " + (total % 10 == 0 ? total / 10 : (total / 10 + 1)));
		if (pageIndex >= (total % 10 == 0 ? total / 10 : (total / 10 + 1))) {
			listener.onfeed("全部加载完成");
		} else {
			listener.onfeed("正在加载");
		}
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				getDataFromNet();
			}
		}, 3000);
	}
}
