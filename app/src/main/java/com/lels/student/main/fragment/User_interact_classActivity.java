package com.lels.student.main.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.strudentlelts.R;
import com.lels.bean.ButtonControl;
import com.lels.bean.ExitApplication;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lels.main.activity.StundentClassroom;
import com.lels.main.activity.adapter.Userinteract_Adapter;
import com.lels.student.connectionclass.activity.ConnectionReportActivity;
import com.lels.student.connectionclass.activity.PrevidTestReportActvity;
import com.lelts.student.db.SqliteDao;
import com.lelts.students.bean.User_interact_info;
import com.lelts.tool.ImageLoder;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.message.proguard.I;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class User_interact_classActivity extends Activity implements
		OnClickListener {
	private SharedPreferences share, stushare;
	private Button btn_join;
	private ListView mylistview;
	private List<User_interact_info> mylist;
	private Userinteract_Adapter myadapter;
	private EditText ed_code;
	private String info, waring, stu_imgurl;
	private Context mContext;
	private ImageView img_stu_photo;
	private RelativeLayout firstclassroom;
	private TextView txt_name, txt_day, txt_dayinfo;// 学生姓名，和距离考试天数
	private int stu_day;// 考试日期
	private String signature;
	//  "className": "班级名称",
	//    "nLessonNo": 课次号,
	//    "CreateTime": 随堂练习提交时间(时间戳 需要转化成时间格式),
	//    "PushQuestionCount": 练习数量,
	//    "CC_ID": 课次ID,
	//    "exIds": exId集合
	String className, nLessonNo, CreateTime, PushQuestionCount, CC_ID, exIds;
	private ImageView backimg;
	// 返回键
	private ImageButton img_back;
	private Editor editor;
	private SqliteDao dao = new SqliteDao();
	private TextView textView4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_interact_class);
		initview();
		LodDialogClass.showCustomCircleProgressDialog(mContext, "",
				getString(R.string.xlistview_header_hint_loading));
		findStudentOwnHistoryExercise();

		ExitApplication.getInstance().addActivity(this);
		// initdata();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	// @Override
	// protected void onResume() {
	// // TODO Auto-generated method stub
	// initview();
	// findStudentOwnHistoryExercise();
	//
	// super.onResume();
	// }
	/**
	 * 初始化控件
	 *
	 */
	private void initview() {
		// Intent geturl = getIntent();
		// stu_imgurl = geturl.getStringExtra("stu_imgurl");
		mContext = this;

		mylist = new ArrayList<User_interact_info>();
		share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		editor = share.edit();
		btn_join = (Button) findViewById(R.id.btn_user_interact_join);
		mylistview = (ListView) findViewById(R.id.interact_listview);
		btn_join.setOnClickListener(this);
		ed_code = (EditText) findViewById(R.id.ed_user_interact_number);
		txt_name = (TextView) findViewById(R.id.interact_stu_name);
		txt_day = (TextView) findViewById(R.id.interact_stu_data);
		txt_dayinfo = (TextView) findViewById(R.id.interact_stu_data_info);
		img_stu_photo = (ImageView) findViewById(R.id.interact_stu_photo);
		firstclassroom = (RelativeLayout) findViewById(R.id.user_interact_firstclassroom);

		backimg = (ImageView) findViewById(R.id.imageViewback);
		img_back = (ImageButton) findViewById(R.id.img_back_interact_stu);
		img_back.setOnClickListener(this);
		stushare = getSharedPreferences("stushare", Context.MODE_PRIVATE);
		textView4 = (TextView) findViewById(R.id.textView4);
		// 头像信息
		stu_imgurl = stushare.getString("iconUrl", "");
		if (stu_imgurl.equals("") || stu_imgurl.equals("null")) {

		} else {
			BitmapUtils bitutil = new BitmapUtils(mContext);
			bitutil.display(backimg, Constants.URL_USERIMG + stu_imgurl);
			backimg.setAlpha(0.6f);
			ImageLoader.getInstance().displayImage(
					Constants.URL_USERIMG + stu_imgurl, img_stu_photo);
		}
		System.out.println("stu_imgurl=================" + stu_imgurl);
		// 设置 学生姓名，距离天数（如果负数则不显示）
		txt_name.setText(stushare.getString("NickName", ""));
		signature = stushare.getString("signature", "");
		stu_day = stushare.getInt("DateDiff", 0);
		System.out.println("距离考试的天数===" + stu_day);
		if (stu_day >= 0) {
			txt_day.setText(stu_day + "");
			txt_day.setTextSize(25);
		} else if (signature.equalsIgnoreCase("null")
				|| signature.equalsIgnoreCase("")) {
			txt_dayinfo.setText("这家伙很懒，还没有个人签名");
			txt_day.setVisibility(View.GONE);
			textView4.setVisibility(View.GONE);
		} else {
			txt_dayinfo.setText(signature);
			txt_dayinfo.setMaxEms(15);
			txt_dayinfo.setSingleLine(true);
			txt_dayinfo.setEllipsize(TruncateAt.END);
			txt_day.setVisibility(View.GONE);
			textView4.setVisibility(View.GONE);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		// 同步课堂
		case R.id.btn_user_interact_join:
			if (ButtonControl.isFastClick()) {
				return;
			} else {
				LodDialogClass.showCustomCircleProgressDialog(mContext, null,
						getString(R.string.common_Loading));
				getGroupInfo();
				juest_code_classes();
				dao.deleteDatabase(mContext);
			}

			break;
		// 返回键
		case R.id.img_back_interact_stu:
			// finish();
			ExitApplication.getInstance().exitK();
			break;
		default:
			break;
		}
	}

	/**
	 * 获取课堂ID iphone、Android 教师/学生App
	 */
	private void getGroupInfo() {
		// TODO Auto-generated method stub
		String iDurl = Constants.URL_ActiveClass_getIdByPassCode;
		RequestParams params = new RequestParams();
		System.out.println("--------------" + ed_code.getText().toString());
		params.addHeader("Authentication", share.getString("Token", ""));
		params.addBodyParameter("passCode", ed_code.getText().toString());
		HttpUtils util = new HttpUtils();
		util.send(HttpMethod.POST, iDurl, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						String result = arg0.result;
						System.out.println(result);
						try {
							JSONObject obj = new JSONObject(result);
							JSONObject data = obj.getJSONObject("Data");
							String activeClassId = data
									.getString("activeClassId");
							System.out.println("activeClassId---"
									+ activeClassId);
							editor.putString("activeClassId", activeClassId);
							editor.commit();

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				});

	}

	/**
	 * 验证课堂暗号
	 * 
	 *
	 */
	private void juest_code_classes() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		HttpUtils utils = new HttpUtils();
		System.out.println("token==========" + share.getString("Token", ""));
		utils.configCurrentHttpCacheExpiry(0);
		utils.configDefaultHttpCacheExpiry(0);
		utils.send(HttpMethod.GET, Constants.URL_StudentGetStudentOnLine
				+ "?passCode=" + ed_code.getText(), params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						LodDialogClass.closeCustomCircleProgressDialog();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						String result = arg0.result;
						try {
							JSONObject obj = new JSONObject(result);
							info = obj.getString("Result");
							waring = obj.getString("Infomation");
							System.out.println("===============info" + info);
							System.out
									.println("===============waring" + waring);
							if (info.equals("true")) {
								Intent intent = new Intent(mContext,
										StundentClassroom.class);
								System.out.println("+++++++++++++code"
										+ ed_code.getText().toString());
								SharedPreferences share = getSharedPreferences(
										"stuinfo", MODE_PRIVATE);
								SharedPreferences.Editor editor = share.edit();
								editor.putString("code", ed_code.getText()
										.toString());
								editor.commit();
								// intent.putExtra("code",
								// ed_code.getText().toString());
								startActivity(intent);
							} else {
								Toast.makeText(mContext, waring,
										Toast.LENGTH_SHORT).show();
							}
							LodDialogClass.closeCustomCircleProgressDialog();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						System.out.println("tescher_result==================="
								+ result);
					}
				});
	}

	/**
	 * 方法说明：随堂互动首页，获取学员自己的历史随堂练习列表
	 *
	 */
	private void findStudentOwnHistoryExercise() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		HttpUtils utils = new HttpUtils();
		System.out.println("token==========" + share.getString("Token", ""));
		utils.send(HttpMethod.GET, Constants.URL_findStudentOwnHistoryExercise,
				params, new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						// 关闭正在加载dialog
						LodDialogClass
								.closeCustomCircleProgressDialog();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						//  "className": "班级名称",
						//    "nLessonNo": 课次号,
						//    "CreateTime": 随堂练习提交时间(时间戳 需要转化成时间格式),
						//    "PushQuestionCount": 练习数量,
						//    "CC_ID": 课次ID,
						//    "exIds": exId集合
						String result = arg0.result;
						try {
							JSONObject obj = new JSONObject(result);
							JSONArray data = obj.getJSONArray("Data");
							if (data.length() == 0) {
								System.out.println("Date为空，没有答过题");
								firstclassroom.setVisibility(View.VISIBLE);
								mylistview.setVisibility(View.INVISIBLE);
								// 关闭正在加载dialog
								LodDialogClass
										.closeCustomCircleProgressDialog();
							} else {
								System.out.println("Date不为空，有答过题");
								firstclassroom.setVisibility(View.INVISIBLE);
								mylistview.setVisibility(View.VISIBLE);
								for (int i = 0; i < data.length(); i++) {
									JSONObject info = data.getJSONObject(i);
									className = info.getString("className");
									nLessonNo = info.getString("nLessonNo");
									CC_ID = info.getString("CC_ID");
									exIds = info.getString("exIds");
									CreateTime = info.getString("CreateTime");
									PushQuestionCount = info
											.getString("PushQuestionCount");
									User_interact_info user_info = new User_interact_info();
									user_info.setClassname(className);
									user_info.setClasscount(nLessonNo);
									user_info.setClasstime(CreateTime);
									user_info.setExId(exIds);
									user_info.setCc_id(CC_ID);
									user_info.setTextcount(PushQuestionCount);
									mylist.add(user_info);
								}

								System.out.println("获取学生历史的答题记录===="
										+ mylist.toString());
								myadapter = new Userinteract_Adapter(mylist,
										mContext);
								mylistview.setAdapter(myadapter);
								// 关闭正在加载dialog
								LodDialogClass
										.closeCustomCircleProgressDialog();
								mylistview
										.setOnItemClickListener(new OnItemClickListener() {

											@Override
											public void onItemClick(
													AdapterView<?> parent,
													View view, int position,
													long id) {
												// TODO Auto-generated method
												// stub
												Intent intent = new Intent(
														User_interact_classActivity.this,
														PrevidTestReportActvity.class);
												intent.putExtra("className",
														mylist.get(position)
																.getClassname());
												intent.putExtra(
														"nLessonNo",
														mylist.get(position)
																.getClasscount());
												intent.putExtra("exIds", mylist
														.get(position)
														.getExId());
												intent.putExtra("CC_ID", mylist
														.get(position)
														.getCc_id());
												startActivity(intent);
											}
										});
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						System.out
								.println("findStudentOwnHistoryExercise_result==================="
										+ result);
					}
				});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent) 返回键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			ExitApplication.getInstance().exitK();
			return false;
		}

		return super.onKeyDown(keyCode, event);
	}
}
