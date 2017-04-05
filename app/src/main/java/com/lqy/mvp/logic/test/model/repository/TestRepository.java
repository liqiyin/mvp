package com.lqy.mvp.logic.test.model.repository;

import com.lqy.mvp.api.ApiClient;
import com.lqy.mvp.logic.test.model.http.response.InTheatersResp;

import rx.Observable;

/**
 * Created by slam.li on 2017/3/21.
 * model层 负责对网络或者数据库数据进行加工 由P层调用
 */

public class TestRepository {
    public static Observable<InTheatersResp> getInTheaters(int page) {
        final int PAGE_SIZE = 10;
        int start = page * PAGE_SIZE;
        Observable<InTheatersResp> observableFromNetWork =
                ApiClient.getInstance().getInTheaters("0b2bdeda43b5688921839c8ecb20399b", "上海", start, PAGE_SIZE, "", "")
                .map(inTheatersResp -> inTheatersResp);
        return observableFromNetWork;
    }
}
