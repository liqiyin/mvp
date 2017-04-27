package com.lqy.mvp.api;

import com.lqy.mvp.logic.test.model.http.response.InTheatersResp;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by slam.li on 2017/3/20.
 * api 请求接口封装类
 */

public interface ApiService {
    //test豆瓣电影
    @GET("in_theaters")
    Observable<InTheatersResp> getInTheaters(@Query("apikey") String apiKey,
                                             @Query("city") String city,
                                             @Query("start") int start,
                                             @Query("count") int count,
                                             @Query("client") String client,
                                             @Query("udid") String udid);

}
