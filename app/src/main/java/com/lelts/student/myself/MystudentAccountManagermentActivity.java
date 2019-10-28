package com.lelts.student.myself;

import java.io.File;
import java.io.FileOutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.strudentlelts.R;
import com.lels.bean.ExitApplication;
import com.lels.constants.Constants;
import com.lels.main.activity.ForgetPswActivity;
import com.lels.main.activity.LoginActvity;
import com.lels.student.customcontrols.CircleImageView;
import com.lelts.tool.DataCleanManager;
import com.lelts.tool.PrintTool;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MystudentAccountManagermentActivity extends Activity implements OnClickListener {

	private static final String TAG = "MySelfActivity";
	private String token;
	private PrintTool print;
	// IconUrl = 头像路径;
	// NickName = 昵称;
	// Signature = 个性签名;
	// sCodes = 绑定学员号;
	private String IconUrl;
	private String NickName;
	private String Signature;
	private String sCodes;

	// 跳转组件的生命
	private CircleImageView imageview_user_headpic;
	private RelativeLayout relative_alter_name;
	private RelativeLayout relative_alter_password;
	private RelativeLayout relative_bind_studentnum;
	private RelativeLayout relative_alter_personsignature;

	// 命名组建的生命
	private TextView textview_username;
	private TextView textview_aler_password;
	private TextView textview_alter_signmark;

	// 拍照 相册选择
	private PopupWindow mPopupWindowDialog;
	private TextView mTextViewDialogTakePicture, mTextViewDialogAlbum, mTextViewDialogCancel;
	// 用来标识请求照相功能的activity
	public static final int CAMERA_WITH_DATA = 3023;
	private String takePicturePath;// 调用相机拍摄照片的名字
	private String filePath;// 裁剪后图片的路径
	private String IMAGE_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/head_pic";

	private Bitmap bitmap_alter = null;
	private Button btn_exit;
	private ImageView imageview_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myself_myaccountmanagerment);
		print = new PrintTool(MystudentAccountManagermentActivity.this);
		getDataFromShare();
		init();
		initPop();
		getDataFromNet();
		ExitApplication.getInstance().addActivitylistExit(this);
	}

	private void getDataFromShare() {
		SharedPreferences share = this.getSharedPreferences("userinfo", MODE_PRIVATE);
		token = share.getString("Token", "");
		print.printforLog(TAG, token);
	}

	private void getDataFromNet() {
		Log.d(TAG, "解析获取我的收藏列表" + "getdatafromnet()");

		String url = new Constants().URL_MYSELF_GETAXXOUNTINFO;
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);
		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(1000 * 10);
		http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				super.onStart();

			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				try {
					Log.d(TAG, "解析获取我的数据信息" + responseInfo.result);
					JSONObject str = new JSONObject(responseInfo.result);
					String Result = str.getString("Result");
					String Infomation = str.getString("Infomation");
					String Data = str.getString("Data");
					JSONObject obj = new JSONObject(Data);

					// IconUrl = 头像路径;
					// NickName = 昵称;
					// Signature = 个性签名;
					// sCodes = 绑定学员号;

					IconUrl = obj.getString("IconUrl");
					NickName = obj.getString("NickName");
					sCodes = obj.getString("sCodes");
					Signature = obj.getString("Signature");

					setViewData();
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

	private void setViewData() {

		textview_username.setText(NickName);
		// 显示账号绑定的用户ID
		textview_aler_password.setText(sCodes);
		if (Signature.equalsIgnoreCase("null")) {
			textview_alter_signmark.setText("");
		} else {
			textview_alter_signmark.setText(Signature);
		}

		if (bitmap_alter != null) {
			imageview_user_headpic.setImageBitmap(bitmap_alter);
		} else {
			if (!IconUrl.equalsIgnoreCase("")) {

				String url = new Constants().URL_USERIMG + IconUrl;

				ImageLoader.getInstance().displayImage(url, imageview_user_headpic);
			}
		}

	}

	private void initPop() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.post_daily_picture_choose_dialog, null);
		mTextViewDialogTakePicture = (TextView) view.findViewById(R.id.textview_dialog_take_picture);
		mTextViewDialogAlbum = (TextView) view.findViewById(R.id.textview_dialog_album);
		mTextViewDialogCancel = (TextView) view.findViewById(R.id.textview_dialog_cancel);
		mPopupWindowDialog = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		mPopupWindowDialog.setFocusable(true);
		mPopupWindowDialog.update();
		mPopupWindowDialog.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
		mPopupWindowDialog.setOutsideTouchable(true);
		mTextViewDialogTakePicture.setOnClickListener(this);
		mTextViewDialogAlbum.setOnClickListener(this);
		mTextViewDialogCancel.setOnClickListener(this);
	}

	private void init() {
		textview_username = (TextView) findViewById(R.id.textview_username);
		textview_aler_password = (TextView) findViewById(R.id.textview_aler_password);
		textview_alter_signmark = (TextView) findViewById(R.id.textview_alter_signmark);

		imageview_user_headpic = (CircleImageView) findViewById(R.id.imageview_user_headpic);
		relative_alter_name = (RelativeLayout) findViewById(R.id.relative_alter_name);
		relative_alter_password = (RelativeLayout) findViewById(R.id.relative_alter_password);
		relative_bind_studentnum = (RelativeLayout) findViewById(R.id.relative_bind_studentnum);
		relative_alter_personsignature = (RelativeLayout) findViewById(R.id.relative_alter_personsignature);

		btn_exit = (Button) findViewById(R.id.btn_exit);
		imageview_back = (ImageView) findViewById(R.id.imageview_back);
		imageview_user_headpic.setOnClickListener(this);
		relative_alter_name.setOnClickListener(this);
		relative_alter_password.setOnClickListener(this);
		relative_bind_studentnum.setOnClickListener(this);
		relative_alter_personsignature.setOnClickListener(this);
		btn_exit.setOnClickListener(this);
		imageview_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageview_user_headpic:
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			if (imm.isActive()) {
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}
			if (mPopupWindowDialog != null) {
				mPopupWindowDialog.showAtLocation(findViewById(R.id.LinearLayout1),
						Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			}
			break;
		// 用户昵称
		case R.id.relative_alter_name:

			Intent intent = new Intent();
			intent.setClass(MystudentAccountManagermentActivity.this, MystudentAccountAlterNameActivity.class);
			startActivity(intent);

			break;
		// 修改密码
		case R.id.relative_alter_password:
			intent = new Intent();
			intent.setClass(MystudentAccountManagermentActivity.this, MystudentAccountAlterPasswordActivity.class);
			startActivity(intent);
			// intent = new Intent();
			// intent.setClass(MystudentAccountManagermentActivity.this,
			// ForgetPswActivity.class);
			// startActivity(intent);

			break;
		// 绑定学员号
		case R.id.relative_bind_studentnum:
			intent = new Intent();
			intent.putExtra("NickName", NickName);
			intent.setClass(MystudentAccountManagermentActivity.this, MystudentAccountBindStudentNumActivity.class);
			startActivity(intent);

			break;
		// 学生个性签名
		case R.id.relative_alter_personsignature:
			intent = new Intent();
			intent.setClass(MystudentAccountManagermentActivity.this, MystudentAccountAlterSignMarkActivity.class);
			startActivity(intent);

			break;

		case R.id.textview_dialog_take_picture:// 拍照
			String sdStatus = Environment.getExternalStorageState();
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
				Toast.makeText(MystudentAccountManagermentActivity.this, "未检测到内存卡", Toast.LENGTH_SHORT);
				return;
			}
			Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			// 下面这句指定调用相机拍照后的照片存储的路径
			takePicturePath = IMAGE_FILE_PATH + "/IMG" + System.currentTimeMillis() + ".jpg";
			File image = new File(takePicturePath);
			intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
			startActivityForResult(intent2, 2);
			if (mPopupWindowDialog != null && mPopupWindowDialog.isShowing()) {
				mPopupWindowDialog.dismiss();
			}
			break;
		case R.id.textview_dialog_album:// 相册
			intent = new Intent(Intent.ACTION_PICK, null);
			intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
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
			break;
		case R.id.btn_exit:

			logOffUser();

//			DataCleanManager.cleanSharedPreference(MystudentAccountManagermentActivity.this);
			//清空密码
			SharedPreferences stushare = getSharedPreferences("stushare", MODE_PRIVATE);
			Editor editor = stushare.edit();
			editor.putString("userPass", "");
			editor.commit();
			
			intent = new Intent();
			intent.setClass(MystudentAccountManagermentActivity.this, LoginActvity.class);
			startActivity(intent);
			finish();

			break;
		// 返回
		case R.id.imageview_back:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d(TAG, "onRestart()");
		getDataFromNet();

	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume()");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(TAG, "onActivityResult()");
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
				bitmap_alter = bitmap;
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

				sendTestResult();
				imageview_user_headpic.setImageBitmap(bitmap);
			}
		}
	}

	@SuppressWarnings("static-access")
	private void sendTestResult() {

		String url = new Constants().URL_MYSELF_UPDATE_MYICON;

		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);

		File file = new File(filePath);
		System.out.println("图片的路径为============" + filePath);
		params.addBodyParameter("file", file);

		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(1000 * 10);

		http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				try {
					Log.d("MySelfActivity", "上传头像" + responseInfo.result);
					JSONObject str = new JSONObject(responseInfo.result);
					String Result = str.getString("Result");
					String Infomation = str.getString("Infomation");
					String Data = str.getString("Data");
					if (Result.equalsIgnoreCase("true")) {
						Toast.makeText(MystudentAccountManagermentActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
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

	private void logOffUser() {

		@SuppressWarnings("static-access")
		String url = new Constants().URL_MYSELF_LOGOFFUSER;

		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);

		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(1000 * 10);

		http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				super.onStart();

			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				try {
					Log.d("MyselfSyssetActivity", "退出登录信息为" + responseInfo.result);
					JSONObject str = new JSONObject(responseInfo.result);
					String Result = str.getString("Result");
					String Infomation = str.getString("Infomation");
					String Data = str.getString("Data");

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				System.out.println("onFailure");
			}
		});
	}
}
