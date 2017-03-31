package com.lqy.mvp;

import com.lqy.mvp.library.activity.BaseActivity;

import java.lang.ref.WeakReference;

/**
 * Created by slam.li on 2017/3/21.
 * 栈顶activity获取类
 * 在其他不传activity引用时但必须使用activity实体类时使用(比如dialog) 可调用该方法获取
 */
public class CurrentActivityManager {
    private static CurrentActivityManager currentActivityManager = new CurrentActivityManager();

    private WeakReference<BaseActivity> activityWeakReference;
    private CurrentActivityManager() {}

    public static CurrentActivityManager getInstance(){
        return currentActivityManager;
    }

    public BaseActivity getCurrentActivity() {
        BaseActivity activity = null;
        if (activityWeakReference != null) {
            activity = activityWeakReference.get();
        }
        return activity;
    }

    public void setCurrentActivity(BaseActivity activity) {
        activityWeakReference = new WeakReference<>(activity);
    }
}
