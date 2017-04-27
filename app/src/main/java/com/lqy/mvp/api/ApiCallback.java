package com.lqy.mvp.api;

import com.lqy.mvp.MyApplication;
import com.lqy.mvp.R;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

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
        } else {
            onFail(MyApplication.getInstance().getString(R.string.wrong_type));
        }
    }

    @Override
    public void onComplete() {}

    protected abstract void onSuccess(T obj);

    protected abstract void onFail(String msg);

    private boolean handleResponseCode(int code) {
        //TODO
        return code == 0;
    }
}
