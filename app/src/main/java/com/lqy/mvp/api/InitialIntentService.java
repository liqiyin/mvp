package com.lqy.mvp.api;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.lqy.mvp.BuildConfig;
import com.lqy.mvp.Constants;
import com.lqy.mvp.util.SystemUtils;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.BuglyStrategy;
import com.tencent.bugly.beta.Beta;

/**
 * Created by slam.li on 2017/3/24.
 * 耗时初始化操作在该service中实现
 */

public class InitialIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public InitialIntentService(String name) {
        super(name);
    }

    public InitialIntentService() {
        this("InitialIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //okhttp 初始化时涉及到了磁盘的读操作 严格模式下会用提示 所以异步初始化
        ApiClient.getInstance();
        initBugly();
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
        Bugly.init(getApplicationContext(), Constants.BUGLY_APPKEY, BuildConfig.DEBUG, strategy);
    }
}
