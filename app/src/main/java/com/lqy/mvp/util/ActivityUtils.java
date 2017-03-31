package com.lqy.mvp.util;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;

import com.lqy.mvp.library.activity.BaseActivity;

/**
 * Created by slam.li on 2017/3/28.
 * 界面跳转管理类
 */

public class ActivityUtils {
    /**
     * 跳转至授予app权限界面
     * @param activity
     */
    public static void jumpToGrantPermission(BaseActivity activity) {
        PackageManager pm = activity.getPackageManager();
        PackageInfo info = null;
        try {
            info = pm.getPackageInfo(activity.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (SystemUtils.isMIUI(activity)) {
            Intent i = new Intent("miui.intent.action.APP_PERM_EDITOR");
            i.setClassName("com.android.settings", "com.miui.securitycenter.permission.AppPermissionsEditor");
            i.putExtra("extra_package_uid", info.applicationInfo.uid);
            try {
                activity.startActivity(i);
            } catch (Exception e) {}
        } else {
            Uri packageUri = Uri.parse("package:" + info.packageName);
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageUri);
            activity.startActivity(intent);
        }
    }
}
