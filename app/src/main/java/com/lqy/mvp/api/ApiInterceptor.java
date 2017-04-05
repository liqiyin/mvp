package com.lqy.mvp.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by slam.li on 2017/3/20.
 * 可在该类中添加请求头信息
 */

public class ApiInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request newRequest = request.newBuilder().addHeader("token", "jkl").build();
        return chain.proceed(newRequest);
    }

}
