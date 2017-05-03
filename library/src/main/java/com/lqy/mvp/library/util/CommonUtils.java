package com.lqy.mvp.library.util;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.security.MessageDigest;

/**
 * Created by slam.li on 2017/3/20.
 * 常用的工具方法
 */

public class CommonUtils {
    /**
     * @param str json字符串
     * @param className 类名
     * @param <T> 转化的类类型
     * @return
     */
    public static <T> T decodeJson(String str, Class<T> className) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(str, className);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将px转换成dp值
     */
    public static int px2dp(Context context, int px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 将dp转换成px值
     */
    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 将sp转换成px值
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static String md5(String str) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if(TextUtils.isEmpty(str)){
            return null;
        }

        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);

        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }

        return hexValue.toString();
    }
}
