/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015��7��15�� 
 * 
 *******************************************************************************/
package com.lels.student.connectionclass.activity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.Contacts.Data;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.strudentlelts.R;
import com.lels.bean.ExitApplication;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lels.main.activity.StundentClassroom;
import com.lels.student.connectionclass.adapter.ConnectionStartTestAdapter;
import com.lelts.student.db.SqliteDao;
import com.lelts.students.bean.StuAnswerInfo;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

@SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled", "HandlerLeak" })
public class ConnetionStartTestActivity extends Activity implements
		OnClickListener {
	private ListView mlistview;
	private Context context;
	private List<StuAnswerInfo> manswerlist;
	private List<HashMap<String, Object>> mlist, chooselist;
	private ConnectionStartTestAdapter madapter;
	private WebView mwebview;
	private int oldposition;
	private Button btn_next, btn_sure;
	private ImageView img_close;
	private SharedPreferences share, stuinfo;
	private ImageButton img_back;
	private Intent intent;
	private String domainPFolder, SectionID;
	private int QNumberBegin, QNumberEnd, chooseposition = 0;
	private AlertDialog alertDialog;
	private SqliteDao dao ;
	private String str = "",cucurrCode="";
	private MyWebClient mWebClient;
	private long endtime,jointime;
	private int n;
	private String myanswer;
	private StringBuffer answer;
	private String data;
	private Timer end_test_time;
	private String PaperSubmitCountdown;
	private int sumtime ;
	private TextView txt_waring;
	//判断 试卷的类型 type
	private int testtype;
	//资料答题需要的参数
//	private String domainPFolder
	private String PID;
	private String mateId ;
	private String ST_ID;
	private String PaperFolder;
	private String domainPZip;
//	private int testtype;
	//判断是否隐藏试卷的题干
	private int HideContent = -1;
	private int Data;
	//判断时候跳转的
	private boolean flagintent = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connection_starttest);
		initview();
		LodDialogClass.showCustomCircleProgressDialog(context, null,getString(R.string.xlistview_header_hint_loading));
		gettesttype();
		ExitApplication.getInstance().addActivity(this);
	}
	
	/**
	 * 方法说明：获取 试卷的来源  1.表示老师推题，2.表示资料答题
	 *
	 */
	private void gettesttype() {
		// TODO Auto-generated method stub
			testtype = stuinfo.getInt("testtype", -1);
			Editor ed = stuinfo.edit();
			switch (testtype) {
			//老师推题
			case 1:
				
				System.out.println("======试卷类型====老师推题===flagintent===="+flagintent);
				if (flagintent==true) {
					getjointime();
					ed.putInt("jointime", (int)jointime);
					ed.commit();
				}else{
					jointime = stuinfo.getInt("jointime", 0);
					
				}
			
				getPaperQuestionList();
				ed.putInt("testtype", 1);
				ed.commit();
				timer();
				break;
			//资料答题
			case 2:
				System.out.println("======试卷类型====资料答题");
				getjointime();
				ed.putInt("testtype", 2);
				ed.putInt("jointime", (int)jointime);
				ed.commit();
				getPaperQuestionListformdata();
				
				break;
				//听力答题
				case 3:
					System.out.println("======试卷类型====资料答题");
					getjointime();
					ed.putInt("testtype", 2);
					ed.putInt("jointime", (int)jointime);
					ed.commit();
					getPaperQuestionListformdata();
					
					break;

			default:
				break;
			}
		
	}

	/**
	 * 初始化数据
	 *
	 */
	private void initview() {
		// TODO Auto-generated method stub
		context = this;
		intent = new Intent();
		mlistview = (ListView) findViewById(R.id.mlistview_connection_starttest);
		mlist = new ArrayList<HashMap<String, Object>>();
		chooselist = new ArrayList<HashMap<String, Object>>();
		mwebview = (WebView) findViewById(R.id.webview_connection_starttest);
		btn_next = (Button) findViewById(R.id.btn_next_conn_starttest);
		btn_next.setOnClickListener(this);
		share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		stuinfo = getSharedPreferences("stuinfo", Context.MODE_PRIVATE);
		img_back = (ImageButton) findViewById(R.id.img_back_ClassRoom_conncetion_strattest);
		img_back.setOnClickListener(this);
		Intent getintent = getIntent();
		if(getintent!= null){
			chooseposition = getintent.getIntExtra("chooseposition", 0);
			flagintent = getintent.getBooleanExtra("flagintent", true);
		}else{
			chooseposition = 0;
		}
	
		System.out.println("返回的选择的第几题号。。。。。chooseposition==="+chooseposition);
		PaperSubmitCountdown = stuinfo.getString("PaperSubmitCountdown", "");
		dao = new SqliteDao();
	
	}
	/**
	 * 加载webview
	 *
	 */
	private void initmwebview(String url) {

		// TODO Auto-generated method stub
		// WebView是否能加载 JavaScript
		mwebview.getSettings().setJavaScriptEnabled(true);
		// 加载，调用方法
		mwebview.addJavascriptInterface(new WebTOANDROIDInterface(), "android");
		// 设置 缓存模式
		mwebview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		// 设置加载进来的页面自适应手机屏幕
//		mwebview.getSettings().setLayoutAlgorithm(
//				WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//		// 开启 DOM storage API 功能
//		mwebview.getSettings().setDomStorageEnabled(true);
//		// 开启 database storage API 功能
//		mwebview.getSettings().setDatabaseEnabled(true);
//		// 优先使用缓存
//		mwebview.getSettings()
//				.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		mWebClient = new MyWebClient();
		mwebview.loadUrl(url);
		// 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
		mwebview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				view.loadUrl(url);
				return true;
			}
	
		});
		mwebview.setWebViewClient(mWebClient);
		 //在JS中调用本地java方法
		mwebview.addJavascriptInterface(new WebTOANDROIDInterface(), "android");
	}

	/**
	 * 网络请求 老师发送的试卷题
	 *
	 */
	private void getPaperQuestionList() {
		// TODO Auto-generated method stub
		System.out.println("activeClassPaperInfoId======="
				+ stuinfo.getString("activeClassPaperInfoId", "")
				+ "======paperId=========" + stuinfo.getString("paperId", ""));
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		final HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(0);
		utils.configDefaultHttpCacheExpiry(0);
		utils.send(
				HttpMethod.GET,
				Constants.URL_ActiveClassPaperQuestionNumberTodoList
						+ "?activeClassPaperInfoId="
						+ stuinfo.getString("activeClassPaperInfoId", "")
						+ "&paperId=" + stuinfo.getString("paperId", ""),
				params, new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						String result = arg0.result;
						System.out.println("testlist_result==================="
								+ result);
						try {
							JSONObject ob = new JSONObject(result);
							JSONObject obj = ob.getJSONObject("Data");
							JSONObject obj1 = obj.getJSONObject("paperInfo");
							domainPFolder = obj1.getString("domainPFolder");
							PaperFolder = obj1.getString("PaperFolder");
							domainPZip = obj1.getString("domainPZip");
							JSONArray array = obj
									.getJSONArray("selectedQuestions");
							for (int i = 0; i < array.length(); i++) {
								JSONObject selectedQuestions = array
										.getJSONObject(i);
								HashMap<String, Object> choosemap = new HashMap<String, Object>();
								choosemap.put("id",
										selectedQuestions.getString("id"));
								choosemap
										.put("relactiveclasspaperinfoId",
												selectedQuestions
														.getString("relactiveclasspaperinfoId"));
								choosemap.put("paperid",
										selectedQuestions.getString("paperid"));
								choosemap.put("questionid", selectedQuestions
										.getString("questionid"));
								choosemap.put("psid",
										selectedQuestions.getString("psid"));
								choosemap.put("qpid",
										selectedQuestions.getString("qpid"));
								chooselist.add(choosemap);
							}
							System.out.println("domainPFolder==============>"
									+ domainPFolder);
							System.out.println("chooselist=================>"
									+ chooselist);
							// domainPFolder+ "/config_phone.json"
							
							//下载zip文件
							downlode(domainPZip);
//							
//							utils.send(HttpMethod.GET, domainPFolder
//									+ "/config_phone.json",
//
//							new RequestCallBack<String>() {
//								@Override
//								public void onFailure(HttpException arg0,
//										String arg1) {
//
//								}
//
//								@Override
//								public void onSuccess(ResponseInfo<String> arg0) {
//									// TODO Auto-generated method stub
//									String testresutl = arg0.result;
//									System.out.println("testresutl==========>"
//											+ testresutl);
//									try {
//										JSONObject ob = new JSONObject(
//												testresutl);
//										JSONArray array = ob
//												.getJSONArray("PaperCatagoryList");
//										for (int i = 0; i < array.length(); i++) {
//											JSONObject PaperCatagoryList = array
//													.getJSONObject(i);
//											JSONArray array2 = PaperCatagoryList
//													.getJSONArray("paperSectionList");
//											for (int j = 0; j < array2.length(); j++) {
//												JSONObject paperSectionList = array2
//														.getJSONObject(j);
//												SectionID = paperSectionList.getString("SectionID");
//												System.out.println("SectionID=================="+ SectionID);
//												for (int m = 0; m < chooselist.size(); m++) {
//													if(chooselist.get(m).get("psid")!=null){
//													if (SectionID.equals(chooselist.get(m).get("psid"))) {
//														System.out.println("section+++++++++++"+ "========chooselistSectionid========="+ chooselist.get(m).get("psid"));
//														JSONArray array3 = paperSectionList.getJSONArray("QuestionPageList");
//														System.out.println("array3=================="+ array3);
//														for (int k = 0; k < array3.length(); k++) {
//															JSONObject QuestionPageList = array3.getJSONObject(k);
////															QNumberBegin = QuestionPageList.getInt("QNumberBegin");
////															QNumberEnd = QuestionPageList.getInt("QNumberEnd");
//															PID = QuestionPageList.getString("PID");
//															JSONArray array4 = QuestionPageList.getJSONArray("FileName");
//															System.out.println("array4==========="+ array4);
//												//			if (QNumberEnd > QNumberBegin) {
//																for (int l = 0; l < array4.length(); l++) {
//																	HashMap<String, Object> map = new HashMap<String, Object>();
//																	JSONObject  FileName_obj = array4.getJSONObject(l);
//																	String FileName = FileName_obj.getString("FileName");
//																	String QNumberBegin = FileName_obj.getString("QNumberBegin");
//																	String QNumberEnd = FileName_obj.getString("QNumberEnd");
//																	int QnumBegin = Integer.parseInt(QNumberBegin);
//																	int QnumEnd = Integer.parseInt(QNumberEnd);
//																	//判断题号的  填空题多道题的则要单独判断
//																	if((QnumEnd-QnumBegin)>0){
//																		if((QnumEnd-QnumBegin)==1){
//																			map.put("num","第"+ QnumBegin+"题" );
//																		}else{
//																			map.put("num","第"+QnumBegin +"-"+(QnumEnd-1)+"题");
//																		}
//																		// domainPFolder+http://testielts2.staff.xdf.cn/upload_dev/paperzip/TempPaper_10768/
//																		map.put("webview",domainPFolder+"/"+FileName);
//																		map.put("_id", SectionID+"_"+PID+"_"+l);
//																		System.out
//																				.println("map的值====="+map.get("_id")+"-------"+map.get("webview"));
//																		mlist.add(map);
//																		
//																		//判断是否 是最后一道题
//																		if (chooseposition == mlist.size()-1) {
//																			btn_next.setText("完成");
//																		}else{
//																			btn_next.setText("下一题");
//																			
//																		}
//																		
//																		//判断数据库中是否 有此题，没有则添加 
//																		System.out.println("map里面的s_code==="+map.get("_id"));
//																		if(map.get("_id")!=null){
//																			System.out
//																					.println("插入信息===="+map.get("_id").toString());
//																			if(dao.SelectBys_id(context,map.get("_id").toString()) == false){
//																				
//																				dao.InsertInfoAnswer(context, map.get("_id").toString(), map.get("num").toString(),"Q2347_4461||1");
//																				
//																			}
//																			
//																		}
//																	}
//											
//																	System.out.println("map里面的s_code==="+map.get("_id"));
//						
//																}
//														}
//
//													}
//													}
//												}
//											//	dao.InsertInfoAnswer(context, mlist.get(0).get("num").toString(), "Q2347_4461|B|1");
//												oldposition = chooseposition;   
//												
//												System.out
//														.println("oldposition 的值是多少===="+oldposition);
//												madapter = new ConnectionStartTestAdapter(mlist, context);
//									
//												madapter.setSelectedPosition(oldposition);
//												mlistview.setAdapter(madapter);
//										
//												mlistview.setOnItemClickListener(new OnItemClickListener() {
//															@Override
//															public void onItemClick(AdapterView<?> parent,View view,int position,long id) {
//															
//																
//																mwebview.loadUrl("javascript:getAnswers()");
//																mwebview.loadUrl("javascript:getCurrentQSCode()");
//																try {
//																	Thread.sleep(100);
//																} catch (InterruptedException e) {
//																	// TODO Auto-generated catch block
//																	e.printStackTrace();
//																} 
//																System.out.println("updata====>"+mlist.get(oldposition).get("_id").toString());
//																//更改 试卷的答案 根据S_id
//																dao.UpdateInfoAnswer(context,mlist.get(oldposition).get("_id").toString(),str);
//
////																mwebview.loadUrl("javascript:getAnswers()");
////																mwebview.loadUrl("javascript:getCurrentQSCode()");
////																
////																try {
////																	Thread.sleep(100);
////																} catch (InterruptedException e) {
////																	// TODO Auto-generated catch block
////																	e.printStackTrace();
////																}
////																
////																dao.UpdateInfoAnswer(context, mlist.get(position).get("num").toString(),str,cucurrCode);
//																oldposition = position;
//																
//																if (oldposition >= mlist.size() - 1) {
//																	btn_next.setText("完成");
//																} else {
//																	btn_next.setText("下一题");
//																}
//																initmwebview(mlist.get(position).get("webview").toString());
//																madapter.setSelectedPosition(position);
//																madapter.notifyDataSetInvalidated();
//															}
//														});
//
//											}
//											initmwebview(mlist.get(oldposition).get("webview").toString());
//											LodDialogClass.closeCustomCircleProgressDialog();
//											System.out.println("chooseposition=============="+ chooseposition);
//											System.out.println("mlist===================="+ mlist);
//										
//										}
//									
//									} catch (JSONException e) {
//										// TODO Auto-generated catch
//										// block
//										e.printStackTrace();
//									} 
//									
//								}
//
//							});

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});

	}

	public class WebTOANDROIDInterface {
		
		@JavascriptInterface
		public void getAnswers(String line) {
			str = line;

			System.out.println("str=====>" +str);
		}
		@JavascriptInterface
		public void getCurrentQSCode(String Code){
			cucurrCode = Code;
			System.out.println("currCode======>"+cucurrCode);
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
		//下一题
		case R.id.btn_next_conn_starttest:
			mwebview.loadUrl("javascript:getAnswers()");
			mwebview.loadUrl("javascript:getCurrentQSCode()");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			System.out.println("oldposition 的值是===="+oldposition);
			System.out.println("updata====>"+mlist.get(oldposition).get("_id").toString());
			//根据s_id来 更改试卷的答案
			dao.UpdateInfoAnswer(context,mlist.get(oldposition).get("_id").toString(),str);

			oldposition++;
			if (oldposition == mlist.size() - 1) {
				btn_next.setText("完成");
				madapter.setSelectedPosition(oldposition);
				madapter.notifyDataSetInvalidated();
				initmwebview(mlist.get(oldposition).get("webview").toString());
			} else if (oldposition < mlist.size()) {
				madapter.setSelectedPosition(oldposition);
				madapter.notifyDataSetInvalidated();
				initmwebview(mlist.get(oldposition).get("webview").toString());
				btn_next.setText("下一题");
			} else if (oldposition == mlist.size()) {
				btn_next.setText("完成");
				
				
				intent.setClass(ConnetionStartTestActivity.this,
						ConnectionSaveAnswerActivity.class);
				
				intent.putExtra("jointime", jointime);
				startActivity(intent);
//				startActivityForResult(intent, INT_CODE);
				finish();
			} else {
				oldposition = mlist.size();
			}
			break;
			// 返回上一级
		case R.id.img_back_ClassRoom_conncetion_strattest:
//			intent.setClass(ConnetionStartTestActivity.this,
//					StundentClassroom.class);
//			startActivity(intent);
			dao.deleteDatabase(context);
			if (testtype==1) {
				intent.setClass(ConnetionStartTestActivity.this,
				StundentClassroom.class);
		startActivity(intent);
		finish();
			}else{
				
				finish();
			}

			break;
		// 下课 DiagLog 确定按钮 ,DiaLog消失
		case R.id.waring_end_classroom_btn_sure:
			System.out.println("jotime======>"+jointime);
//			intent.setClass(ConnetionStartTestActivity.this,
//					ConnectionSaveAnswerActivity.class);
			getendtime();
			createExamInfoId();

//			startActivity(intent);
			alertDialog.dismiss();
			break;
		// DiagLog 取消按钮 ，DiaLog消失
		case R.id.waing_end_classroom_btn_close:
		
			
			alertDialog.dismiss();
			break;
		default:
			break;
		}

	}

	/**
	 * 方法说明：弹出Dialog
	 *
	 */
	private void ShowDiglog() {
		// TODO Auto-generated method stub
		LayoutInflater layoutInflater = LayoutInflater.from(ConnetionStartTestActivity.this);
		View myLoginView = layoutInflater.inflate(
				R.layout.waring_end_classroom, null);
		if(alertDialog == null){
			alertDialog = new AlertDialog.Builder(ConnetionStartTestActivity.this).create();
			alertDialog.setView(myLoginView, 0, 0, 0, 30);
			alertDialog.getWindow().setGravity(Gravity.CENTER);
			alertDialog.setCanceledOnTouchOutside(false);
			alertDialog.setCancelable(false);
			alertDialog.show();
		}
		
		txt_waring = (TextView) myLoginView.findViewById(R.id.waring_end_classroom_txt_testname);
		
		if (Data == 3) {
			txt_waring.setText("已达到百分比，请提交试卷！");
		}
		
		btn_sure = (Button) myLoginView
				.findViewById(R.id.waring_end_classroom_btn_sure);
	//	img_close = (ImageButton) myLoginView
//				.findViewById(R.id.waing_end_classroom_btn_close);
		btn_sure.setOnClickListener(this);
	//	img_close.setOnClickListener(this);
	}
	private class MyWebClient extends WebViewClient {
		
		private String urlLoad;
		
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
		    //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
            view.loadUrl(url);
            urlLoad = url;
			return true;
		}

//		@Override
//		public void onPageStarted(WebView view, String url, Bitmap favicon) {
//			
//			
//			System.out.println("onPageStarted============>");
//			super.onPageStarted(view, url, favicon);
//			
//		}
		/* (non-Javadoc)
		 * @see android.webkit.WebViewClient#onPageStarted(android.webkit.WebView, java.lang.String, android.graphics.Bitmap)
		 */
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
			LodDialogClass.showCustomCircleProgressDialog(context, "", "加载试卷...");
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			System.out.println("onPageFinished============>");
			//HideContent   1是隐藏题干
			System.out.println("testtype====的类型是===="+testtype);
			if (testtype==1) {
				HideContent = 1;
			}else{
				HideContent = 0;
			}
			
			System.out.println("HideContent =-======="+HideContent );
			mwebview.loadUrl("javascript:setHideContent(\'" + HideContent + "\');");
			//根据 s_code  来获取试卷的 答案
			data = dao.setAnswer(context, mlist.get(oldposition).get("_id").toString());
			System.out.println("data======>"+data);
			
			mwebview.loadUrl("javascript:setMyAnswers(\'" + data + "\')");
			LodDialogClass.closeCustomCircleProgressDialog();
			
			super.onPageFinished(view, url);
		}
	}
	/**
	 * 方法说明：获取进入的答题时间
	 *
	 */
	private void getjointime() {
		// TODO Auto-generated method stub
		SimpleDateFormat    formatter    =   new    SimpleDateFormat    ("HH:mm:ss");     
		Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间     
		String    strr    =    formatter.format(curDate);
		long Hours = curDate.getHours();
		long Seconds = curDate.getSeconds();
		long Minutes = curDate.getMinutes();
		jointime= Hours*3600+Minutes*60+Seconds;
		System.out.println("进入的时间Join==========="+strr);
		System.out.println("curDate.getTime();"+curDate.getTime());
		System.out.println("	curDate.getSeconds();"+	curDate.getSeconds());
		System.out.println("	curDate.getHours();"+	curDate.getHours());
		System.out.println("	curDate.getMinutes();"+	curDate.getMinutes());
	}
	/**
	 * 方法说明：结束时间
	 *
	 */
	private void getendtime() {
		// TODO Auto-generated method stub
		SimpleDateFormat    formatter    =   new    SimpleDateFormat    ("HH:mm:ss");     
		Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间     
		String    str    =    formatter.format(curDate);
		long Hours = curDate.getHours();
		long Seconds = curDate.getSeconds();
		long Minutes = curDate.getMinutes();
		endtime= Hours*3600+Minutes*60+Seconds;
		System.out.println("Join==========="+str);
		System.out.println("curDate.getTime();"+curDate.getTime());
		System.out.println("	curDate.getSeconds();"+	curDate.getSeconds());
		System.out.println("	curDate.getHours();"+	curDate.getHours());
		System.out.println("	curDate.getMinutes();"+	curDate.getMinutes());
		n = (int) ((endtime)-(jointime));
		System.out.println("时间差====="+n);
	}
	/**
	 * 方法说明：心跳 获取教师结束当前练习的指令，如状态为结束，学生端强制提交当前随堂练习试卷的作答
	 * Data = 当前随堂练习试卷的推送状态 0未推送 1已推送 2已完成(教师点了结束);3强制提交(提交人数到了百分比);
	 *
	 */
	private void ActiveClassExerciseStatus() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
	    HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(0);
		utils.configDefaultHttpCacheExpiry(0);
		utils.send(
				HttpMethod.GET,
				Constants.URL_ActiveClassExerciseStatus
						+ "?ccId="
						+ stuinfo.getString("ccId", "")
						+ "&paperId=" + stuinfo.getString("paperId", ""),
				params, new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						String result = arg0.result;
						try {
							JSONObject obj = new JSONObject(result);
							Data = obj.getInt("Data");
							System.out.println("data...."+Data);
							if(Data ==2 ){
								ShowDiglog();
								//end_test_time.cancel();
							}else if (Data == 3) {
								//强制提交(人数已经到了百分比)
								ShowDiglog();
							}
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						System.out.println("ActiveClassExerciseStatus_result==================="
								+ result);
						
					}
				});
	}
	
	/**
	 * 方法说明：提交试卷时，先生成examInfoId
	 *
	 */
	private void createExamInfoId() {
		// TODO Auto-generated method stub
		manswerlist = new ArrayList<StuAnswerInfo>();
		manswerlist = dao.GetallAnswer(context);
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		final HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, Constants.URL_createExamInfoId
				+ "?paperId=" + stuinfo.getString("paperId", "")+"&TaskType=2"+"&costTime="+(n+""), params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				String result = arg0.result;
				try {
					JSONObject obj = new JSONObject(result);
					data = obj.getString("Data");
					
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < manswerlist.size(); i++) {
						answer = sb.append(manswerlist.get(i).getAnswer()+"`");
					}
					myanswer = answer.deleteCharAt(sb.length()-1).toString();
					System.out.println("paperId====="+stuinfo.getString("paperId", "")+"========examInfoId======"+data+"====answer=========="+myanswer+"===ccId==="+stuinfo.getString("ccId", ""));
//					提交试卷(判卷)
//					[Args]：
//					 paperId=[考试试卷的主键ID]&
//					 examInfoId=[examInfoId]&
//					 studentAnswers=[学生答案]&
//					 ccId=[课次ID]
					RequestParams params = new RequestParams();
					params.addBodyParameter("paperId",stuinfo.getString("paperId", ""));
					params.addBodyParameter("examInfoId",data);
					params.addBodyParameter("studentAnswers",myanswer);
					params.addBodyParameter("ccId",stuinfo.getString("ccId", ""));
					params.addHeader("Authentication", share.getString("Token", ""));
					utils.send(HttpMethod.POST, Constants.URL_EvaluateTheActiveClassExercisePaper, params, new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							// TODO Auto-generated method stub
						}

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							// TODO Auto-generated method stub
							String result = arg0.result;
							System.out.println("test_result===================" + result);
//							intent.setClass(ConnectionSaveAnswerActivity.this,ConnectionReportActivity.class);
//							startActivity(intent);
							intent.putExtra("examInfoId", data);
							intent.setClass(ConnetionStartTestActivity.this,ConnectionReportActivity.class);
							startActivity(intent);
							System.out.println("data===============+++++"+data);
						}
					});
					
				} catch (JSONException e) {
					// TODO Auto-genuerated catch block
					e.printStackTrace();
				}
				
				System.out.println("example_result===================" + result);
			}
		});

	}
	
	/**
	 * 方法说明：时间到了强制提交
	 *
	 */
	private void timeisover() {
		// TODO Auto-generated method stub
		
		Message msg = new Message();
		handler.sendMessage(msg);
		
	}
	
	Handler handler  = new Handler(){
		public void handleMessage(android.os.Message msg) {
			sumtime  =Integer.parseInt(PaperSubmitCountdown)*60;
			//	sumtime
			if(n>sumtime){
				ShowDiglog();
				txt_waring.setText("答题时间已到，请提交试卷 !  ");
				if(alertDialog.isShowing()){
					end_test_time.cancel();
				}
			}
		};
	};

	
	/**
	 * 方法说明：每3秒  获取教师结束当前练习的指令
	 *
	 */
	private void timer() {
		end_test_time = new Timer();
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ActiveClassExerciseStatus();
				getendtime();
				timeisover();
			}
		};
		end_test_time.schedule(task, 3000, 3000);
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 * 页面销毁
	 * 
	 */
	
	/**
	 * 方法说明：网络解析 资料答题的试卷
	 *
	 */
	private void getPaperQuestionListformdata() {
		// TODO Auto-generated method stub
		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(0);
		utils.configDefaultHttpCacheExpiry(0);
						// domainPFolder+ "/config_phone.json"
		System.out.println("资料的domainPFolder==="+stuinfo.getString("domainPFolder", ""));
							utils.send(HttpMethod.GET, stuinfo.getString("domainPFolder", "")
									+ "/config_phone.json",
							new RequestCallBack<String>() {
								@Override
								public void onFailure(HttpException arg0,
										String arg1) {

								}

								@Override
								public void onSuccess(ResponseInfo<String> arg0) {
									// TODO Auto-generated method stub
									String testresutl = arg0.result;
									System.out.println("testresutl==========>"
											+ testresutl);
									try {
										JSONObject ob = new JSONObject(
												testresutl);
										JSONArray array = ob
												.getJSONArray("PaperCatagoryList");
										for (int i = 0; i < array.length(); i++) {
											JSONObject PaperCatagoryList = array
													.getJSONObject(i);
											JSONArray array2 = PaperCatagoryList
													.getJSONArray("paperSectionList");
											for (int j = 0; j < array2.length(); j++) {
												JSONObject paperSectionList = array2.getJSONObject(j);
												String SectionID = paperSectionList.getString("SectionID");
														JSONArray array3 = paperSectionList.getJSONArray("QuestionPageList");
														System.out.println("array3=================="+ array3);
														for (int k = 0; k < array3.length(); k++) {
															JSONObject QuestionPageList = array3.getJSONObject(k);
//															QNumberBegin = QuestionPageList.getInt("QNumberBegin");
//															QNumberEnd = QuestionPageList.getInt("QNumberEnd");
															String PID = QuestionPageList.getString("PID");
															JSONArray array4 = QuestionPageList.getJSONArray("FileName");
															System.out.println("array4==========="+ array4);
																for (int l = 0; l < array4.length(); l++) {
																	HashMap<String, Object> map = new HashMap<String, Object>();
																	JSONObject  FileName_obj = array4.getJSONObject(l);
																	String FileName = FileName_obj.getString("FileName");
																	String QNumberBegin = FileName_obj.getString("QNumberBegin");
																	String QNumberEnd = FileName_obj.getString("QNumberEnd");
																	int QnumBegin = Integer.parseInt(QNumberBegin);
																	int QnumEnd = Integer.parseInt(QNumberEnd);
																	//判断题号的  填空题多道题的则要单独判断
																	if((QnumEnd-QnumBegin)>0){
																	if((QnumEnd-QnumBegin)==1){
																		map.put("num", QnumBegin );
																	}else{
																		map.put("num",QnumBegin +"~"+(QnumEnd-1));
																	}
																	
																	map.put("_id", SectionID+"_"+PID+"_"+l);
																	// domainPFolder+http://testielts2.staff.xdf.cn/upload_dev/paperzip/TempPaper_10768/
																	map.put("webview",stuinfo.getString("domainPFolder", "")+"/"+FileName);
																	mlist.add(map);
																	
																	//判断是否 是最后一道题
																	if (chooseposition == mlist.size()-1) {
																		btn_next.setText("完成");
																	}else{
																		btn_next.setText("下一题");
																		
																	}
																	//判断数据库中是否 有此题，没有则添加 
																	System.out.println("map里面的s_code==="+map.get("_id"));
																		if(dao.SelectBys_id(context,map.get("_id").toString()) == false){
																	
																			dao.InsertInfoAnswer(context,map.get("_id").toString(),map.get("num").toString(), str);
																	}
																	
																	}
																	
																}

														}

												oldposition = chooseposition;   
												madapter = new ConnectionStartTestAdapter(mlist, context);
												madapter.setSelectedPosition(oldposition);
												mlistview.setAdapter(madapter);
												//关闭加载dialog
										
												
												mlistview.setOnItemClickListener(new OnItemClickListener() {
															@Override
															public void onItemClick(AdapterView<?> parent,View view,int position,long id) {
																mwebview.loadUrl("javascript:getAnswers()");
																mwebview.loadUrl("javascript:getCurrentQSCode()");
																try {
																	Thread.sleep(100);
																} catch (InterruptedException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																} 
																System.out.println("updata====>"+mlist.get(oldposition).get("_id").toString());
																//更改 试卷的答案 根据S_id
																dao.UpdateInfoAnswer(context,mlist.get(oldposition).get("_id").toString(),str);

//																mwebview.loadUrl("javascript:getAnswers()");
//																mwebview.loadUrl("javascript:getCurrentQSCode()");
//																
//																try {
//																	Thread.sleep(100);
//																} catch (InterruptedException e) {
//																	// TODO Auto-generated catch block
//																	e.printStackTrace();
//																}
//																
//																dao.UpdateInfoAnswer(context, mlist.get(position).get("num").toString(),str,cucurrCode);
																oldposition = position;
																
																if (oldposition >= mlist.size() - 1) {
																	btn_next.setText("完成");
																} else {
																	btn_next.setText("下一题");
																}
																initmwebview(mlist.get(position).get("webview").toString());
																madapter.setSelectedPosition(position);
																madapter.notifyDataSetInvalidated();
															}
														});

											}
											initmwebview(mlist.get(oldposition).get("webview").toString());
											LodDialogClass.closeCustomCircleProgressDialog();
											System.out.println("chooseposition=============="+ chooseposition);
											System.out.println("mlist===================="+ mlist);
										}
								
									} catch (JSONException e) {
										// TODO Auto-generated catch
										// block
										e.printStackTrace();
									} 
									
								}

				});
	}
	
	
	/**
	 * 网络下载
	 * 
	 * 
	 */
	private void downlode(String path) {
		// TODO Auto-generated method stub
		
		  HttpUtils http = new HttpUtils();
		  HttpHandler handler = http.download(path,"/sdcard/httpcomponents-client-4.2.5-src.zip",
		      true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
		      true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
		      new RequestCallBack<File>() {
			 
			  

				/* (non-Javadoc)
				 * @see com.lidroid.xutils.http.callback.RequestCallBack#onStart()
				 */
				@Override
				public void onStart() {
					// TODO Auto-generated method stub
					super.onStart();
					LodDialogClass.showCustomCircleProgressDialog(context, null,"正在下载试卷...");
				}

				@Override
				public void onFailure(HttpException arg0, String arg1) {
					// TODO Auto-generated method stub
					System.out.println("下载失败！");
					LodDialogClass.closeCustomCircleProgressDialog();
					HttpUtils utils = new HttpUtils();
					utils.send(HttpMethod.GET, domainPFolder + "/config_phone.json",new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							// TODO Auto-generated method stub
								parsejson(arg0.result);
							
						}
						});
					
				}

				@Override
				public void onSuccess(ResponseInfo<File> arg0) {
					// TODO Auto-generated method stub
					LodDialogClass.closeCustomCircleProgressDialog();
					System.out.println("下载成功！"+"ResponseInfo<File>======"+arg0.result.getPath());
					try {
							upZipFile(
									new File(
											"/sdcard/httpcomponents-client-4.2.5-src.zip"),
									"/sdcard/folderPath");
						String json = getStringFromJsonFile(ConnetionStartTestActivity.this, "/sdcard/folderPath/"+PaperFolder+"/config_phone.json");
						parsejson(json);
					System.out.println("====="+getStringFromJsonFile(ConnetionStartTestActivity.this, "/sdcard/folderPath/"+PaperFolder+"/config_phone.json"));
					} catch (ZipException e) {;	
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		     
		  });

	}
	/**
	    * 解压缩功能.
	    * 将zipFile文件解压到folderPath目录下.
	    * @throws Exception
	*/
	    public int upZipFile(File zipFile, String folderPath)throws ZipException,IOException {
	    //public static void upZipFile() throws Exception{
	        ZipFile zfile=new ZipFile(zipFile);
	        Enumeration zList=zfile.entries();
	        ZipEntry ze=null;
	        byte[] buf=new byte[1024];
	        while(zList.hasMoreElements()){
	            ze=(ZipEntry)zList.nextElement();    
	            if(ze.isDirectory()){
	                Log.d("upZipFile", "ze.getName() = "+ze.getName());
	                String dirstr = folderPath + ze.getName();
	                //dirstr.trim();
	                dirstr = new String(dirstr.getBytes("8859_1"), "GB2312");
	                Log.d("upZipFile", "str = "+dirstr);
	                File f=new File(dirstr);
	                f.mkdir();
	                continue;
	            }
	            Log.d("upZipFile", "ze.getName() = "+ze.getName());
	            OutputStream os=new BufferedOutputStream(new FileOutputStream(getRealFileName(folderPath, ze.getName())));
	            InputStream is=new BufferedInputStream(zfile.getInputStream(ze));
	            int readLen=0;
	            while ((readLen=is.read(buf, 0, 1024))!=-1) {
	                os.write(buf, 0, readLen);
	            }
	            is.close();
	            os.close();    
	        }
	        zfile.close();
	        Log.d("upZipFile", "finishssssssssssssssssssss");
	        return 0;
	    }

	    /**
	    * 给定根目录，返回一个相对路径所对应的实际文件名.
	    * @param baseDir 指定根目录
	    * @param absFileName 相对路径名，来自于ZipEntry中的name
	    * @return java.io.File 实际的文件
	*/
	    public static File getRealFileName(String baseDir, String absFileName){
	        String[] dirs=absFileName.split("/");
	        File ret=new File(baseDir);
	        String substr = null;
	        if(dirs.length>1){
	            for (int i = 0; i < dirs.length-1;i++) {
	                substr = dirs[i];
	                try {
	                    //substr.trim();
	                    substr = new String(substr.getBytes("8859_1"), "GB2312");
	                    
	                } catch (UnsupportedEncodingException e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }
	                ret=new File(ret, substr);
	                
	            }
	            Log.d("upZipFile", "1ret = "+ret);
	            if(!ret.exists())
	                ret.mkdirs();
	            substr = dirs[dirs.length-1];
	            try {
	                //substr.trim();
	                substr = new String(substr.getBytes("8859_1"), "GB2312");
	                Log.d("upZipFile", "substr = "+substr);
	            } catch (UnsupportedEncodingException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	            
	            ret=new File(ret, substr);
	            Log.d("upZipFile", "2ret = "+ret);
	            return ret;
	        }
	        return ret;
	    }
	    
	    public static String getStringFromJsonFile(Context cxt, String fileName) {
			String result = "";
			try {
//				InputStream is = cxt.openFileInput(fileName);
				FileInputStream fis = new FileInputStream(fileName);
				InputStreamReader isr = new InputStreamReader(fis);
				BufferedReader br = new BufferedReader(isr);
				String line = "";
				while ((line = br.readLine()) != null) {
					result += line;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(fileName + " result123 = >" + result);
			return result;
		}
	    
	    private void parsejson(String json) {
	    	
//			String testresutl = arg0.result;
//			System.out.println("testresutl==========>"
//					+ testresutl);
			try {
				JSONObject ob = new JSONObject(json);
				JSONArray array = ob
						.getJSONArray("PaperCatagoryList");
				for (int i = 0; i < array.length(); i++) {
					JSONObject PaperCatagoryList = array
							.getJSONObject(i);
					JSONArray array2 = PaperCatagoryList
							.getJSONArray("paperSectionList");
					for (int j = 0; j < array2.length(); j++) {
						JSONObject paperSectionList = array2
								.getJSONObject(j);
						SectionID = paperSectionList.getString("SectionID");
						System.out.println("SectionID=================="+ SectionID);
						for (int m = 0; m < chooselist.size(); m++) {
							if(chooselist.get(m).get("psid")!=null){
							if (SectionID.equals(chooselist.get(m).get("psid"))) {
								System.out.println("section+++++++++++"+ "========chooselistSectionid========="+ chooselist.get(m).get("psid"));
								JSONArray array3 = paperSectionList.getJSONArray("QuestionPageList");
								System.out.println("array3=================="+ array3);
								for (int k = 0; k < array3.length(); k++) {
									JSONObject QuestionPageList = array3.getJSONObject(k);
//									QNumberBegin = QuestionPageList.getInt("QNumberBegin");
//									QNumberEnd = QuestionPageList.getInt("QNumberEnd");
									PID = QuestionPageList.getString("PID");
									JSONArray array4 = QuestionPageList.getJSONArray("FileName");
									System.out.println("array4==========="+ array4);
						//			if (QNumberEnd > QNumberBegin) {
										for (int l = 0; l < array4.length(); l++) {
											HashMap<String, Object> map = new HashMap<String, Object>();
											JSONObject  FileName_obj = array4.getJSONObject(l);
											String FileName = FileName_obj.getString("FileName");
											String QNumberBegin = FileName_obj.getString("QNumberBegin");
											String QNumberEnd = FileName_obj.getString("QNumberEnd");
											int QnumBegin = Integer.parseInt(QNumberBegin);
											int QnumEnd = Integer.parseInt(QNumberEnd);
											//判断题号的  填空题多道题的则要单独判断
											if((QnumEnd-QnumBegin)>0){
												if((QnumEnd-QnumBegin)==1){
													map.put("num", QnumBegin );
												}else{
													map.put("num",QnumBegin +"~"+(QnumEnd-1));
												}
												// domainPFolder+http://testielts2.staff.xdf.cn/upload_dev/paperzip/TempPaper_10768/
												map.put("webview",domainPFolder+"/"+FileName);
												map.put("_id", SectionID+"_"+PID+"_"+l);
												System.out
														.println("map的值====="+map.get("_id")+"-------"+map.get("webview"));
												mlist.add(map);
												
												//判断是否 是最后一道题
												if (chooseposition == mlist.size()-1) {
													btn_next.setText("完成");
												}else{
													btn_next.setText("下一题");
													
												}
												
												//判断数据库中是否 有此题，没有则添加 
												System.out.println("map里面的s_code==="+map.get("_id"));
												if(map.get("_id")!=null){
													System.out
															.println("插入信息===="+map.get("_id").toString());
													if(dao.SelectBys_id(context,map.get("_id").toString()) == false){
														
														dao.InsertInfoAnswer(context, map.get("_id").toString(), map.get("num").toString(),str);
														
													}
													
												}
											}
					
											System.out.println("map里面的s_code==="+map.get("_id"));

										}
								}

							}
							}
						}
					//	dao.InsertInfoAnswer(context, mlist.get(0).get("num").toString(), "Q2347_4461|B|1");
						oldposition = chooseposition;   
						
						System.out
								.println("oldposition 的值是多少===="+oldposition);
						madapter = new ConnectionStartTestAdapter(mlist, context);
			
						madapter.setSelectedPosition(oldposition);
						mlistview.setAdapter(madapter);
				
						mlistview.setOnItemClickListener(new OnItemClickListener() {
									@Override
									public void onItemClick(AdapterView<?> parent,View view,int position,long id) {
									
										
										mwebview.loadUrl("javascript:getAnswers()");
										mwebview.loadUrl("javascript:getCurrentQSCode()");
										try {
											Thread.sleep(100);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} 
										System.out.println("updata====>"+mlist.get(oldposition).get("_id").toString());
										//更改 试卷的答案 根据S_id
										dao.UpdateInfoAnswer(context,mlist.get(oldposition).get("_id").toString(),str);

//										mwebview.loadUrl("javascript:getAnswers()");
//										mwebview.loadUrl("javascript:getCurrentQSCode()");
//										
//										try {
//											Thread.sleep(100);
//										} catch (InterruptedException e) {
//											// TODO Auto-generated catch block
//											e.printStackTrace();
//										}
//										
//										dao.UpdateInfoAnswer(context, mlist.get(position).get("num").toString(),str,cucurrCode);
										oldposition = position;
										
										if (oldposition >= mlist.size() - 1) {
											btn_next.setText("完成");
										} else {
											btn_next.setText("下一题");
										}
										initmwebview(mlist.get(position).get("webview").toString());
										madapter.setSelectedPosition(position);
										madapter.notifyDataSetInvalidated();
									}
								});

					}
					initmwebview(mlist.get(oldposition).get("webview").toString());
					LodDialogClass.closeCustomCircleProgressDialog();
					System.out.println("chooseposition=============="+ chooseposition);
					System.out.println("mlist===================="+ mlist);
				
				}
			
			} catch (JSONException e) {
				// TODO Auto-generated catch
				// block
				e.printStackTrace();
			} 
	    	
	    	
	    }
		//重写返回键
		@Override  
	    public boolean onKeyDown(int keyCode, KeyEvent event) {  
	        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {  
	        	
	    		//清空数据库
	        	dao.deleteDatabase(context);
				finish();
	            return true;  
	        } else  
	            return super.onKeyDown(keyCode, event);  
	    }  
		
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		//计时器停止
		if(end_test_time!= null){
			end_test_time.cancel();	
			end_test_time = null;
		}
	
		
		super.onDestroy();
	}
	
	
}