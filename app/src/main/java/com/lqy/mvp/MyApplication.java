package com.lqy.mvp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.lqy.mvp.library.activity.BaseActivity;
import com.lqy.mvp.library.util.SystemUtils;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.BuglyStrategy;
import com.tencent.bugly.beta.Beta;

public class MyApplication extends Application {

    private static MyApplication myApplicationInstance;

    public static MyApplication getInstance() {
        return myApplicationInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplicationInstance = this;
        if (checkMainProcess()) {
            initStrictMode();
            initLeakCanary();
            initFresco();
            initApiService();
            initBugly();
        }
    }

    /**
     * 初始化严格模式
     */
    void initStrictMode() {
        if (BuildConfig.DEBUG) {
            //检查主线程耗时
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            //检查内存泄漏
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        }
    }

    /**
     * 初始化内存泄漏工具
     */
    void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }

    /**
     * 初始化fresco
     */
    void initFresco() {
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true)
                .setBitmapsConfig(Bitmap.Config.RGB_565)
                .build();
        Fresco.initialize(this, config);
    }

    /**
     * 初始化耗时操作
     */
    void initApiService() {
        startService(new Intent(getApplicationContext(), InitialIntentService.class));
        registerActivityLifecycleCallbacks();
    }

    /**
     * 初始化bugly 涉及到文件读写操作
     */
    void initBugly() {
        //bugly 更新检测和bug收集
        BuglyStrategy strategy = new BuglyStrategy();
        strategy.setAppChannel(SystemUtils.getQudao(getApplicationContext()));
        Beta.autoInit = true;
        Beta.autoCheckUpgrade = true;
        Beta.storageDir = getApplicationContext().getExternalCacheDir();
        Bugly.setIsDevelopmentDevice(getApplicationContext(), BuildConfig.DEBUG);//是否是开发设备
        Bugly.init(getApplicationContext(), Constants.BUGLY_APPKEY, BuildConfig.DEBUG, strategy);
    }

    /**
     * 分包支持
     */
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
        // 安装tinker
        Beta.installTinker();
    }


    /**
     * 项目中集成了推送服务 会开启多个进程 所以application的onCreate方法会走若干次 仅在主进程中初始化需要用到的东西
     */
    private boolean checkMainProcess() {
        String processName = SystemUtils.getCurProcessName(getApplicationContext());
        return "com.lb.duoduo".equals(processName);
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
                }
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