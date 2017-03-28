package com.lb.duoduo.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by slam.li on 2017/3/20.
 * 可在该类中添加请求头信息
 */

public class SmartApiInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
//        Request request = chain.request();
//        Response response = chain.proceed(request);
//        BaseResponse baseResponse = CommonUtils.decodeJson(response.body().string(), BaseResponse.class);
//        if (baseResponse != null) {
//            handleResponseCode(baseResponse.code);
//        }
        return null;
    }

}
