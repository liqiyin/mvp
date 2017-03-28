package com.lb.duoduo.logic.test.presenter;

import com.lb.duoduo.api.ApiCallback;
import com.lb.duoduo.logic.test.contract.TestContract;
import com.lb.duoduo.logic.test.model.http.response.InTheatersResp;
import com.lb.duoduo.logic.test.model.repository.TestRepository;
import com.lb.duouo.library.util.RxSchedulerUtils;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by slam.li on 2017/3/21.
 * P层具体实现
 */

public class TestPresenter implements TestContract.Presenter {

    private TestContract.View view;
    private CompositeSubscription subscriptions;
    public TestPresenter(TestContract.View view) {
        subscriptions = new CompositeSubscription();
        this.view = view;
    }

    @Override
    public void subscribe() {}

    @Override
    public void unSubscribe() {
        subscriptions.clear();
    }

    @Override
    public void loadTestContent(int start) {
        Subscription subscription = TestRepository.getInTheaters(start)
            .compose(RxSchedulerUtils.normalSchedulersTransformer())
            .subscribe(new ApiCallback<InTheatersResp>() {
                @Override
                protected void onSuccess(InTheatersResp obj) {
                    view.displayRequestContent(obj.subjects);
                }

                @Override
                protected void onFail(String msg) {

                }
            });
        subscriptions.add(subscription);
    }
}
