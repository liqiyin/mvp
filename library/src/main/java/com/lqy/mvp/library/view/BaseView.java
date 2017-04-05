package com.lqy.mvp.library.view;

/**
 * 关联presenter的view所要实现的接口
 * @param <T>
 */
public interface BaseView<T> {
    void showDialog();
    void hideDialog();
}
