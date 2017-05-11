package com.lqy.mvp.gallery;

import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lqy.mvp.R;
import com.lqy.mvp.gallery.model.GImage;
import com.lqy.mvp.library.fragment.BaseFragment;
import com.lqy.mvp.library.util.ImageUtils;
import com.lqy.mvp.library.util.RxSchedulerUtils;
import com.lqy.mvp.util.PicassoUtils;

import java.io.File;

import io.reactivex.disposables.Disposable;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;
import top.zibin.luban.Luban;

/**
 * Created by slam.li on 2017/5/3.
 * 图片预览选择fragment
 */

public class GalleryPreviewItemFragment extends BaseFragment {

    private static final String ARGS_ITEM = "args_item";
    ImageViewTouch image;

    public static GalleryPreviewItemFragment newInstance(GImage gImage) {
        GalleryPreviewItemFragment fragment = new GalleryPreviewItemFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGS_ITEM, gImage);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery_preview_item, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final GImage gImage = getArguments().getParcelable(ARGS_ITEM);
        if (gImage == null) {
            return;
        }

        image = (ImageViewTouch)view.findViewById(R.id.image_view);
        image.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);

        //压缩图片
        Disposable disposable = Luban.get(getActivity())
                .load(new File(gImage.getImagePath()))
                .asObservable()
                .compose(RxSchedulerUtils.normalSchedulersTransformer())
                .subscribe(file -> {
                    Point size = ImageUtils.getBitmapSize(Uri.fromFile(file), getActivity());
                    PicassoUtils.loadBigImage(size.x, size.y, image, Uri.fromFile(file), R.mipmap.ic_launcher);
                });
        ((GalleryPreviewActivity)getActivity()).insertIOTask(disposable);
    }

    /**
     * 还原图片
     */
    public void resetView() {
        if (getView() != null) {
            ((ImageViewTouch) getView().findViewById(R.id.image_view)).resetMatrix();
        }
    }
}
