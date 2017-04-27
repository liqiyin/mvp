package com.lqy.mvp.logic.test.presenter;

import com.lqy.mvp.api.ApiCallback;
import com.lqy.mvp.logic.test.contract.TestContract;
import com.lqy.mvp.logic.test.model.http.response.InTheatersResp;
import com.lqy.mvp.logic.test.model.repository.TestRepository;
import com.lqy.mvp.library.util.RxSchedulerUtils;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by slam.li on 2017/3/21.
 * P层具体实现
 */

public class TestPresenter implements TestContract.Presenter {

    private TestContract.View view;
    private CompositeDisposable compositeDisposable;
    public TestPresenter(TestContract.View view) {
        compositeDisposable = new CompositeDisposable();
        this.view = view;
    }

    @Override
    public void subscribe() {}

    @Override
    public void unSubscribe() {
        compositeDisposable.clear();
    }

    @Override
    public void loadTestContent(int start) {
        TestRepository.getInTheaters(start)
            .compose(RxSchedulerUtils.normalSchedulersTransformer())
            .subscribe(new ApiCallback<InTheatersResp>() {
                @Override
                public void onSubscribe(@NonNull Disposable disposable) {
                    compositeDisposable.add(disposable);
                }

                @Override
                protected void onSuccess(InTheatersResp obj) {
                    view.displayRequestContent(obj.subjects);
                }

                @Override
                protected void onFail(String msg) {
                    view.displayRequestContent(null);
                }
            });
    }
}
