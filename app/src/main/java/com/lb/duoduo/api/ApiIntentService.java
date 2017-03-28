package com.lb.duoduo.api;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by slam.li on 2017/3/24.
 * okhttp 初始化时涉及到了磁盘的读操作 严格模式下会用提示 所以异步初始化
 */

public class ApiIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ApiIntentService(String name) {
        super(name);
    }

    public ApiIntentService() {
        this("ApiIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ApiClient.getInstance();
    }
}
