/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015年6月24日 
 * 
 *******************************************************************************/
package com.lelts.tool;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.strudentlelts.R;
import com.lels.constants.Constants;
import com.lels.main.activity.LoginActvity;
import com.lels.student.chatroom.activity.KickOffDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import io.rong.imkit.RongIM;
import io.rong.imkit.RongIM.UserInfoProvider;
import io.rong.imkit.RongIMClientWrapper;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ConnectCallback;
import io.rong.imlib.RongIMClient.ConnectionStatusListener;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.model.UserInfo;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2015年6月24日
 * 作者:	 于耀东
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class ImageLoder extends Application implements ConnectionStatusListener, UserInfoProvider {
	private Context mContext;
	private int curIndex;
	private SharedPreferences stu;

	public int getCurIndex() {
		return curIndex;
	}

	public void setCurIndex(int curIndex) {
		this.curIndex = curIndex;
	}

	/*
	 * public static void getTaskList(Context context) { ActivityManager am =
	 * (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	 * PackageManager pm = context.getPackageManager(); try {
	 * List<RunningTaskInfo> list = am.getRunningTasks(100); for
	 * (RunningTaskInfo info : list) { if
	 * (info.topActivity.getPackageName().equals(MY_PKG_NAME) &&
	 * info.baseActivity.getPackageName().equals(MY_PKG_NAME)) { isAppRunning =
	 * true; break; } // } } catch (SecurityException se) {
	 * se.printStackTrace(); } }
	 */
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// 初始化
		RongIM.init(this);
		// new
		// setOtherListener();
		// new
		RongIMClient.init(this);
		mContext = getApplicationContext();
		PushAgent mPushAgent = PushAgent.getInstance(mContext);
		mPushAgent.enable();
		System.out.println("推送消息  app" + curIndex);
		UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
			@Override
			public void dealWithCustomAction(final Context context, final UMessage msg) {
				// TODO Auto-generated method stub
				super.dealWithCustomAction(context, msg);
				new Handler().post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub

						boolean flag = isTopActivy("com.example.strudentlelts", context);
						if (flag) {
							System.out.println("进入应用");

							View layout = LayoutInflater.from(context).inflate(R.layout.custom, null);
							TextView title = (TextView) layout.findViewById(R.id.tvTitleToast);
							title.setText("您有新消息");
							TextView text = (TextView) layout.findViewById(R.id.tvTextToast);
//							text.setText(msg.custom);
							text.setText("新消息，注意查看。");
							Toast toast = new Toast(getApplicationContext());
							toast.setGravity(Gravity.CENTER | Gravity.CENTER, 12, 40);
							toast.setDuration(Toast.LENGTH_LONG);
							toast.setView(layout);
							toast.show();
						} else {
							System.out.println("没有进入");
							Intent intent = new Intent();
							intent.setClass(ImageLoder.this, LoginActvity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
						}
					}
				});
			}
		};
		mPushAgent.setNotificationClickHandler(notificationClickHandler);

		DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				// .showImageOnLoading(R.drawable.img_defalut) //默认图片
				// .showImageForEmptyUri(R.drawable.img_defalut)
				// //url爲空會显示该图片，自己放在drawable里面的
				// .showImageOnFail(R.drawable.img_defalut)// 加载失败显示的图片
				.displayer(new RoundedBitmapDisplayer(360)) // 圆角，不需要请删除
				.build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).memoryCacheExtraOptions(480, 800)
				// 缓存在内存的图片的宽和高度
				.diskCache(new UnlimitedDiscCache(getCacheDir())).diskCacheExtraOptions(480, 800, null)
				// CompressFormat.PNG类型，70质量（0-100）
				.memoryCache(new WeakMemoryCache()).memoryCacheSize(2 * 1024 * 1024) // 缓存到内存的最大数据
				.diskCacheSize(50 * 1024 * 1024) // 缓存到文件的最大数据
				.diskCacheFileCount(1000) // 文件数量
				.defaultDisplayImageOptions(options) // 上面的options对象，一些属性配置
				.build();
		ImageLoader.getInstance().init(config); // 初始化

		stu = getSharedPreferences("", Context.MODE_PRIVATE);
		
		registerActivityLifecycleCallbacks(new MyActivityLifecycleCallbacks());
	}

	public void connect(String token) {
		RongIM.connect(token, new ConnectCallback() {

			@Override
			public void onSuccess(String arg0) {
				// TODO Auto-generated method stub
				System.out.println("connection...OK");
				setOtherListener();
			}

			@Override
			public void onError(ErrorCode arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTokenIncorrect() {
				// TODO Auto-generated method stub

			}
		});
	}

	public void reconnect(String token) {
		connect(token);
	}

	public void logout() {
		if (null != RongIM.getInstance()) {
			RongIM.getInstance().logout();
			RongIM.getInstance().disconnect();
		}
	}

	public static boolean isTopActivy(String packageName, Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		String currentPackageName = cn.getPackageName();

		return (currentPackageName != null && currentPackageName.equals(packageName));
	}

	/*
	 * private void sHowDialog(UMessage msg) { // TODO Auto-generated method
	 * stub Toast.makeText(getApplicationContext(), msg.custom+"showdialog",
	 * 0).show(); AlertDialog.Builder builder = new Builder(mContext);
	 * 
	 * builder.setMessage(msg.custom);
	 * 
	 * builder.setTitle("提示"); builder.setPositiveButton("确认", new
	 * OnClickListener() {
	 * 
	 * @Override public void onClick(DialogInterface dialog, int which) {
	 * 
	 * dialog.dismiss(); }
	 * 
	 * });
	 * 
	 * builder.setNegativeButton("取消", new OnClickListener() {
	 * 
	 * @Override public void onClick(DialogInterface dialog, int which) {
	 * 
	 * dialog.dismiss();
	 * 
	 * }
	 * 
	 * }); builder.create().show(); }
	 */

	@Override
	public UserInfo getUserInfo(String userId) {
		SharedPreferences localUser = getSharedPreferences("userinfo", MODE_PRIVATE);
		String localUserId = localUser.getString("UID", "");
		UserInfo user = null;
		if (userId.equals("xdf0050009193")) {
			String portrait = localUser.getString("IconUrl",
					"http://testielts2.staff.xdf.cn/upload_dev/userImage/1445498421212349.jpg");
			System.out.println("usershit 123123123 = " + userId + ",local =" + localUserId + ",portrait = " + portrait
					+ ",NickName = " + localUser.getString("NickName", ""));
			user = new UserInfo(userId, localUser.getString("NickName", ""),
					"".equals(portrait) ? null : Uri.parse(portrait));
		}
		return user;
	}

	@Override
	public void onChanged(ConnectionStatus connectionStatus) {
		// TODO Auto-generated method stub
		switch (connectionStatus) {

		case CONNECTED:// 连接成功。
			System.out.println("connection...CONNECTED");
			break;
		case DISCONNECTED:// 断开连接。
			System.out.println("connection...DISCONNECTED");
			break;
		case CONNECTING:// 连接中。
			System.out.println("connection...CONNECTING");
			break;
		case NETWORK_UNAVAILABLE:// 网络不可用。
			System.out.println("connection...NETWORK_UNAVAILABLE");
			break;
		case KICKED_OFFLINE_BY_OTHER_CLIENT:// 用户账户在其他设备登录，本机会被踢掉线
			System.out.println("connection...KICKED_OFFLINE_BY_OTHER_CLIENT");
			if (isAppOnForeground()) {
				notifyKickOff();
			} else {
				waitToShowKickDlg = true;
			}
			cancelTrace();
			break;
		default:
			System.out.println("connection...unknown");
			break;
		}
	}

	private void notifyKickOff() {
		SharedPreferences stushare = getSharedPreferences("stushare", MODE_PRIVATE);
		Editor editor = stushare.edit();
		editor.remove("userPass");
		editor.commit();
		Intent intent1 = new Intent();
		intent1.setAction("com.lelts.tool");
		sendBroadcast(intent1);
		
		Intent intent = new Intent(this, KickOffDialog.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	// 展示windowManager(无需上下文承接)
	private void showWindow(String msg) {
		Toast.makeText(getApplicationContext(), msg, 0).show();
		WindowManager mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
		wmParams.type = LayoutParams.TYPE_SYSTEM_ALERT;
		// 设置图片格式，效果为背景透明
		wmParams.format = PixelFormat.RGBA_8888;
		// 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
		wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
		// 调整悬浮窗显示的停靠位置为左侧置顶
		wmParams.gravity = Gravity.CENTER;
		// 以屏幕左上角为原点，设置x、y初始值，相对于gravity
		wmParams.x = 0;
		wmParams.y = 0;
		// 设置悬浮窗口长宽数据
		wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		/*
		 * // 设置悬浮窗口长宽数据 wmParams.width = 200; wmParams.height = 80;
		 */

		LayoutInflater inflater = LayoutInflater.from(this);
		// 获取浮动窗口视图所在布局
		LinearLayout mFloatLayout = (LinearLayout) inflater.inflate(R.layout.window_manager_layout, null);
		// 添加mFloatLayout
		mWindowManager.addView(mFloatLayout, wmParams);
		// 浮动窗口按钮
		TextView tv = (TextView) mFloatLayout.findViewById(R.id.massege);
		tv.setText(msg);
		// Button mFloatView = (Button)mFloatLayout.findViewById(R.id.float_id);
		/*
		 * mFloatView.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub // android.view.WindowManager$BadTokenException: Unable to add
		 * window android.view.ViewRootImpl$W@42509a38 -- permission denied for
		 * this window type
		 * 
		 * } });
		 */
		// return (currentPackageName != null && currentPackageName
		// .equals(packageName));
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
		cancelTrace();
		System.out.println("gansi");
	}

	private void setOtherListener() {
		RongIMClientWrapper.setConnectionStatusListener(this);
	}

	// 网络解析聊天token数据
	public void checkNewestUserInfo(String token) {
		String url = Constants.URL_ActiveClass_getChatToken;
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);
		params.addBodyParameter("checkUpdate", "true");
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				System.out.println("网络解析聊天token数据    ===  onFailure");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String result = arg0.result;
				System.out.println("网络解析聊天token数据    ===  onSuccess : " + result);
				try {
					JSONObject str = new JSONObject(result);
					String Result = str.getString("Result");
					String Infomation = str.getString("Infomation");
					String data = str.getString("Data");
					JSONObject obj_data = new JSONObject(data);
					String chatToken = obj_data.getString("chatToken");
					System.out.println("login的聊天数据 ？？？=" + chatToken);
					SharedPreferences share = getSharedPreferences("userChatToken", MODE_PRIVATE);
					Editor editor = share.edit();
					editor.putString("chatToken", chatToken);
					editor.commit();

					SharedPreferences userInfo = getSharedPreferences("userChatToken", MODE_PRIVATE);
					Editor et_userInfo = userInfo.edit();
					et_userInfo.putString("chatToken", chatToken);
					et_userInfo.commit();
				} catch (JSONException e) {
					e.printStackTrace();
				} finally {
					SharedPreferences share = getSharedPreferences("userinfo", MODE_PRIVATE);
					connect(share.getString("chatToken", ""));
				}
			}
		});
	}
	
	public boolean isAppOnForeground() {

		ActivityManager activityManager = (ActivityManager) getApplicationContext()
				.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = getApplicationContext().getPackageName();

		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}

		return false;
	}
	
	private List<Activity> allActivities = new ArrayList<Activity>();
	public void finishAllDelay() {
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					if (!allActivities.isEmpty()) {
						for (int i = 0; i < allActivities.size(); i++) {
							if (!(allActivities.get(i) instanceof LoginActvity))
							allActivities.get(i).finish();
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}, 500);
		
	}
	
	private boolean waitToShowKickDlg = false;
	class MyActivityLifecycleCallbacks implements ActivityLifecycleCallbacks {

		@Override
		public void onActivityCreated(Activity arg0, Bundle arg1) {
			// TODO Auto-generated method stub
			allActivities.add(arg0);
		}

		@Override
		public void onActivityDestroyed(Activity activity) {
			// TODO Auto-generated method stub
			allActivities.remove(activity);
		}

		@Override
		public void onActivityPaused(Activity activity) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onActivityResumed(Activity activity) {
			// TODO Auto-generated method stub
			if (waitToShowKickDlg && null != RongIM.getInstance() && null != RongIM.getInstance().getRongIMClient()) {
				if (RongIM.getInstance().getRongIMClient().getCurrentConnectionStatus() == ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT) {
					notifyKickOff();
				}
				waitToShowKickDlg = false;
			}
		}

		@Override
		public void onActivitySaveInstanceState(Activity activity,
				Bundle outState) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onActivityStarted(Activity activity) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onActivityStopped(Activity activity) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private Timer tracer;
	private TraceTask task;
	public void traceUser(String token) {
		if (null != tracer) {
			tracer.cancel();
			tracer = null;
		}
		tracer = new Timer();
		if (null != task) {
			task.cancel();
			task = null;
		}
		task = new TraceTask(token);
		tracer.scheduleAtFixedRate(task, 0, 5 * 60 * 1000);
	}
	
	public void cancelTrace() {
		if (null != tracer) {
			tracer.cancel();
			tracer = null;
		}
		if (null != task) {
			task.cancel();
			task = null;
		}
		currentTime= -1;
	}
	
	private long currentTime = -1;
	public long getIntervalTime() {
		return this.currentTime;
	}
	
	class TraceTask extends TimerTask {

		String token;
		public TraceTask(String userToken) {
			this.token = userToken;
		}
		
		@Override
		public void run() {
			String url = Constants.URL_updateLogoffTime;
			RequestParams params = new RequestParams();
			params.addHeader("Authentication", this.token);
			HttpUtils http = new HttpUtils();
			http.configCurrentHttpCacheExpiry(1000 * 10);
			http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					System.out.println("trace sucess");
				}

				@Override
				public void onFailure(HttpException error, String msg) {
					System.out.println("trace onFailure");
				}
			});
			currentTime = System.currentTimeMillis();
			if (null != ImageLoder.this.intervalTracer) {
				ImageLoder.this.intervalTracer.intervalFeedback(currentTime);
			}
		}
		
	}
	
	public interface IntervalTimeTrace {
		
		public void intervalFeedback(long currentInterval);
	}
	
	private IntervalTimeTrace intervalTracer;
	public void registerIntervalTrace(IntervalTimeTrace tracer) {
		this.intervalTracer = tracer;
	}
	
	public void unregisterIntervalTrace() {
		this.intervalTracer = null;
	}
}
