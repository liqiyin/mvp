package com.lqy.mvp.util;

import android.content.Intent;

import com.lqy.mvp.gallery.GalleryActivity;
import com.lqy.mvp.library.activity.BaseActivity;
import com.lqy.mvp.logic.pub.activity.QrCodeActivity;
import com.lqy.mvp.logic.test.view.activity.TestActivity;

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

    public static void jumpToTestActivity(BaseActivity activity) {
        activity.startActivity(new Intent(activity, TestActivity.class));
    }
}
