package com.lb.duoduo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.lb.duoduo.api.ApiIntentService;
import com.lb.duouo.library.activity.BaseActivity;

public class SmartApplication extends Application {

    private static SmartApplication smartApplicationInstance;

    public static SmartApplication getInstance() {
        return smartApplicationInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            //检查主线程耗时
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDeath().build());
            //检查内存泄漏
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
        }

        smartApplicationInstance = this;
        Fresco.initialize(this);

        //测试中发现6.0没有耗时操作 而4.2有耗时 可能是okhttp针对不同api版本底层实现不同 为了适配 将初始化工作交由ApiIntentService初始化
//        ApiClient.init();
        startService(new Intent(getApplicationContext(), ApiIntentService.class));
        registerActivityLifecycleCallbacks();
    }

    /**
     * 该方法用来注册activity状态的回调
     * 仅支持API版本大于等于14
     * 这里使用它的目的：
     * 1.获取正在和用户交互的activity实体类
     * （待增加）
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    void registerActivityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}

            @Override
            public void onActivityStarted(Activity activity) {}

            @Override
            public void onActivityResumed(Activity activity) {
                if (activity instanceof BaseActivity) {
                    CurrentActivityManager.getInstance().setCurrentActivity((BaseActivity) activity);
                    return;
                }
                throw new RuntimeException("The activity is not subclass of BaseActivity !");
            }

            @Override
            public void onActivityPaused(Activity activity) {}

            @Override
            public void onActivityStopped(Activity activity) {}

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

            @Override
            public void onActivityDestroyed(Activity activity) {}
        });
    }
}