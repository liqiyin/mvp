package com.lb.duouo.library.presenter;

/**
 * Created by slam.li on 2017/3/15.
 * presenter基类 所有presenter必须实现这个接口
 */

public interface BasePresenter {
    /**
     * 进入页面时调用
     */
    void subscribe();

    /**
     * 页面销毁时调用
     */
    void unSubscribe();
}
