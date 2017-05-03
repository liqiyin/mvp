package com.lqy.mvp.gallery;

import com.lqy.mvp.gallery.model.GAlbum;
import com.lqy.mvp.library.presenter.BasePresenter;
import com.lqy.mvp.library.view.BaseView;

import java.util.List;

/**
 * Created by slam.li on 2017/4/26.
 *
 */

public class GalleryContract {
    interface View extends BaseView<Presenter> {
        void displayGallery(List<GAlbum> albumList);
    }

    interface Presenter extends BasePresenter {
    }
}
