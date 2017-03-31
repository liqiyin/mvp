package com.lqy.mvp.library.util;

import com.google.gson.Gson;

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
}
