package com.lqy.mvp.logic.test.contract;

import com.lqy.mvp.logic.test.model.http.response.InTheatersResp;
import com.lqy.mvp.library.presenter.BasePresenter;
import com.lqy.mvp.library.view.BaseView;

import java.util.List;

/**
 * Created by slam.li on 2017/3/21.
 * 定义P层和V层接口
 */

public interface TestContract {
    interface View extends BaseView<Presenter> {
        void displayRequestContent(List<InTheatersResp.SubjectsBean> inTheatersResp);
    }

    interface Presenter extends BasePresenter {
        void loadTestContent(int start);
    }
}
