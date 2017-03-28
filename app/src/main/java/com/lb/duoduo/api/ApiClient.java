package com.lb.duoduo.api;

import com.lb.duoduo.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by slam.li on 2017/3/20.
 * 请求服务封装
 */
public final class ApiClient {

    private static volatile ApiService apiService;

    private static void init() {
        String smartApiHost = ApiConstants.TEST_HOST;
        apiService = initSmartService(smartApiHost, ApiService.class);
    }

    public static ApiService getInstance() {
        if (apiService == null) {
            synchronized (ApiClient.class) {
                if (apiService == null) {
                    init();
                }
            }
        }
        return apiService;
    }


    private static <T> T initSmartService(String baseUrl, Class<T> clazz) {

        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(httpLoggingInterceptor);
        }
        //添加自定义拦截器 可添加头参数等 TODO
//        builder.addInterceptor(new SmartApiInterceptor());
        OkHttpClient client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        return retrofit.create(clazz);
    }

}

