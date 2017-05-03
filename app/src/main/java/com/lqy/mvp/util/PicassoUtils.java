package com.lqy.mvp.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by slam.li on 2017/5/2.
 * 图片加载
 */

public class PicassoUtils {
    private final static String TAG = PicassoUtils.class.getName();

    /**
     * 加载普通图片
     * @param imageView
     * @param uri
     */
    public static void loadNormalImage(ImageView imageView, Uri uri, int placeHolder) {
        Picasso.with(imageView.getContext())
                .load(uri)
                .placeholder(placeHolder)
                .config(Bitmap.Config.RGB_565)
                .into(imageView);
    }

    /**
     * 在列表中加载图片时使用
     * @param imageView
     * @param uri
     */
    public static void loadThumbnailInList(int resizeX, int resizeY, ImageView imageView, Uri uri, int placeHolder) {
        Picasso.with(imageView.getContext())
                .load(uri)
                .resize(resizeX, resizeY)
                .tag(TAG)
                .placeholder(placeHolder)
                .config(Bitmap.Config.RGB_565)
                .into(imageView);
    }

    /**
     * 在列表中加载图片时使用
     * @param imageView
     * @param uri
     */
    public static void loadSquareThumbnailInList(int resize, ImageView imageView, Uri uri, int placeHolder) {
        Picasso.with(imageView.getContext())
                .load(uri)
                .resize(resize, resize)
                .tag(TAG)
                .placeholder(placeHolder)
                .config(Bitmap.Config.RGB_565)
                .centerCrop()
                .into(imageView);
    }

    /**
     * 加载大图时使用
     * @param resizeX
     * @param resizeY
     * @param imageView
     * @param uri
     */
    public static void loadBigImage(int resizeX, int resizeY, ImageView imageView, Uri uri, int placeHolder) {
        Picasso.with(imageView.getContext())
                .load(uri)
                .resize(resizeX, resizeY)
                .placeholder(placeHolder)
                .config(Bitmap.Config.ARGB_8888)
                .priority(Picasso.Priority.HIGH)
                .centerInside()
                .into(imageView);
    }

    public static void pauseLoadImage(Context context) {
        Picasso.with(context).pauseTag(TAG);
    }

    public static void resumeLoadImage(Context context) {
        Picasso.with(context).resumeTag(TAG);
    }
}
