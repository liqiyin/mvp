package com.lqy.mvp.library.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;

public class BaseActivity extends FragmentActivity {

    /*
     * 解决Vector兼容性问题
     *
     * First up, this functionality was originally released in 23.2.0,
     * but then we found some memory usage and Configuration updating
     * issues so we it removed in 23.3.0. In 23.4.0 (technically a fix
     * release) we’ve re-added the same functionality but behind a flag
     * which you need to manually enable.
     *
     * http://www.jianshu.com/p/e3614e7abc03
     */
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    protected BaseActivity mActivity;
    protected Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String className = this.getClass().getName();
        Log.d("Activity created======>", className);

        mActivity = this;
        context = getApplicationContext();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String className = this.getClass().getName();
        Log.d("Activity destroyed====>", className);
    }

    /**
     * 设置沉浸式状态栏颜色
     * @param colorId
     */
    @TargetApi(Build.VERSION_CODES.M)
    protected void setStatusBarColor(int colorId) {
        // create our manager instance after the content view is set
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tintManager.setTintColor(getResources().getColor(colorId, null));
        } else {
            tintManager.setTintColor(getResources().getColor(colorId));
        }
    }

    public void showDialog() {
        //TODO
    }

    public void hideDialog() {
        //TODO
    }

    public void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public void showTost(int strId) {
        Toast.makeText(context, strId, Toast.LENGTH_SHORT).show();
    }
}
