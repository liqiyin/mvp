package com.lqy.mvp.gallery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import java.util.List;

public interface TakePhoto {
    int REQUEST_CHOOSE = 2000;//选择图片
    int REQUEST_CROP = 2001;//剪切图片
    int REQUEST_TAKE_PHOTO = 2002;//拍照

    void onActivityResult(int requestCode, int resultCode, Intent data);
    void onRestoreInstanceState(Bundle savedInstanceState);
    void onSaveInstanceState(Bundle outState);
    /**
     *
     * @param photoCount 照片数量
     * @param isCrop 是否剪切 photoCount为1 且 isCrop为true时生效 该情况使用chooseOnePhotoAndCrop()方法
     */
    void choosePhoto(int photoCount, boolean isCrop);

    /**
     * @param size 图片大小
     * @param inputUri 源图片路径
     * @param outputUri 剪裁图片路径
     */
    void cropPhoto(int size, Uri inputUri, Uri outputUri);

    /**
     * 选择一张图片并剪裁
     */
    void chooseOnePhotoAndCrop();

    /**
     * 拍照
     */
    void takePhoto();

    interface TakeResultListener {
        void takeSuccess(List<Uri> list);
        void takeFail(String msg);
        void takeCancel();
    }
}