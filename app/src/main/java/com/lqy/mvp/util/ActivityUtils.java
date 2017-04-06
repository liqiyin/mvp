package com.lqy.mvp.util;

import android.content.Intent;

import com.lqy.mvp.library.activity.BaseActivity;
import com.lqy.mvp.logic.pub.activity.QrCodeActivity;

/**
 * Created by slam.li on 2017/3/28.
 * 界面跳转管理类
 */

public class ActivityUtils {
    /**
     * 二维码跳转
     */
    public static void jumpToQrCodeActivity(BaseActivity activity) {
        activity.startActivity(new Intent(activity, QrCodeActivity.class));
    }
}
