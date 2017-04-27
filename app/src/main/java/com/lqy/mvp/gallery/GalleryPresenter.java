package com.lqy.mvp.gallery;

import com.lqy.mvp.library.activity.BaseActivity;
import com.lqy.mvp.library.util.RxSchedulerUtils;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class GalleryPresenter implements GalleryContract.Presenter {
    private GalleryContract.View view;
    private CompositeDisposable compositeDisposable;
    private BaseActivity activity;
    public GalleryPresenter(GalleryContract.View view, BaseActivity activity) {
        compositeDisposable = new CompositeDisposable();
        this.view = view;
        this.activity = activity;
    }

    @Override
    public void subscribe() {
        Disposable disposable = GalleryRepository.getImageList(activity)
            .compose(RxSchedulerUtils.normalSchedulersTransformer())
            .subscribe(imageList -> {
                view.displayGallery();
            });
        compositeDisposable.add(disposable);
    }

    @Override
    public void unSubscribe() {
        compositeDisposable.clear();
    }
}
