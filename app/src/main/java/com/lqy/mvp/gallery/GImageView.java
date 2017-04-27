package com.lqy.mvp.gallery;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by slam.li on 2017/4/26.
 * 高宽相同的ImageView
 */

public class GImageView extends ImageView {
    public GImageView(Context context) {
        super(context);
    }

    public GImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);
    }
}
