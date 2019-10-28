package com.lelts.tool;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 包名：com.example.DBAppDemo
 * 描述：
 * User 张伟
 * Date 2015/7/17 0017.
 * Time 上午 9:33.
 * 修改日期：
 * 修改内容：
 */
public class AppUtils {
    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    0);
            return info.versionCode;
        } catch (Exception e) {
            return 1;
        }
    }

}
