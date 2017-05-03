package com.lqy.mvp.gallery.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by slam.li on 2017/5/2.
 * 图库边界
 */

public class GalleryGridInset extends RecyclerView.ItemDecoration {
    private int mSpanCount;
    private int mSpacing;

    public GalleryGridInset(int spanCount, int spacing) {
        this.mSpanCount = spanCount;
        this.mSpacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int column = position % mSpanCount;
        outRect.left = column * mSpacing / mSpanCount;
        outRect.right = mSpacing - (column + 1) * mSpacing / mSpanCount;
        if (position >= mSpanCount) {
            outRect.top = mSpacing; // item top
        }
    }
}
