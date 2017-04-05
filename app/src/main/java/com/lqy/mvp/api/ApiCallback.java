package com.lqy.mvp.api;

import rx.Observer;

/**
 * Created by slam.li on 2017/3/21.
 * api 请求网络之后回调
 * 可统一对错误码处理进行处理
 */

public abstract class ApiCallback<T> implements Observer<T> {

    @Override
    public void onError(Throwable e) {
        onFail(e.getMessage());
    }

    @Override
    public void onNext(T t) {
        if (t instanceof BaseResponse) {
            if (handleResponseCode(((BaseResponse) t).code)) {
                onSuccess(t);
            } else {
                onFail(((BaseResponse) t).msg);
            }
        }
        onFail("返回类型错误");
    }

    @Override
    public void onCompleted() {}

    protected abstract void onSuccess(T obj);

    protected abstract void onFail(String msg);

    private boolean handleResponseCode(int code) {
        //TODO
        return code == 0;
    }
}
