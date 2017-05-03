package com.lqy.mvp.gallery;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lqy.mvp.R;
import com.lqy.mvp.gallery.model.GImage;
import com.lqy.mvp.gallery.widget.CheckView;
import com.lqy.mvp.library.util.ImageUtils;
import com.lqy.mvp.util.PicassoUtils;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

/**
 * Created by slam.li on 2017/5/3.
 */

public class GalleryPreviewItemFragment extends Fragment {

    private static final String ARGS_ITEM = "args_item";

    public static GalleryPreviewItemFragment newInstance(GImage gImage) {
        GalleryPreviewItemFragment fragment = new GalleryPreviewItemFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGS_ITEM, gImage);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_preview_item, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final GImage gImage = getArguments().getParcelable(ARGS_ITEM);
        if (gImage == null) {
            return;
        }

        ImageViewTouch image = (ImageViewTouch)view.findViewById(R.id.image_view);
        image.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);

        CheckView checkView = (CheckView) view.findViewById(R.id.check_view);

        Point size = ImageUtils.getBitmapSize(gImage.getContentUri(), getActivity());
        PicassoUtils.loadBigImage(size.x, size.y, image, gImage.getContentUri(), R.mipmap.ic_launcher);
    }

    public void resetView() {
        if (getView() != null) {
            ((ImageViewTouch) getView().findViewById(R.id.image_view)).resetMatrix();
        }
    }
}
