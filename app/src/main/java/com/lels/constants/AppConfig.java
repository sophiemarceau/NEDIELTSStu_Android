package com.lels.constants;

/**
 * 手机号 邮箱 验证 配置文件 测试环境：http://testu2.staff.xdf.cn/apis/usersv2.ashx 外网可以访问
 * 正式环境：http://passport.xdf.cn/apis/usersv2.ashx
 * */
public class AppConfig {
//准生产环境
//    	U2ApiUrl= http://passport.xdf.cn/apis/usersv2.ashx
//		U2AppID=90120
//		U2AppKey=u2ys#vskvqy*@%!vs15v
	
	
//测试环境开发 
//	U2ApiUrl=http://testu2.staff.xdf.cn
//	U2AppID=90101
//	U2AppKey=u2testAppKey#$vs
	

//	public static final String U2RootUrls = "http://testu2.staff.xdf.cn";//测试环境；
//	public static final String U2AppIds = "90101";
//	public static final String U2AppKeys = "u2testAppKey#$vs";
	
	public static final String U2RootUrls = "http://passport.xdf.cn";// 正式环境;
	public static final String U2AppIds = "90120";
	public static final String U2AppKeys = "u2ys#vskvqy*@%!vs15v";//u2ys#vskvqy*@%!vs15v
	/**
	 * U2AesKey：u2_test_aesK_test_test_test_test
	 * */
	public static final String U2AesKey = "u2_test_aesK_test_test_test_test";
	
	/**
	 *  var method = "SendEmailCodeV5"; //方法名称，固定值
	 * */
	public static final String Method = "SendEmailCodeV5";
	
}
