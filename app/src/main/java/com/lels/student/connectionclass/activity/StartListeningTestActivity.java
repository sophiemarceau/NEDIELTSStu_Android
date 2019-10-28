package com.lels.student.connectionclass.activity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.strudentlelts.R;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lels.student.connectionclass.adapter.ConnectionStartTestAdapter;
import com.lelts.student.db.SqliteDao;
import com.lelts.students.bean.StuAnswerInfo;
import com.lelts.tool.Player;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class StartListeningTestActivity extends Activity implements
		OnClickListener {
	private ListView mlistview;
	private Context context;
	private List<HashMap<String, Object>> mlist;
	private ConnectionStartTestAdapter madapter;
	private WebView mwebview;
	private int oldposition;
	private Button btn_next;
	private SharedPreferences share, stuinfo;
	private ImageButton img_back;
	private Intent intent;
	private String domainPFolder, SectionID;
	private String PID;
	private int chooseposition = -1;
	private SqliteDao dao;
	private String str = "", cucurrCode = "";
	private MyWebClient mWebClient;
	private long endtime, jointime;
	private int n;
	private String data;
	// 判断 试卷的类型 type
	// 资料答题需要的参数
	// private String domainPFolder

	private List<String> Array_SectionID = new ArrayList<String>();
	private List<String> Array_PID  =  new ArrayList<String>();
	private List<String> Array_Qscodes  =  new ArrayList<String>();
	// private int testtype;
	private Button btnsure;
	private TextView allcorrect, timetv,timeall;
	private ImageView imgplay;
	private Player mplayer;
	private SeekBar seekbar;
	private String videoUrl;
	// private String videopath = "mnt/sdcard/Music/lkq.mp3";
	
	//音频的路径
	private String mylisten;
	//音频的布局
	private RelativeLayout myrelative_listen;
	//存放MP3的position
	private int preposition = 0;
	//音频播放的按钮
	
	//获取答题的参数 
	//	[Args]：
	//	 paperId=[考试试卷的主键ID]
	//	 stId=[试卷所属的任务ID]
	private String paperId,stId;
	//路径 path
	private String path;
	
	private TextView title__listening;
	private List<StuAnswerInfo> manswer_list;
	private HashMap< String, Object> answer_map;
	//多端答题
	private String  return_Qscode = "";
	private int currentPosition = 0; //应该显示第几题
	private String currentFileName = null; //当前的题是什么
//	private Map<String, Object> map_answer;
	private Myreceview myreceview;
	private IntentFilter intentFilter;
	//判断是否获得多段答题位置
	private  boolean flag  = true;
	private String sectionAnswerStr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_startlistening_test); 
		
		initview();
		LodDialogClass.showCustomCircleProgressDialog(context, null,getString(R.string.xlistview_header_hint_loading));
		getjointime();
		
		getExamInfo();
//		if (flag==true) {
//			
//			loadSectionPaperInfo();
//			flag = false;
//		}



	}


	/**
	 * 初始化数据
	 * 
	 */
	private void initview() {
		// TODO Auto-generated method stub
		context = this;
		//注册广播
		myreceview = new Myreceview();
		intentFilter = new IntentFilter("com.lelts.tool");
		registerReceiver(myreceview, intentFilter);
		intent = new Intent();


//		context.registerReceiver(new Myreceview(), new IntentFilter("com.lelts.tool"));
		
//		map_answer = new HashMap<String, Object>();
		mlistview = (ListView) findViewById(R.id.mlistview_connection_starttest);
		mlist = new ArrayList<HashMap<String, Object>>();
		mwebview = (WebView) findViewById(R.id.webview_connection_starttest);
		btn_next = (Button) findViewById(R.id.btn_next_conn_starttest);
		btn_next.setOnClickListener(this);
		share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		stuinfo = getSharedPreferences("stuinfo", Context.MODE_PRIVATE);
		img_back = (ImageButton) findViewById(R.id.img_back_listening);
		img_back.setOnClickListener(this);
		
		paperId = stuinfo.getString("P_ID", "");
		stId = stuinfo.getString("ST_ID", "");
		
		domainPFolder = stuinfo.getString("domainPFolder", "");
		Intent getintent = getIntent();
		if (getintent != null) {
			chooseposition = getintent.getIntExtra("chooseposition", -1);
			flag = getintent.getBooleanExtra("flag", true);
		} else {
			chooseposition = -1;
			flag = true;
		}
		System.out.println("flag的值======"+flag);
		System.out.println("返回的选择的第几题号。。。。。chooseposition===" + chooseposition);
		dao = new SqliteDao();
		//音频
		timetv = (TextView) findViewById(R.id.tv_lx_self);
		timeall = (TextView) findViewById(R.id.timeall);
		seekbar = (SeekBar) findViewById(R.id.seekbar1_self1);

		
		imgplay = (ImageView) findViewById(R.id.listener_correct_start_img1);
		imgplay.setOnClickListener(this);
		
		mplayer = new Player(seekbar,imgplay);
		seekbar.setOnSeekBarChangeListener(new SeekBarChangeEvent());
		// 实例化share
		share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		System.out.println(share.getString("Token", ""));
		myrelative_listen = (RelativeLayout) findViewById(R.id.listener_correct_play_linear1);
		seekbar.setEnabled(false);
	}
	/**
	 * 获取试卷的信息 
	 */
	
	private void getExamInfo() {
		// TODO Auto-generated method stub
//		[Auth]：token
//		[URL]：/PaperInfo/loadSectionPaperInfo
//		[Method]：POST
//		[Args]：
//		 paperId=[考试试卷的主键ID]
//		 stId=[试卷所属的任务ID]
//		[Return]：
//		 Infomation = 提示信息;
//		 Result = 返回结果true/false;
//		 Data: {
//		  "checkHasRecord": 用于判断是否有未答完题记录，1表示有，0表示没有
//		  "ST_ID": 任务ID,
//		  "P_ID": 试卷ID,paperId,
//		  "UID": 用户ID,
//		  "SectionID": 做题位置sectionId
//		  "SectionName": 做题位置sectionName
//		  "SectionAnswerStr": 答案字符串
//		  "PID": 做题位置pId
//		  "Qscode": 做题位置Qscode,
//		  "CostTime": 答题时间
//		 }

		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		params.addBodyParameter("paperId", paperId);
		params.addBodyParameter("stId", stId);
		System.out.println("paperId===" + paperId+ "=====stId===" + stId);
		HttpUtils util = new HttpUtils();
		path = Constants.URL_loadSectionPaperInfo;
		util.send(HttpMethod.POST, path, params, new RequestCallBack<String>() {

	

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				System.out.println("onFailure");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				String result = arg0.result;
				System.out.println("学获取多端答题位置信息不包含数据库操作----------" + result);
				try {
					JSONObject object = new JSONObject(result);
					String Result = object.getString("Result"); 
					String Infomation = object.getString("Infomation");
					if (Result.equals("true")) {
						//获取数据成功
						JSONObject Data = object.getJSONObject("Data");
//						"checkHasRecord": 用于判断是否有未答完题记录，1表示有，0表示没有
						String checkHasRecord = Data.getString("checkHasRecord");
						if(checkHasRecord.equals("1")){
//							  "ST_ID": 任务ID,
//							  "P_ID": 试卷ID,paperId,
//							  "UID": 用户ID,
//							  "SectionID": 做题位置sectionId
//							  "SectionName": 做题位置sectionName
//							  "SectionAnswerStr": 答案字符串
//							  "PID": 做题位置pId
//							  "Qscode": 做题位置Qscode,
//							  "CostTime": 答题时间
							String CostTime = Data.getString("CostTime");
							sectionAnswerStr = Data.getString("SectionAnswerStr");
							String UID = Data.getString("UID");
							String PID = Data.getString("PID");
							String ST_ID = Data.getString("ST_ID");
							return_Qscode = Data.getString("Qscode");
							String SectionID = Data.getString("SectionID");
						
							ObjectMapper objectMapper = new ObjectMapper();
					 
					            //将json字符串转成map结合解析出来，并打印(这里以解析成map为例)
//					            Map<String, Map<String, Object>> maps;
//					            List< Map<String, Map<String, Object>> > answer_list = null ;
//									answer_list = new ArrayList<Map<String,Map<String,Object>>>();
// 									maps = objectMapper.readValue(SectionAnswerStr, Map.class);
//						            System.out.println("转换成map的长度====="+maps.size());
//						            Set<String> key = maps.keySet();
//						            Iterator<String> iter = key.iterator();
//						            while (iter.hasNext()) {
//						                String field = iter.next();
//						                System.out.println(field + ":" + maps.get(field));
//						            }
//						            answer_list.add(maps);
//						            System.out.println("答案的list集合====="+maps.toString());
							System.out.println("sectionAnswerStr====="+sectionAnswerStr+"====return_Qscode======"+return_Qscode);
							flag = true;
						}else{
							System.out.println("没有未答完的题==========");
							flag = false;
						}
						
						getPaperQuestionListformdata();
					}else{
						//接口返回问题
						Toast.makeText(context, Infomation, Toast.LENGTH_SHORT).show();
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

	}
	
	
	/**
	 * 获取多端答题位置信息
	 * 
	 */
	private void loadSectionPaperInfo() {
		// TODO Auto-generated method stub
//		[Auth]：token
//		[URL]：/PaperInfo/loadSectionPaperInfo
//		[Method]：POST
//		[Args]：
//		 paperId=[考试试卷的主键ID]
//		 stId=[试卷所属的任务ID]
//		[Return]：
//		 Infomation = 提示信息;
//		 Result = 返回结果true/false;
//		 Data: {
//		  "checkHasRecord": 用于判断是否有未答完题记录，1表示有，0表示没有
//		  "ST_ID": 任务ID,
//		  "P_ID": 试卷ID,paperId,
//		  "UID": 用户ID,
//		  "SectionID": 做题位置sectionId
//		  "SectionName": 做题位置sectionName
//		  "SectionAnswerStr": 答案字符串
//		  "PID": 做题位置pId
//		  "Qscode": 做题位置Qscode,
//		  "CostTime": 答题时间
//		 }

//		RequestParams params = new RequestParams();
//		params.addHeader("Authentication", share.getString("Token", ""));
//		params.addBodyParameter("paperId", paperId);
//		params.addBodyParameter("stId", stId);
//		System.out.println("paperId===" + paperId+ "=====stId===" + stId);
//		HttpUtils util = new HttpUtils();
//		path = Constants.URL_loadSectionPaperInfo;
//		util.send(HttpMethod.POST, path, params, new RequestCallBack<String>() {
//
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				// TODO Auto-generated method stub
//				System.out.println("onFailure");
//			}
//
//			@Override
//			public void onSuccess(ResponseInfo<String> arg0) {
//				// TODO Auto-generated method stub
//				String result = arg0.result;
//				System.out.println("学获取多端答题位置信息----------" + result);
//				try {
//					JSONObject object = new JSONObject(result);
//					String Result = object.getString("Result"); 
//					String Infomation = object.getString("Infomation");
//					if (Result.equals("true")) {
//						//获取数据成功
//						JSONObject Data = object.getJSONObject("Data");
////						"checkHasRecord": 用于判断是否有未答完题记录，1表示有，0表示没有
//						String checkHasRecord = Data.getString("checkHasRecord");
//						if(checkHasRecord.equals("1")){
////							  "ST_ID": 任务ID,
////							  "P_ID": 试卷ID,paperId,
////							  "UID": 用户ID,
////							  "SectionID": 做题位置sectionId
////							  "SectionName": 做题位置sectionName
////							  "SectionAnswerStr": 答案字符串
////							  "PID": 做题位置pId
////							  "Qscode": 做题位置Qscode,
////							  "CostTime": 答题时间
//							String CostTime = Data.getString("CostTime");
//							String SectionAnswerStr = Data.getString("SectionAnswerStr");
//							String UID = Data.getString("UID");
//							String PID = Data.getString("PID");
//							String ST_ID = Data.getString("ST_ID");
////							return_Qscode = Data.getString("Qscode");
//							String SectionID = Data.getString("SectionID");
//						
//							ObjectMapper objectMapper = new ObjectMapper();
//					 
//					            //将json字符串转成map结合解析出来，并打印(这里以解析成map为例)
//					            Map<String, Map<String, Object>> maps;
//					            List< Map<String, Map<String, Object>> > answer_list = null ;
//									answer_list = new ArrayList<Map<String,Map<String,Object>>>();
//// 									maps = objectMapper.readValue(SectionAnswerStr, Map.class);
////						            System.out.println("转换成map的长度====="+maps.size());
////						            Set<String> key = maps.keySet();
////						            Iterator<String> iter = key.iterator();
////						            while (iter.hasNext()) {
////						                String field = iter.next();
////						                System.out.println(field + ":" + maps.get(field));
////						            }
////						            answer_list.add(maps);
////						            System.out.println("答案的list集合====="+maps.toString());
//						            String str;
//									try {
//										str = new String(SectionAnswerStr.getBytes("ISO-8859-1"), "GB18030");
//						            System.out.println("str============="+str);
//						    		str  = str.replace("\"", "").replace("{", "").replace("}", "");
//						    		System.out.println("teststr replace = > " + str);
//						    		String[] strs = str.split(",");
//						    		for (String string : strs) {
//						    			if (":".equals(string)) {
//						    				continue;
//						    			}
//						    			String[] sectionid_pid_sqcode_answer = string.split(":");
//						    			if (sectionid_pid_sqcode_answer.length == 1) {
//						    				continue;
//						    			}
//						    			String sectionid_pid = sectionid_pid_sqcode_answer[0];
//						    			String sqcode_answer = sectionid_pid_sqcode_answer[1];
//						    			if (TextUtils.isEmpty(sqcode_answer) || TextUtils.isEmpty(sectionid_pid)) {
//						    				continue;
//						    			}
//						    			//System.out.println("teststr sectionid_pid : " + sectionid_pid + ", sqcode_answer : " + sqcode_answer);
//						    			String[] sp = sectionid_pid.split("_");
//						    			//System.out.println("teststr sectionid : " + sp[0] + ", pid : " + sp[1]);
//						    			String[] sa = sqcode_answer.split("\\|");
//						    			if (sa.length == 1) {
//						    				continue;
//						    			}
//						    			System.out.println("teststr =====有效数据=====sectionid : " + sp[0] + ", pid : " + sp[1] + ", sqcode : " + sa[0] + ", answers : " + sqcode_answer.split("\\|", 2)[1]);
////						    			dao.InsertInfoAnswer(context,  sp[0]+"_"+sp[1]+"_"+sa[0], sa[0], sa[0]+sqcode_answer.split("\\|", 2)[1]);
//						    			System.out.println("根据s_id====="+sp[0]+"_"+sp[1]+"_"+sa[0]+"====来更改s_answer===="+sa[0]+"|"+sqcode_answer.split("\\|", 2)[1]);
//						    			dao.UpdateInfoAnswer(context, sp[0]+"_"+sp[1]+"_"+sa[0], sa[0]+"|"+sqcode_answer.split("\\|", 2)[1]);
////						    			dao.InsertInfoAnswer(context, sp[0]+"_"+sp[1]+"_"+sa[0], "", sa[0]+sqcode_answer.split("\\|", 2)[1]);
//						    		}
//						    		
//									} catch (UnsupportedEncodingException e) {
//										// TODO Auto-generated catch block
//										e.printStackTrace();
//									}
//
//							
//						}else{
//							System.out.println("没有未答完的题==========");
//						}
////						getPaperQuestionListformdata();
//						
//					}else{
//						//接口返回问题
//						Toast.makeText(context, Infomation, Toast.LENGTH_SHORT).show();
//					}
//					
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//			}
//		});
		
		
		ObjectMapper objectMapper = new ObjectMapper();
 
            //将json字符串转成map结合解析出来，并打印(这里以解析成map为例)
            Map<String, Map<String, Object>> maps;
            List< Map<String, Map<String, Object>> > answer_list = null ;
				answer_list = new ArrayList<Map<String,Map<String,Object>>>();
//					maps = objectMapper.readValue(SectionAnswerStr, Map.class);
//	            System.out.println("转换成map的长度====="+maps.size());
//	            Set<String> key = maps.keySet();
//	            Iterator<String> iter = key.iterator();
//	            while (iter.hasNext()) {
//	                String field = iter.next();
//	                System.out.println(field + ":" + maps.get(field));
//	            }
//	            answer_list.add(maps);
//	            System.out.println("答案的list集合====="+maps.toString());
	            String str;
					try {
						str = new String(sectionAnswerStr.getBytes("ISO-8859-1"), "GB18030");
					    System.out.println("str============="+str);
			    		str  = str.replace("\"", "").replace("{", "").replace("}", "");
			    		System.out.println("teststr replace = > " + str);
			    		String[] strs = str.split(",");
			    		for (String string : strs) {
			    			if (":".equals(string)) {
			    				continue;
			    			}
			    			String[] sectionid_pid_sqcode_answer = string.split(":");
			    			if (sectionid_pid_sqcode_answer.length == 1) {
			    				continue;
			    			}
			    			String sectionid_pid = sectionid_pid_sqcode_answer[0];
			    			String sqcode_answer = sectionid_pid_sqcode_answer[1];
			    			if (TextUtils.isEmpty(sqcode_answer) || TextUtils.isEmpty(sectionid_pid)) {
			    				continue;
			    			}
			    			//System.out.println("teststr sectionid_pid : " + sectionid_pid + ", sqcode_answer : " + sqcode_answer);
			    			String[] sp = sectionid_pid.split("_");
			    			//System.out.println("teststr sectionid : " + sp[0] + ", pid : " + sp[1]);
			    			String[] sa = sqcode_answer.split("\\|");
			    			if (sa.length == 1) {
			    				continue;
			    			}
			    			System.out.println("teststr =====有效数据=====sectionid : " + sp[0] + ", pid : " + sp[1] + ", sqcode : " + sa[0] + ", answers : " + sqcode_answer.split("\\|", 2)[1]);
//			    			dao.InsertInfoAnswer(context,  sp[0]+"_"+sp[1]+"_"+sa[0], sa[0], sa[0]+sqcode_answer.split("\\|", 2)[1]);
			    			System.out.println("根据s_id====="+sp[0]+"_"+sp[1]+"_"+sa[0]+"====来更改s_answer===="+sa[0]+"|"+sqcode_answer.split("\\|", 2)[1]);
			    			dao.UpdateInfoAnswer(context, sp[0]+"_"+sp[1]+"_"+sa[0], sa[0]+"|"+sqcode_answer.split("\\|", 2)[1]);
//			    			dao.InsertInfoAnswer(context, sp[0]+"_"+sp[1]+"_"+sa[0], "", sa[0]+sqcode_answer.split("\\|", 2)[1]);
				
			    		}
			    		
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        

		
		
	}
	
	/**
	 * 提交部分试卷答案 
	 * 
	 */
	private void submitSectionAnswer() {
		//		SectionID_PID
		manswer_list = dao.GetallAnswer(context);
		System.out.println("获取的答案信息======="+manswer_list);
		System.out.println("Array_SectionID的数组的长度==="+Array_SectionID.size()+"Array_PID的数组的长度====");
		answer_map = new HashMap<String, Object>();
		
		for (int i = 0; i < Array_SectionID.size(); i++) {
			System.out.println("Array_SectionID.get(i)==="+Array_SectionID.get(i));
			String m_answer = manswer_list.get(i).getAnswer();
			answer_map.put(Array_SectionID.get(i)+"_"+Array_PID.get(i),m_answer);
		}
		System.out.println("answer_map======"+answer_map);
		ObjectMapper mapper = new ObjectMapper();
		String jsonfromMap = null;
			try {
				jsonfromMap = mapper.writeValueAsString(answer_map);
				System.out.println("jsonfromMap--" + jsonfromMap);
			} catch (JsonGenerationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (JsonMappingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		// TODO Auto-generated method stub
		///[Auth]：token
		//[URL]：/PaperInfo/loadSectionPaperInfo
		//[Method]：POST
		//[Args]：
		// paperId=[考试试卷的主键ID]
		// stId=[试卷所属的任务ID]
		//[Return]：
		// Infomation = 提示信息;
		// Result = 返回结果true/false;
		// Data: {
		//  "checkHasRecord": 用于判断是否有未答完题记录，1表示有，0表示没有
		//  "ST_ID": 任务ID,
		//  "P_ID": 试卷ID,paperId,
		//  "UID": 用户ID,
		//  "SectionID": 做题位置sectionId
		//  "SectionName": 做题位置sectionName
		//  "SectionAnswerStr": 答案字符串
		//  "PID": 做题位置pId
		//  "Qscode": 做题位置Qscode,
		//  "CostTime": 答题时间
		// }
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		params.addBodyParameter("paperId", paperId);
		params.addBodyParameter("studentAnswers", jsonfromMap);
		params.addBodyParameter("stId", stId);
		params.addBodyParameter("costTime", n+"");
		params.addBodyParameter("sectionName", "");
		params.addBodyParameter("sectionID", SectionID);
		params.addBodyParameter("pId", PID);
		params.addBodyParameter("Qscode", cucurrCode);
		System.out.println("paperId===" + paperId+ "=====studentAnswers===" + jsonfromMap+ "=====stId===" + stId+ "=====costTime===" + n+ "=====sectionID===" + SectionID+ "=====pId===" + PID+ "=====Qscode===" + cucurrCode);
		HttpUtils util = new HttpUtils();
		path = Constants.URL_submitSectionAnswer;
		util.send(HttpMethod.POST, path, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				System.out.println("onFailure");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				String result = arg0.result;
				System.out.println("提交部分试卷答案的信息---------" + result);
				try {
					JSONObject object = new JSONObject(result);
					String Result = object.getString("Result");
					String Infomation = object.getString("Infomation");
					if (Result.equals("true")) {
						
					}else{
						//接口返回问题
						Toast.makeText(context, Infomation, Toast.LENGTH_SHORT).show();
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		
	}
	
	/**
	 * 转换成时间
	 * 
	 */
	private String toTime(int time) {
		int minute = time / 1000 / 60;
		int s = time / 1000 % 60;
		String mm = null;
		String ss = null;
		if (minute < 10)
			mm = "0" + minute;
		else
			mm = minute + "";

		if (s < 10)
			ss = "0" + s;
		else
			ss = "" + s;

		return mm + ":" + ss;
	}

	/**
	 * seekbar进度监听
	 * 
	 * @author Administrator
	 *
	 */
	class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
		int progre;

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			this.progre = progress * mplayer.mediaPlayer.getCurrentPosition()
					/ seekBar.getMax();
			timetv.setText(toTime(progre));
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字
			mplayer.mediaPlayer.seekTo(progre);
		}
	}

	/**
	 * 加载WebView
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
		// mwebview.getSettings().setLayoutAlgorithm(
		// WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		// // 开启 DOM storage API 功能
		// mwebview.getSettings().setDomStorageEnabled(true);
		// // 开启 database storage API 功能
		// mwebview.getSettings().setDatabaseEnabled(true);
		// // 优先使用缓存
		// mwebview.getSettings()
		// .setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
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
		// 在JS中调用本地java方法
		mwebview.addJavascriptInterface(new WebTOANDROIDInterface(), "android");
	}

	

	public class WebTOANDROIDInterface {

		@JavascriptInterface
		public void getAnswers(String line) {
			str = line;

			System.out.println("str=====>" + str);
		}

		@JavascriptInterface
		public void getCurrentQSCode(String Code) {
			cucurrCode = Code;
			System.out.println("currCode======>" + cucurrCode);
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
		
		case R.id.listener_correct_start_img1:
			
			if (mplayer.mediaPlayer.isPlaying()) {
				mplayer.pause();
				imgplay.setBackgroundResource(R.drawable.bofang);
			}else{
				
				mplayer.play();
				imgplay.setBackgroundResource(R.drawable.tingzhi);
			}

			break;
		
		// 下一题
		case R.id.btn_next_conn_starttest:
			imgplay.setBackgroundResource(R.drawable.bofang);
			mwebview.loadUrl("javascript:getAnswers()");
//			map_answer.put(cucurrCode, str);
//			System.out.println("map_answer===="+map_answer);
			mwebview.loadUrl("javascript:getCurrentQSCode()");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("oldposition 的值是====" + oldposition);
			System.out.println("updata====>"
					+ mlist.get(oldposition).get("num").toString());
			// 根据s_id来 更改试卷的答案
			dao.UpdateInfoAnswer(context, mlist.get(oldposition).get("_id")
					.toString(), str);

			oldposition++;
			
//			System.out.println("听力的资料===="+mlist.get(oldposition)
//					.get("listen").toString());
			

			if (oldposition == mlist.size() - 1) {
				btn_next.setText("完成");
				madapter.setSelectedPosition(oldposition);
				madapter.notifyDataSetInvalidated();
				initmwebview(mlist.get(oldposition).get("webview").toString());
			} else if (oldposition < mlist.size()) {
				
				if (mlist.get(oldposition).get("listen")==null||mlist.get(preposition).get("listen")==null) {
					
				}else{

				if(preposition!=oldposition){
					System.out
							.println("已经换题");
					String newlisten,prelisten;
					newlisten = mlist.get(oldposition).get("listen").toString();
					prelisten = mlist.get(preposition).get("listen").toString();
					if(newlisten.equals(prelisten)){
						System.out
								.println("音频相同");
						seekbar.setThumb(context.getResources().getDrawable(R.drawable.seekbartag));
						if(newlisten.equals("")){
							//音频文件为空
							System.out
							.println("音频文件为空");
							timeall.setText("/00:00");
							mplayer.mediaPlayer.stop();
							myrelative_listen.setClickable(false);
							imgplay.setClickable(false);
							imgplay.setBackgroundResource(R.drawable.bofanghuise);
							seekbar.setThumb(context.getResources().getDrawable(R.drawable.zuobiaohuise));
						}
					}else{
						System.out
						.println("音频不相同");
						if(newlisten.equals("")){
							//音频文件为空
							System.out
							.println("音频文件为空");
							timeall.setText("/00:00");
							mplayer.mediaPlayer.stop();
							myrelative_listen.setClickable(false);
							imgplay.setClickable(false);
							imgplay.setBackgroundResource(R.drawable.bofanghuise);
							seekbar.setThumb(context.getResources().getDrawable(R.drawable.zuobiaohuise));
						}else{
							System.out
							.println("音频文件不为空");
							myrelative_listen.setClickable(true);
							imgplay.setClickable(true);
							mplayer.playUrl(domainPFolder+"/"+mlist.get(oldposition)
									.get("listen").toString());
							timeall.setText("/"+toTime(mplayer.mediaPlayer.getDuration()));
							seekbar.setThumb(context.getResources().getDrawable(R.drawable.seekbartag));
						}
						
					}
//					
			
					preposition = oldposition;
				}else{
					
				}
			}
				
				
				madapter.setSelectedPosition(oldposition);
				madapter.notifyDataSetInvalidated();
				initmwebview(mlist.get(oldposition).get("webview").toString());
				btn_next.setText("下一题");
			} else if (oldposition == mlist.size()) {
				btn_next.setText("完成");

				intent.setClass(StartListeningTestActivity.this,
						ConnectionSaveAnswerActivity.class);
				intent.putExtra("state", 1);
				startActivity(intent);
				// startActivityForResult(intent, INT_CODE);
				finish();
			} else {
				oldposition = mlist.size();
			}
			break;
		// 返回上一级
		case R.id.img_back_listening:
			// intent.setClass(ConnetionStartTestActivity.this,
			// StundentClassroom.class);
			// startActivity(intent);
			dao.deleteDatabase(context);
			finish();

			break;
			

		default:
			break;
		}

	}


	private class MyWebClient extends WebViewClient {

		private String urlLoad;

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
			view.loadUrl(url);
			urlLoad = url;
			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			
			
			
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			System.out.println("onPageFinished============>");
			// 根据 s_code 来获取试卷的 答案
			System.out.println("数据list======="+mlist+"=====加载网页时候的oldposition========"+oldposition);
			
			data = dao.setAnswer(context, mlist.get(oldposition).get("_id")
					.toString());
//			if (map_answer.isEmpty()) {
//				data="";
//			}else{
//				data = map_answer.get(cucurrCode).toString();
//			}
//		 
			int HideContent = 0;
			System.out.println("data======>" + data);
			mwebview.loadUrl("javascript:setMyAnswers(\'" + data + "\')");
			mwebview.loadUrl("javascript:setHideContent(\'" + HideContent + "\');");
			mwebview.loadUrl("javascript:getCurrentQSCode()");
			super.onPageFinished(view, url);
		}
	}

	/**
	 * 方法说明：获取进入的答题时间
	 * 
	 */
	private void getjointime() {
		// TODO Auto-generated method stub
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String strr = formatter.format(curDate);
		long Hours = curDate.getHours();
		long Seconds = curDate.getSeconds();
		long Minutes = curDate.getMinutes();
		jointime = Hours * 3600 + Minutes * 60 + Seconds;
		System.out.println("Join===========" + strr);
		System.out.println("curDate.getTime();" + curDate.getTime());
		System.out.println("	curDate.getSeconds();" + curDate.getSeconds());
		System.out.println("	curDate.getHours();" + curDate.getHours());
		System.out.println("	curDate.getMinutes();" + curDate.getMinutes());
		Editor ed = stuinfo.edit();
		ed.putInt("jointime", (int)jointime);
		ed.commit();
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
	 * 方法说明：网络解析 资料答题的试卷
	 * 
	 */
	private void getPaperQuestionListformdata() {
		// TODO Auto-generated method stub
		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(0);
		utils.configDefaultHttpCacheExpiry(0);
		System.out.println("资料的domainPFolder==="
				+ domainPFolder);
		
		utils.send(HttpMethod.GET,domainPFolder
				+ "/config_phone.json", new RequestCallBack<String>() {
			private String QSCode;

			@Override
			public void onFailure(HttpException arg0, String arg1) {

			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				String testresutl = arg0.result;
				System.out.println("testresutl==========>" + testresutl);
				try {
					JSONObject ob = new JSONObject(testresutl);
				
					JSONArray array = ob.getJSONArray("PaperCatagoryList");
					for (int i = 0; i < array.length(); i++) {
						JSONObject PaperCatagoryList = array.getJSONObject(i);
					
						JSONArray array2 = PaperCatagoryList
								.getJSONArray("paperSectionList");
						for (int j = 0; j < array2.length(); j++) {
							JSONObject paperSectionList = array2
									.getJSONObject(j);
							SectionID = paperSectionList.getString("SectionID");
							Array_SectionID.add(SectionID);
							JSONArray array3 = paperSectionList
									.getJSONArray("QuestionPageList");
							System.out.println("array3=================="
									+ array3);
							for (int k = 0; k < array3.length(); k++) {
								JSONObject QuestionPageList = array3
										.getJSONObject(k);
								PID = QuestionPageList.getString("PID");
								Array_PID.add(PID);
								//获取音频文件
								JSONArray array5 = QuestionPageList
										.getJSONArray("AudioFiles");
								//音频文件
								if(array5.length()==0){
									mylisten = "";
								}else{
									mylisten = array5.get(0).toString();
									
								}
								//获取试卷的内容
								
								JSONArray array4 = QuestionPageList
										.getJSONArray("FileName");
								System.out
										.println("FileName[]===========" + array4);
								for (int l = 0; l < array4.length(); l++) {
									HashMap<String, Object> map = new HashMap<String, Object>();
									JSONObject FileName_obj = array4.getJSONObject(l);
									JSONArray Qscodes = FileName_obj.getJSONArray("Qscodes");
									//存试卷的Scode ,判断答到什么位置
									for (int m = 0; m < Qscodes.length(); m++) {
										JSONObject obj_QSCode = Qscodes.getJSONObject(m);
										QSCode = obj_QSCode.getString("QSCode");
										System.out.println("QSCode======"+QSCode+"====return_Qscode=="+return_Qscode);
										if (!return_Qscode.equals("null")||return_Qscode.equals("")) {
										if (return_Qscode.equals(QSCode)) {//如果Qscode和另一个接口返回的一样，那么就找到了
											currentPosition =j ; //确定了是那套题，也就是知道了listview该定位到什么位置
											currentFileName =FileName_obj.getString("FileName");//确认了那个FileName，也就可以根据他的url加载网页
											System.out.println("currentPosition====="+currentPosition+"=====currentFileName====="+currentFileName);
										}
										}
										
									}
									String FileName = FileName_obj
											.getString("FileName");
									String QNumberBegin = FileName_obj
											.getString("QNumberBegin");
									String QNumberEnd = FileName_obj
											.getString("QNumberEnd");
									int QnumBegin = Integer
											.parseInt(QNumberBegin);
									int QnumEnd = Integer.parseInt(QNumberEnd);
									// 判断题号的 填空题多道题的则要单独判断
									if((QnumEnd-QnumBegin)>0){
										if ((QnumEnd - QnumBegin) == 1) {
											map.put("num", QnumBegin);
											System.out.println("获取的音频文件=="+mylisten);
											map.put("listen", mylisten);
										} else {
											map.put("num", QnumBegin + "~"
													+ (QnumEnd - 1) );
											System.out.println("获取的音频文件=="+mylisten);
											map.put("listen", mylisten);
										}
										map.put("PID", PID);
										map.put("SectionID", SectionID);
										map.put("_id", SectionID+"_"+PID+"_"+QSCode);
										// domainPFolder+http://testielts2.staff.xdf.cn/upload_dev/paperzip/TempPaper_10768/
										map.put("webview",
												domainPFolder
											+ "/" + FileName);
										mlist.add(map);
									}
					
									// 判断是否 是最后一道题
									if (chooseposition == mlist.size() - 1) {
										btn_next.setText("完成");
									} else {
										btn_next.setText("下一题");

									}
									// 判断数据库中是否 有此题，没有则添加
									System.out.println("map里面的s_code==="
											+ map.get("_id"));
									if(map.get("_id")!=null){
										if (dao.SelectBys_id(context, map
												.get("_id").toString()) == false) {
											System.out.println("数据库的s_id======="+map.get("_id").toString());
											dao.InsertInfoAnswer(context,map.get("_id").toString(),map.get("num").toString(), str);
										}

									}
							
								}

							}
							if (chooseposition==-1) {
								
								oldposition = currentPosition;
								if (chooseposition == mlist.size() - 1) {
									btn_next.setText("完成");
								}
							}else{
								
								oldposition = chooseposition;
								
							}
							madapter = new ConnectionStartTestAdapter(mlist,
									context);
							madapter.setSelectedPosition(oldposition);
							mlistview.setAdapter(madapter);
							//加载第一个文件，并且判断第一个文件是否有听力
							System.out.println("mlsit的值==="+mlist);
							if(mlist.get(0).get("listen").toString().equals("")){
								//为空
								timeall.setText("/00:00");
								mplayer.mediaPlayer.stop();
							}else{
								
								mplayer.playUrl(domainPFolder+"/"+mlist.get(0)
										.get("listen").toString());
								timeall.setText("/"+toTime(mplayer.mediaPlayer.getDuration()));
							}
							

							mlistview
									.setOnItemClickListener(new OnItemClickListener() {
										@Override
										public void onItemClick(
												AdapterView<?> parent,
												View view, int position, long id) {
											SectionID = mlist.get(position).get("SectionID").toString();
											PID = mlist.get(position).get("PID").toString();
											System.out.println("SectionID===="+SectionID+"=======PID====="+PID);
											imgplay.setBackgroundResource(R.drawable.bofang);
											mwebview.loadUrl("javascript:getAnswers()");
//											mwebview.loadUrl("javascript:getCurrentQSCode()");
											try {
												Thread.sleep(100);
											} catch (InterruptedException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}
											System.out.println("updata====>"
													+ mlist.get(oldposition)
															.get("num")
															.toString());
											// 更改 试卷的答案 根据S_id
											dao.UpdateInfoAnswer(context, mlist
													.get(oldposition)
													.get("_id").toString(), str);

											oldposition = position;

											if (oldposition >= mlist.size() - 1) {
												btn_next.setText("完成");
											} else {
												btn_next.setText("下一题");
											}
											initmwebview(mlist.get(position)
													.get("webview").toString());
											
											System.out.println("听力的资料===="+mlist.get(position)
													.get("listen").toString());

											if(preposition!=position){
												System.out
														.println("已经换题");
												String newlisten,prelisten;
												newlisten = mlist.get(position).get("listen").toString();
												prelisten = mlist.get(preposition).get("listen").toString();
												if(newlisten.equals(prelisten)){
													System.out
															.println("音频相同");
													seekbar.setThumb(context.getResources().getDrawable(R.drawable.seekbartag));
													if(newlisten.equals("")){
														//音频文件为空
														System.out
														.println("音频文件为空");
														timeall.setText("/00:00");
														mplayer.mediaPlayer.stop();
														myrelative_listen.setClickable(false);
														imgplay.setClickable(false);
														imgplay.setBackgroundResource(R.drawable.bofanghuise);
														seekbar.setThumb(context.getResources().getDrawable(R.drawable.zuobiaohuise));
													}
												}else{
													System.out
													.println("音频不相同");
													if(newlisten.equals("")){
														//音频文件为空
														System.out
														.println("音频文件为空");
														timeall.setText("/00:00");
														mplayer.mediaPlayer.stop();
														myrelative_listen.setClickable(false);
														imgplay.setClickable(false);
														imgplay.setBackgroundResource(R.drawable.bofanghuise);
														seekbar.setThumb(context.getResources().getDrawable(R.drawable.zuobiaohuise));
													}else{
														System.out
														.println("音频文件不为空");
														myrelative_listen.setClickable(true);
														imgplay.setClickable(true);
														mplayer.playUrl(domainPFolder+"/"+mlist.get(position)
																.get("listen").toString());
														timeall.setText("/"+toTime(mplayer.mediaPlayer.getDuration()));
														seekbar.setThumb(context.getResources().getDrawable(R.drawable.seekbartag));
													}
													
												}
												preposition = position;
												
											}else{
												
											}
											
											madapter.setSelectedPosition(position);
											madapter.notifyDataSetInvalidated();
												
										}
									});

						}
						initmwebview(mlist.get(oldposition).get("webview")
								.toString());
						System.out.println("chooseposition=============="
								+ chooseposition);
						System.out.println("mlist====================" + mlist);
		
					}
					System.out.println("flag的值 ======"+flag+"sectionAnswerStr===="+sectionAnswerStr);
					if (flag==true&&!(sectionAnswerStr.equals("null")||sectionAnswerStr.equals("")||sectionAnswerStr.equals(null))) {
						
						loadSectionPaperInfo();
						flag = false;
						
					}
					LodDialogClass.closeCustomCircleProgressDialog();
					
//					mwebview.loadUrl("javascript:getAnswers()");
					//判断是否有音频文件
					for (int i = 0; i < mlist.size(); i++) {
						System.out.println(""+mlist.get(i).get("listen"));
						if (!mlist.get(i).get("listen").toString().equals("")) {
						//有音频文件
						myrelative_listen.setVisibility(View.VISIBLE);
						System.out.println("有音频");
						return;
					}else{
						//有听力文件  布局消失
						myrelative_listen.setVisibility(View.GONE);
						System.out.println("没有音频");
					}
					}
		
				} catch (JSONException e) {
					// TODO Auto-generated catch
					// block
					e.printStackTrace();
				}

			}

		});
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

		//播放器停止
		mplayer.stop();
		unregisterReceiver(myreceview);
		super.onDestroy();
	}
	
	private class Myreceview extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			System.out.println("=====收到广播======");
			submitSectionAnswer();
		}
	}

}
