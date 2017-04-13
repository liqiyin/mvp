package com.lqy.mvp.logic.test.view.activity;

import android.os.Bundle;

import com.lqy.mvp.library.activity.BaseActivity;
import com.lqy.mvp.util.ActivityUtils;

/**
 * Created by slam.li on 2017/4/12.
 * 启动页
 */

public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtils.jumpToTestActivity(mActivity);
        finish();
    }
}
