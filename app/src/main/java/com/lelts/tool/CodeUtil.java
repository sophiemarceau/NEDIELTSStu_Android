package com.lelts.tool;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * 描述：项目中的加密工具
 * 
 */
public class CodeUtil
{
	/**
	 * 加密的密钥
	 */
	private static final String KEY = "92314892";
	/**
	 * 加密方法
	 * 
	 * @param encryptString
	 *            加密的文字
	 * @return String 加密后的文字
	 * @throws Exception
	 */

	public static String Encode(String encryptString)
	{
		Cipher cipher = null;
		byte[] encryptedData = null;
		try
		{
			SecretKeySpec key = new SecretKeySpec(KEY.getBytes(), "DES");
			cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			if (null != encryptString)
			{
				encryptedData = cipher.doFinal(encryptString.getBytes());
			}
			else
			{
				return "";
			}

		}
		catch (Exception e)
		{
			new RuntimeException(" CodeUtil Encode" + e.toString());
			//LogPushUtil.commitCrashLog(null, e);

		}

		if(encryptedData != null)
			return Base64.encode(encryptedData);
		else 
			return "";
	}

	/**
	 * 解密方法
	 * 
	 * @param decryptString
	 *            需要解密的文字
	 * @return String 解密后的文字
	 * @throws Exception
	 */
	public static String Decode(String decryptString)
	{
		new Base64();
		byte[] byteMi = null;
		if (null != decryptString)
		{
			byteMi = Base64.decode(decryptString);
		}
		else
		{
			return "";
		}
		
		byte[] decryptedData = null;
		try
		{
			// 设置加密密钥
			SecretKeySpec key = new SecretKeySpec(KEY.getBytes(), "DES");
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key);
			decryptedData = cipher.doFinal(byteMi);
		}
		catch (Exception e)
		{
			new RuntimeException(" CodeUtil Decode" + e.toString());
			//LogPushUtil.commitCrashLog(null, e);
		}

		// 对返回的字符串进行截取
		if(decryptedData != null)
			return new String(decryptedData);
		else
			return "";
	}

}
