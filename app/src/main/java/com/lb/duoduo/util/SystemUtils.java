package com.lb.duoduo.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.lb.duoduo.SmartApplication;

import java.util.List;
import java.util.UUID;

public class SystemUtils {
    /**
     * Killing app from process level
     */
    public static void killProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    /**
     * @return
     */
    public static String getIMSI() {
        TelephonyManager telephonyManager = (TelephonyManager) SmartApplication.getInstance()
                .getApplicationContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        String IMSI = telephonyManager.getSubscriberId();
        return IMSI;
    }

    /**
     * @return
     */
    public static String getDeviceID() {
        Context context = SmartApplication.getInstance();
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = tm.getDeviceId() + "";
        tmSerial = tm.getSimSerialNumber() + "";
        androidId = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider
                .Settings.Secure.ANDROID_ID) + "";

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString();
        return deviceId;
    }

    /**
     * @return
     */
    public static int getVersionCode() {
        PackageInfo packInfo = getPackageInfo();
        if (packInfo != null) {
            return packInfo.versionCode;
        } else {
            return -1;
        }
    }

    /**
     * @return
     */
    public static String getVersionName() {
        PackageInfo packInfo = getPackageInfo();
        if (packInfo != null) {
            return packInfo.versionName;
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * @return
     */
    public static String getPhoneModel() {
        return Build.MODEL;
    }

    /**
     * @return
     */
    public static String getPhoneManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * @return
     */
    public static PackageInfo getPackageInfo() {
        try {
            Context context = SmartApplication.getInstance();
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param targetPackage
     * @return
     */
    public static boolean isPackageExisted(String targetPackage) {
        PackageManager pm = SmartApplication.getInstance().getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(targetPackage, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

    private static boolean checkManufacturer(String manufacturerName) {
        return SystemUtils.getPhoneManufacturer().toLowerCase().equals(manufacturerName);
    }

    /**
     * @return
     */
    public static boolean isMeizu() {
        return checkManufacturer("meizu");
    }

    /**
     * @return
     */
    public static boolean isHuawei() {
        return checkManufacturer("huawei");
    }

    public static boolean isInStatusBarBlackList() {
        return isMeizu() || isHuawei();
    }

    public static boolean isMIUI(Context context) {
        boolean result = false;
        Intent i = new Intent("miui.intent.action.APP_PERM_EDITOR");
        i.setClassName("com.android.settings",
                "com.miui.securitycenter.permission.AppPermissionsEditor");
        if (isIntentAvailable(context, i)) {
            result = true;
        }
        return result;
    }

    private static boolean isIntentAvailable(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.GET_ACTIVITIES);
        return list.size() > 0;
    }

}
