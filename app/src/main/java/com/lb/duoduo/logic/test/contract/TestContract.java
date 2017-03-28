package com.lb.duoduo.logic.test.contract;

import com.lb.duoduo.logic.test.model.http.response.InTheatersResp;
import com.lb.duouo.library.presenter.BasePresenter;
import com.lb.duouo.library.view.BaseView;

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
