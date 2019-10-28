package com.lels.constants;

public class Constants {

//	 开发环境 http://testielts2.staff.xdf.cn /IELTS_2_DEV /upload_dev
	// 产品测试环境 http://testielts2.staff.xdf.cn /IELTS_2 /upload
	// 业务测试环境 http://ieltstest.staff.xdf.cn /IELTS /upload
	
//	准生产环境  http://ilearning.staff.xdf.cn/IELTS/
	
//
	public static final String URL_Base = "http://testielts2.staff.xdf.cn";
	public static final String URL_PROJECT = "/IELTS_2_DEV";
	public static final String URL_Mapped = "/upload_dev";
	
//	public static final String URL_Base = "http://ilearning.staff.xdf.cn";
//	public static final String URL_PROJECT = "/IELTS";
//	public static final String URL_Mapped = "/upload";
	
//public static final String URL_Base = "http://10.62.49.51:8080";
//public static final String URL_PROJECT = "/IELTS_2";
//public static final String URL_Mapped = "/upload_dev";

	public static final String URL_API = "/api";
	public static final String URL_UserImage = "/userImage";
	public static final String URL_ = URL_Base + URL_PROJECT + URL_API;
	

	// >>>>>>>>Mzh8eGRmMDA1MDA1MDIwNnwxNDM4MDc0MjI5NzM3-----------
	public static final String URL_V = URL_Base + URL_PROJECT;

	private static final String URL_A = URL_Base + URL_Mapped;

	/**
	 * 用户头像路径
	 */
	public static final String URL_USERIMG = URL_Base + URL_Mapped
			+ URL_UserImage + "/";
	/**
	 * 广告图片路径
	 */
	public static final String URL_IMAGE_ADVERT = URL_A + "/advertImage/";
	/**
	 * 学生端获取最近一次考试汇报 iphone、Android 学生App
	 */
	public static final String URL_HomegetUserReport = URL_
			+ "/Home/getUserReport";

	/**
	 * 学生端班级任务-学习 iphone、Android 学生App
	 */
	public static final String URL_HomeCompleteRate = URL_
			+ "/Home/CompleteRate";
	/**
	 * 任务-获取模考、练习信息 iphone、Android 学生App
	 */
	public static final String URL_HomegetPapersInfo = URL_
			+ "/Home/getPapersInfo";
	/**
	 * 删除/标记消息已读 iphone、Android 学生App
	 */
	public static final String URL_MessageReadOrDelStuMessage = URL_
			+ "/Message/ReadOrDelStuMessage";
	/**
	 * 任务-获取资料信息 iphone、Android 学生App
	 */
	public static final String URL_HomegetMaterialsInfo = URL_
			+ "/Home/getMaterialsInfo";
	/**
	 * 任务-视频资料中试题库 iphone、Android 学生App
	 */
	public static final String URL_HomegetPapersInfoByMID = URL_
			+ "/Home/getPapersInfoByMID";
	/**
	 * APP学生登陆登录
	 * */
	public static final String URL_STUDENT_LOGIN = URL_
			+ "/User/AppStudentLogin";

	/**
	 * 学生端注册 iphone、Android 学生App
	 * */
	public static final String URL_STUDENT_REGISTER = URL_
			+ "/User/userRegister";

	/**
	 * [URL]：/Task/GetStudentMonthLessonsForApp [Method]：GET
	 * [Args]：dateParam=[要查询的日期2015-06-22]
	 * */
	public static final String URL_STUDENT_CALENDAR = URL_
			+ "/Task/GetStudentMonthLessonsForApp";
	/***
	 * 投票的接口
	 * 
	 */
	

	/**
	 *学生端心跳检测是否结束投票iphone、Android 学生App

	 * */
	public static final String URL_loadMyVote = URL_
			+ "/ActiveClass/loadMyVote";
	

	/**
	 * 学生端弃权投票iphone、Android 学生App
	 * */
	public static final String URL_waiverVote = URL_
			+ "/ActiveClass/waiverVote";
	
	/**
	 *学生端参与投票iphone、Android 学生App
	 * */
	public static final String URL_joinVote = URL_
			+ "/ActiveClass/joinVote";
	
	/**
	 * 学生端检测是否有投票iphone、Android 学生App
	 * */
	public static final String URL_checkHasVote = URL_
			+ "/ActiveClass/checkHasVote";
	
	/**
	 * 教师端心跳汇总当前投票数据，与学生查询投票结果一致iphone、Android 教师/学生App
	 * */
	public static final String URL_collectStuVotes = URL_
			+ "/ActiveClass/collectStuVotes";
	


	/**
	 * APP学生 学生端点击“同步到班”，获取班级详情，展示学员列表
	 * */
	public static final String URL_StudentGetStudentOnLine = URL_
			+ "/ActiveClass/GetStudentClassDetail";
	/**
	 * APP学生 每隔3秒向服务器发送在线的消息，获取最新在线学生列表 包括在线人数/总人数和各学生信息
	 * */
	public static final String URL_TeacherOrStudentGetStudentOnLine = URL_
			+ "/ActiveClass/TeacherOrStudentGetStudentOnLine";

	/**
	 * APP学生 每隔3秒 获取老师开始答题，获得当前课次教师推送的随堂练习信息
	 * */
	public static final String URL_FindPushStatusActiveClassPaperInfo = URL_
			+ "/ActiveClass/FindPushStatusActiveClassPaperInfo";

	/**
	 * APP学生 查询课堂练习需要完成的(教师推送的)题
	 * */
	public static final String URL_ActiveClassPaperQuestionNumberTodoList = URL_
			+ "/ActiveClass/ActiveClassPaperQuestionNumberTodoList";

	/**
	 * APP学生 提交试卷时，先生成examInfoId
	 * */
	public static final String URL_createExamInfoId = URL_
			+ "/PaperInfo/createExamInfoId";
	/**
	 * APP学生 提交试卷(判卷) - 随堂练习
	 * */
	public static final String URL_EvaluateTheActiveClassExercisePaper = URL_
			+ "/PaperInfo/EvaluateTheActiveClassExercisePaper";

	/**
	 * 提交试卷(判卷) - 资料试卷 iphone、Android 学生App
	 * */
	public static final String URL_EvaluateTheMaterialsExercisePaper = URL_
			+ "/PaperInfo/EvaluateTheMaterialsExercisePaper";
	
	
	/**
	 * 提交试卷(判卷) - 正规判卷，客观题和写作题 ipad、iphone、Android 学生App
	 * */
	public static final String URL_EvaluateThePaper = URL_
			+ "/PaperInfo/EvaluateThePaper";
	

	/**
	 * 学生端 学习资料答题报告 iphone、Android 学生App
	 * */
	public static final String URL_findMaterialsExerciseReport = URL_
			+ "/Material/findMaterialsExerciseReport";

	/**
	 * 学生端 提交后，课堂练习报告 iphone、Android 学生App
	 * */
	public static final String URL_findStudentOwnExerciseReport = URL_
			+ "/ActiveClass/findStudentOwnExerciseReport";
	
	/**
	 * 学生端 学生端获取历史练习信息iphone、Android 学生App
	 * */
	public static final String URL_loadStudentHisExercise = URL_
			+ "/ActiveClass/loadStudentHisExercise";
	
	
	/**
	 * 学生端 随堂互动首页，获取学员自己的历史随堂练习列表
	 * */
	public static final String URL_findStudentOwnHistoryExercise = URL_
			+ "/ActiveClass/findStudentOwnHistoryExercise";

	/**
	 * 学生端 获取学员自己的历史随堂练习列表 - 往期课堂练习报告
	 * */
	public static final String URL_findStudentOwnHistoryExerciseReport = URL_
			+ "/ActiveClass/findStudentOwnHistoryExerciseReport";

	/**
	 * 心跳 学生端，获取随堂练习的课堂状态
	 * */
	public static final String URL_ActiveClassStatus = URL_
			+ "/ActiveClass/ActiveClassStatus";

	/**
	 * 心跳 获取教师结束当前练习的指令，如状态为结束，学生端强制提交当前随堂练习试卷的作答
	 * */
	public static final String URL_ActiveClassExerciseStatus = URL_
			+ "/ActiveClass/ActiveClassExerciseStatus";

	/**
	 * 学生个人信息 iphone、Android、Ipad 学生App
	 * */
	public static final String URL_MYSELFINFO = URL_ + "/User/getUserInfo";

	/**
	 * 学生个人信息 iphone、Android、Ipad 学生App
	 * */
	public static final String URL_STUDYONLINE_MAIN = URL_
			+ "/onlinestudy/StudyMaterials";

	/**
	 * 我的收藏-公开课 iphone、Android 学生App
	 * */
	public static final String URL_MYSELF_MYCOLLECT_COLLECT = URL_
			+ "/Material/MyOpenClassFavoriteList";
	/**
	 * 我的收藏-资料 iphone、Android 学生App
	 * */
	public static final String URL_MYSELF_MYCOLLECT_DATA = URL_
			+ "/Material/loadMaterialsFavoriteList";
	/**
	 * 我的收藏-预测 iphone、Android 学生App
	 * */
	public static final String URL_MYSELF_MYCOLLECT_PREDICT = URL_
			+ "/Material/MyPredictFavoriteList";
	/**
	 * 获取广告位信息 iphone、Android 学生/教师App
	 * */
	public static final String URL_UserloadAdvertisements = URL_
			+ "/User/loadAdvertisements";
	/**
	 * 系统消息 -消息列表 iphone、Android、Ipad 教师App、学生App
	 * */
	public static final String URL_MYSELF_MYMESSAGE = URL_
			+ "/Message/allMessageNoReadForStu";

	/**
	 * 删除/标记消息已读 iphone、Android 学生App
	 * */
	public static final String URL_MYSELF_MESSAGE_DELECT = URL_
			+ "/Message/ReadOrDelStuMessage";

	/**
	 * 帐号管理 iphone、Android 学生App
	 * */
	public static final String URL_MYSELF_GETAXXOUNTINFO = URL_
			+ "/User/GetStudentAccountInfo";

	/**
	 * 修改昵称 iphone、Android 学生App
	 * */
	public static final String URL_MYSELF_NICKNAMECHANGE = URL_
			+ "/User/NickNameChange";

	/**
	 * 修改密码 iphone、Android 学生App
	 * */
	public static final String URL_MYSELF_PASSWORDCHANGE = URL_
			+ "/User/passchange";

	/**
	 * 绑定学员号 iphone、Android 学生App
	 * */
	public static final String URL_MYSELF_BINDSTUDENTCODE = URL_
			+ "/User/bindstudentcode";
	/**
	 * 修改个性签名 iphone、Android 学生App
	 * */
	public static final String URL_MYSELF_SIGNATURECHANGE = URL_
			+ "/User/SignatureChange";

	/**
	 * 学生 在线学习 视频公开课 iphone、Android 学生App
	 * */
	public static final String URL_STUDYONLINE_PUBLICCLASS_DATE = URL_
			+ "/onlinestudy/VideoOpenClass";

	/**
	 * 学生 在线学习 视频公开课筛选 iphone、Android 学生App
	 * */
	public static final String URL_STUDYONLINE_PUBLICCLASS_DATE_SCREEN = URL_
			+ "/onlinestudy/VideoOpenClassFilter";
	/**
	 * 游客 资料-视频公开课筛选 iphone、Android 游客App
	 * */
	public static final String URL_onlinestudyvistorVideoMaterials = URL_
			+ "/onlinestudy/vistorVideoMaterials";

	/**
	 * 学生 在线学习 学习资料 iphone、Android 学生App
	 * */
	public static final String URL_STUDYONLINE_STUDY_DATE = URL_
			+ "/onlinestudy/StudyMaterials";

	/**
	 * 学生 在线学习 学习资料筛选 iphone、Android 学生App ======= 学生 在线学习 学习资料筛选 iphone、Android
	 * 学生App >>>>>>> .r1548
	 * */
	public static final String URL_STUDYONLINE_STUDY_DATE_SCREEN = URL_
			+ "/onlinestudy/StudyMaterialsFilter";

	/**
	 * 游客资料筛选 iphone、Android 游客App
	 * */
	public static final String URL_onlinestudyvistorMaterials = URL_
			+ "/onlinestudy/vistorMaterials";

	/**
	 * 学生端考前预测 iphone、Android 学生App ======= 添加、取消收藏 iphone、Android 学生/教师App
	 * */
	public static final String URL_STUDYONLINE_COLLECT_DATA = URL_
			+ "/Material/AddOrCancelMaterialsFavorite";

	/**
	 * 获取视频信息 iphone、Android 学生/教师App
	 * */
	public static final String URL_STUDYONLINE_LOOKVIDEOINFO = URL_
			+ "/Material/lookVideoInfo";

	/**
	 * 资料/预测是否收藏 iphone、Android 已登录游客/学生/教师App type = [类型,不可为空，0资料1预测]  id =
	 * [资料或预测ID，根据类型判断,不可为空]
	 * */
	public static final String URL_STUDYONLINE_MEDIA_ISCOLLECT = URL_
			+ "/Material/checkCollectMaterials";

	/**
	 * 视频资料查看 iphone、Android 已登录游客/学生/教师App
	 * */
	public static final String URL_STUDYONLINE_MEDIA_LOOKUPVIDEO = URL_
			+ "/Material/lookUpVideoMaterials";

	/**
	 * 学生端考前预测 iphone、Android 学生App ======= 添加、取消收藏 iphone、Android 学生/教师App
	 * */
	public static final String URL_STUDYONLINE_PAPERSINFOBUMID = URL_
			+ "/Home/getPapersInfoByMID";

	/**
	 * <<<<<<< .mine 预测 添加、取消收藏 iphone、Android 学生/教师App ======= <<<<<<< .mine 预测
	 * 添加、取消收藏 iphone、Android 学生/教师App >>>>>>> .r1922
	 * */
	public static final String URL_PREDICT_COLLECT_DATA = URL_
			+ "/Material/AddOrCancelPredictFavorite";

	/**
	 * <<<<<<< .mine 查看资料增加查看次数 iphone、Android 学生/教师App 查看资料增加查看次数
	 * iphone、Android 学生/教师App ======= 查看资料增加查看次数 iphone、Android 学生/教师App
	 * ======= 查看资料增加查看次数 iphone、Android 学生/教师App >>>>>>> .r1631 >>>>>>> .r1922
	 * */
	public static final String URL_STUDYONLINE_ADDREADCOUNT = URL_
			+ "/Material/addReadCount";

	/**
	 * 更新TaskFinish任务完成情况 iphone、Android 学生App [Auth]：token
	 * [URL]：/Task/InsertTaskFinish [Method]：GET
	 * */
	public static final String URL_STUDYONLINE_TASK_FINISH = URL_
			+ "/Task/InsertTaskFinish";

	/**
	 * 查看资料详情 = 视频资料
	 * */
	public static final String URL_STUDYONLINE_STUDY_PUBLICCLASS_DETAIL_VIDEO = URL_V
			+ "/materials/selectVideoMaterialsById?mId=";

	/**
	 * 学生端考前预测 iphone、Android 学生App
	 * */
	public static final String URL_STUDYONLINE_TESTPREDICTION = URL_
			+ "/Home/getPredictInfos";

	/**
	 * 学生端我的目标 iphone、Android 学生App
	 * */
	public static final String URL_MYSELF_MYTARGETSPAGE = URL_
			+ "/Home/MyTargetsPage";

	/**
	 * 学生端设置-我的目标-更新学生设定的目标成绩iphone、Android 学生App
	 * */
	public static final String URL_MYSELF_TARGET_UPDATESTUDENTSETTINGS = URL_
			+ "/Home/UpdateStudentSettings";

	/**
	 * 学生端设置-我的目标-更新学生设定的目前成绩iphone、Android 学生App
	 * */
	public static final String URL_MYSELF_TAEGET_MYLASTSCORES = URL_
			+ "/Home/UpdateStudentSettingsMyLastScores";

	/**
	 * 学生端设置-我的目标-更新我的目标中的考试类别iphone、Android 学生App
	 * */
	public static final String URL_MYSELF_TAEGET_MYSKSLB = URL_
			+ "/Home/UpdateStudentSettingsKslb";

	/**
	 * 学生端设置-我的目标-新增我设定的各种日期iphone、Android 学生App
	 * */
	public static final String URL_MYSELF_TAEGET_ADDTARGETDATE = URL_
			+ "/Home/AddTargetDate";

	/**
	 * 学生端设置-我的目标-删除一条我设定的各种日期iphone、Android 学生App
	 * */
	public static final String URL_MYSELF_TAEGET_REMOVETARGETDATE = URL_
			+ "/Home/RemoveTargetDate";

	/**
	 * 学生端考试汇报 iphone、Android 学生App
	 * */
	public static final String URL_MYSELF_TESTRESULT_SENDREPORT = URL_
			+ "/Home/sendReport";

	/**
	 * 上传学生头像
	 * */
	public static final String URL_MYSELF_UPDATE_MYICON = URL_
			+ "/User/UploadMyIcon";

	/**
	 * 获取广告位信息 iphone、Android 学生/教师App
	 * */
	public static final String URL_MYSELF_LOADADVERTISEMENTS = URL_
			+ "/User/loadAdvertisements";

	/**
	 * APP退出系统 iphone、Android、Ipad 教师App、学生App
	 * */
	public static final String URL_MYSELF_LOGOFFUSER = URL_
			+ "/User/AppLogoffUser";
	/**
	 * 获取课堂ID iphone、Android 教师/学生App
	 * */
	public static final String URL_ActiveClass_getIdByPassCode = URL_
			+ "/ActiveClass/getIdByPassCode";
	/**
	 * 判断是否已经分组 iphone、Android 教师/学生App
	 * */
	public static final String URL_ActiveClass_getDivideGroupFlag = URL_
			+ "/ActiveClass/getDivideGroupFlag";
	/**
	 * 获取自己组内信息 iphone、Android 学生App
	 * */
	public static final String URL_ActiveClass_loadMyGroup = URL_
			+ "/ActiveClass/loadMyGroup";
	/**
	 * 获取聊天室Token iphone、Android 学生/教师App
	 */
	public static final String URL_ActiveClass_getChatToken = URL_ + "/ActiveClass/getChatToken";
	/**
	 * 获取聊天室列表信息 iphone、Android 学生/教师App
	 */
	public static final String URL_ActiveClass_loadChatList = URL_ + "/ActiveClass/loadChatList";
	
	/**
	 * 提交部分试卷答案 - iphone、Android 学生App
	 */
	public static final String URL_submitSectionAnswer = URL_ + "/PaperInfo/submitSectionAnswer";
	
	
	/**
	 * 获取多端答题位置信息 - iphone、Android 学生App
	 */
	public static final String URL_loadSectionPaperInfo = URL_ + "/PaperInfo/loadSectionPaperInfo";
	
	
	/**
	 * 更新在线时间- iphone、Android 学生App
	 */
	public static final String URL_updateLogoffTime = URL_ + "/User/updateLogoffTime";
	/**
	 * 资料查看 iphone、Android 学生、教师App
	 */
	public static final String URL_Material_lookUpMaterials = URL_
			+ "/Material/lookUpMaterials";
	/**
	 * 获取最后一次模考 iphone、Android 学生App
	 */
	public static final String URL_Task_getLastExam = URL_
			+ "/Task/getLastExam";
	
}
