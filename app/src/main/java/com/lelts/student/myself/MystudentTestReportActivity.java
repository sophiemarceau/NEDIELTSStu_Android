package com.lelts.student.myself;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.strudentlelts.R;
import com.google.gson.JsonObject;
import com.lels.constants.Constants;
import com.lelts.student.myself.adapter.PopGroupAdapter;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.http.client.entity.FileUploadEntity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MystudentTestReportActivity extends Activity implements
		OnClickListener {

	private RelativeLayout relative_takepicture;
	// 拍照 相册选择
	private PopupWindow mPopupWindowDialog;

	private TextView mTextViewDialogTakePicture, mTextViewDialogAlbum,
			mTextViewDialogCancel;
	// 用来标识请求照相功能的activity
	public static final int CAMERA_WITH_DATA = 3023;

	private String takePicturePath;// 调用相机拍摄照片的名字

	private String filePath = "";// 裁剪后图片的路径

	private String IMAGE_FILE_PATH = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/test_pic";

	private TextView textview_send;// 发送

	private TextView textview_listening_score, textview_reading_score,
			textView_writing_score, textView_speaking_score,
			textView_overall_score;

	private ImageView imageView_talkpicture;
	private ImageView img_back;
	// 设置 popup
	// pop的声明
	private PopupWindow pop_class;
	private ListView lv_group;
	private View view;

	private List<String> groups;
	// private List<HashMap<String, Object>> groups;

	private String token;
	private String listenscore;
	private String readscore;
	private String writescore;
	private String speakscore;
	private String totalscore;
	private String Imgea_path = Constants.URL_USERIMG;
	private String score_path = Constants.URL_HomegetUserReport;
	private String totalScore2;
	private float listenf, readf, writef, speakf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myself_mytestreport);
		getdatafromshare();
		init();
		getscorehttp();
		initpopupdata();
		initpop();
	}

	private void getscorehttp() {
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);
		HttpUtils util = new HttpUtils();
		final BitmapUtils bitutil = new BitmapUtils(
				MystudentTestReportActivity.this);
		util.send(HttpMethod.POST, score_path, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {

					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						/*
						 * SGR_ID=[主键ID]   UID=[用户ID]   ListenScore=[听力成绩]
						 *   ReadScore=[阅读成绩]   WriteScore=[写作成绩]
						 *   SpeakScore=[口语成绩]   TotalScore=[总成绩]
						 *   ReportImgName=[报告图片名称] textview_listening_score,
						 * textview_reading_score, textView_writing_score,
						 * textView_speaking_score, textView_overall_score
						 */
						String result = arg0.result;
						System.out.println(result);
						try {
							JSONObject obj = new JSONObject(result);
							JSONObject obj1 = obj.getJSONObject("Data");
							if (obj1 != null) {

								String SGR_ID = obj1.getString("SGR_ID");
								String UID = obj1.getString("UID");
								String ListenScore = obj1
										.getString("ListenScore");
								textview_listening_score.setText(ListenScore);
								String ReadScore = obj1.getString("ReadScore");
								textview_reading_score.setText(ReadScore);
								String WriteScore = obj1
										.getString("WriteScore");
								textView_writing_score.setText(WriteScore);
								String SpeakScore = obj1
										.getString("SpeakScore");
								textView_speaking_score.setText(SpeakScore);
								totalScore2 = obj1.getString("TotalScore");
								textView_overall_score.setText(totalScore2);

								String ReportImgName = obj1
										.getString("ReportImgName");
								if (!"".equals(ReportImgName)||!"null".equals(ReportImgName)) {

									bitutil.display(imageView_talkpicture,
											Imgea_path + "/" + ReportImgName);
								}
								System.out.println(ReportImgName + "/////");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});

	}

	private void getdatafromshare() {
		SharedPreferences share = this.getSharedPreferences("userinfo",
				MODE_PRIVATE);
		token = share.getString("Token", "");

	}

	private void initpopupdata() {
		// 加载数据
		groups = new ArrayList<String>();

		groups.add("0.0");
		groups.add("1.0");
		groups.add("1.5");
		groups.add("2.0");
		groups.add("2.5");
		groups.add("3.0");
		groups.add("3.5");
		groups.add("4.0");
		groups.add("4.5");
		groups.add("5.0");
		groups.add("5.5");
		groups.add("6.0");
		groups.add("6.5");
		groups.add("7.0");
		groups.add("7.5");
		groups.add("8.0");
		groups.add("8.5");
		groups.add("9.0");
	}

	private void init() {
		relative_takepicture = (RelativeLayout) findViewById(R.id.relative_takepicture);
		imageView_talkpicture = (ImageView) findViewById(R.id.imageView_talkpicture);
		textview_listening_score = (TextView) findViewById(R.id.textview_listening_score);
		textview_reading_score = (TextView) findViewById(R.id.textview_reading_score);
		textView_writing_score = (TextView) findViewById(R.id.textView_writing_score);
		textView_speaking_score = (TextView) findViewById(R.id.textView_speaking_score);
		textView_overall_score = (TextView) findViewById(R.id.textView_overall_score);

//		imageview_testresult = (ImageView) findViewById(R.id.imageview_testresult);
		img_back = (ImageView) findViewById(R.id.imageview_back);
		textview_send = (TextView) findViewById(R.id.textview_send);

		relative_takepicture.setOnClickListener(this);
		textview_listening_score.setOnClickListener(this);
		textview_reading_score.setOnClickListener(this);
		textView_writing_score.setOnClickListener(this);
		textView_speaking_score.setOnClickListener(this);
		textView_overall_score.setOnClickListener(this);
		img_back.setOnClickListener(this);
		textview_send.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.relative_takepicture:
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			if (imm.isActive()) {
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}
			if (mPopupWindowDialog != null) {
				mPopupWindowDialog.showAtLocation(
						findViewById(R.id.relative_takepicture), Gravity.BOTTOM
								| Gravity.CENTER_HORIZONTAL, 0, 0);
			}
			break;
		case R.id.textview_dialog_take_picture:// 拍照
			String sdStatus = Environment.getExternalStorageState();
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
				Toast.makeText(MystudentTestReportActivity.this, "未检测到内存卡",
						Toast.LENGTH_SHORT);
				return;
			}
			Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			// 下面这句指定调用相机拍照后的照片存储的路径
			takePicturePath = IMAGE_FILE_PATH + "/IMG"
					+ System.currentTimeMillis() + ".jpg";
			File image = new File(takePicturePath);
			intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
			startActivityForResult(intent2, 2);
			if (mPopupWindowDialog != null && mPopupWindowDialog.isShowing()) {
				mPopupWindowDialog.dismiss();
			}
			break;
		case R.id.textview_dialog_album:// 相册
			Intent intent = new Intent(Intent.ACTION_PICK, null);
			intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					"image/*");
			takePicturePath = getIntent().getStringExtra("data");
			startActivityForResult(intent, 1);
			if (mPopupWindowDialog != null && mPopupWindowDialog.isShowing()) {
				mPopupWindowDialog.dismiss();
			}
			break;
		case R.id.textview_dialog_cancel: // 取消
			if (mPopupWindowDialog != null && mPopupWindowDialog.isShowing()) {
				mPopupWindowDialog.dismiss();
			}

		case R.id.textview_listening_score:

			showpop(textview_listening_score, 1);
			break;
		case R.id.textview_reading_score:

			showpop(textview_reading_score, 2);
			break;
		case R.id.textView_writing_score:
			showpop(textView_writing_score, 3);
			break;
		case R.id.textView_speaking_score:
			showpop(textView_speaking_score, 4);
			break;

		case R.id.textview_send:
			sendtestresult();
			break;
		// 后退返回键
		case R.id.imageview_back:
			finish();
		default:
			break;
		}
	}

	/**
	 * 学生端考试汇报 iphone、Android 学生App [Auth]：token [URL]：/Home/sendReport
	 * [Method]：POST [Args]：  ListenScore=[听力成绩]  ReadScore=[阅读成绩]
	 *  WriteScore=[写作成绩]  SpeakScore=[口语成绩]  TotalScore=[总成绩]  file=[上传的汇报图片文件]
	 */
	@SuppressWarnings("static-access")
	private void sendtestresult() {

		listenscore = textview_listening_score.getText().toString();
		readscore = textview_reading_score.getText().toString();
		writescore = textView_writing_score.getText().toString();
		speakscore = textView_speaking_score.getText().toString();
		totalscore = textView_overall_score.getText().toString();

		System.out.println(totalscore);
		String url = new Constants().URL_MYSELF_TESTRESULT_SENDREPORT;
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);

		params.addBodyParameter("ListenScore", listenscore);
		params.addBodyParameter("ReadScore", readscore);
		params.addBodyParameter("WriteScore", writescore);
		params.addBodyParameter("SpeakScore", speakscore);
		params.addBodyParameter("TotalScore", totalscore);
		// 判断路径是否为空
		if (filePath.equalsIgnoreCase("")) {
			Toast.makeText(MystudentTestReportActivity.this, "考试报告不可为空", 0)
					.show();
		} else {
			File file = new File(filePath);
			System.out.println("图片的路径为============" + filePath + "--"
					+ listenscore + readscore);
			params.addBodyParameter("file", file);
		}

		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(1000 * 10);

		http.send(HttpRequest.HttpMethod.POST, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						super.onStart();
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						try {
							Log.d("MystudentTestreportactivity", "上传考试成绩"
									+ responseInfo.result);
							JSONObject str = new JSONObject(responseInfo.result);
							String Result = str.getString("Result");
							String Infomation = str.getString("Infomation");
							String Data = str.getString("Data");
							if (Result.equalsIgnoreCase("true")) {
								Toast.makeText(
										MystudentTestReportActivity.this,
										"提交成功", Toast.LENGTH_SHORT).show();
							}

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

	private void initpop() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.post_daily_picture_choose_dialog,
				null);
		mTextViewDialogTakePicture = (TextView) view
				.findViewById(R.id.textview_dialog_take_picture);
		mTextViewDialogAlbum = (TextView) view
				.findViewById(R.id.textview_dialog_album);
		mTextViewDialogCancel = (TextView) view
				.findViewById(R.id.textview_dialog_cancel);
		mPopupWindowDialog = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		mPopupWindowDialog.setFocusable(true);
		mPopupWindowDialog.update();
		mPopupWindowDialog.setBackgroundDrawable(new BitmapDrawable(
				getResources(), (Bitmap) null));
		mPopupWindowDialog.setOutsideTouchable(true);
		mTextViewDialogTakePicture.setOnClickListener(this);
		mTextViewDialogAlbum.setOnClickListener(this);
		mTextViewDialogCancel.setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (resultCode == RESULT_OK) {
				switch (requestCode) {
				// 如果是直接从相册获取
				case 1:
					startPhotoZoom(data.getData());
					break;
				// 如果是调用相机拍照时
				case 2:
					File temp = new File(takePicturePath);
					startPhotoZoom(Uri.fromFile(temp));
					break;
				// 取得裁剪后的图片
				case 3:
					if (data != null) {
						setPicToView(data);
					}
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap bitmap = extras.getParcelable("data");
			FileOutputStream b = null;
			File file = new File(IMAGE_FILE_PATH);
			file.mkdirs();
			filePath = IMAGE_FILE_PATH + System.currentTimeMillis() + ".jpg";
			try {
				b = new FileOutputStream(filePath);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
				imageView_talkpicture.setImageBitmap(bitmap);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			} finally {
				try {
					b.flush();
					b.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void showpop(final TextView mtextView, final int str) {
		if (pop_class == null) {
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.pop_myself_testreport, null);
			lv_group = (ListView) view
					.findViewById(R.id.pop_listview_myself_result_class);

			PopGroupAdapter groupAdapter = new PopGroupAdapter(this, groups);
			lv_group.setAdapter(groupAdapter);
			// 创建一个PopuWidow对象
			pop_class = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
		}
		// 使其聚集
		pop_class.setFocusable(true);
		// 设置允许在外点击消失
		pop_class.setOutsideTouchable(true);

		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		pop_class.setBackgroundDrawable(new BitmapDrawable());
		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		// 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
		int xPos = windowManager.getDefaultDisplay().getWidth() / 2
				- pop_class.getWidth() / 2;
		Log.i("coder", "xPos:" + xPos);

		// pop_class.showAsDropDown(mtextView, xPos, 0);
		pop_class.showAsDropDown(mtextView);

		lv_group.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {

				mtextView.setText(groups.get(position).toString());

				switch (str) {
				case 1:
					String listen = groups.get(position).toString();
					listenf = Float.valueOf(listen);
					break;
				case 2:
					String read = groups.get(position).toString();
					readf = Float.valueOf(read);
					break;
				case 3:
					String write = groups.get(position).toString();
					writef = Float.valueOf(write);
					break;
				case 4:
					String speak = groups.get(position).toString();
					speakf = Float.valueOf(speak);
					break;
				default:
					break;
				}
				float total;
				if (textView_overall_score.getText().toString().equals("0.0")) {

					total = (listenf + readf + writef + speakf) / 4;

				} else {
					float listen = Float.valueOf(textview_listening_score
							.getText().toString());
					float read = Float.valueOf(textview_reading_score.getText()
							.toString());
					float writ = Float.valueOf(textView_writing_score.getText()
							.toString());
					float speak = Float.valueOf(textView_speaking_score
							.getText().toString());
					total = (listen + read + writ + speak) / 4;
				}
				int totalint = (int) total;
				float value = total - totalint;
				float totalf = totalint;
				if (value >= 0.25 && value < 0.75) {
					textView_overall_score.setText((totalf + 0.5) + "");
				} else if (value >= 0.75) {
					textView_overall_score.setText((totalf + 1) + "");
				} else {
					textView_overall_score.setText(totalf + "");
					System.out.println("total-------" + total);
				}
				if (pop_class != null) {
					pop_class.dismiss();
				}
			}
		});
	}

}
