package com.lqy.mvp.gallery.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.lqy.mvp.R;
import com.lqy.mvp.gallery.model.GImage;
import com.lqy.mvp.util.PicassoUtils;

/**
 * Created by slam.li on 2017/4/28.
 * 图库每个item
 */

public class GalleryGrid extends SquareFrameLayout {
    public ImageView image;
    public CheckView checkView;
    View imageCover;
    PreBindInfo preBindInfo;
    GImage source;

    public GalleryGrid(Context context) {
        super(context);
        init();
    }

    public GalleryGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.gallery_grid_content, this);
        image = (ImageView) findViewById(R.id.image);
        checkView = (CheckView) findViewById(R.id.checkbox);
        imageCover = findViewById(R.id.image_cover);
    }

    public void bind(GImage source) {
        this.source = source;
        setImage();
        initCheckView();
    }

    public void preBindMedia(PreBindInfo preBindInfo) {
        this.preBindInfo = preBindInfo;
    }

    private void setImage() {
        PicassoUtils.loadSquareThumbnailInList(preBindInfo.mResize, image, source.getContentUri(), preBindInfo.mPlaceholder);
    }

    private void initCheckView() {
        if (preBindInfo.mShowCheckView) {
            checkView.setCountable(preBindInfo.mCheckViewCountable);
        } else {
            checkView.setVisibility(GONE);
        }
    }

    public void setChecked(boolean checked) {
        checkView.setChecked(checked);
        imageCover.setVisibility(checked ? VISIBLE : GONE);
    }

    public static class PreBindInfo {
        int mResize;
        int mPlaceholder;
        boolean mCheckViewCountable;
        RecyclerView.ViewHolder mViewHolder;
        boolean mShowCheckView;

        public PreBindInfo(int resize, int placeholder, boolean checkViewCountable,
                           RecyclerView.ViewHolder viewHolder, boolean showCheckView) {
            mResize = resize;
            mPlaceholder = placeholder;
            mCheckViewCountable = checkViewCountable;
            mViewHolder = viewHolder;
            mShowCheckView = showCheckView;
        }
    }
}
