package com.lqy.mvp.gallery;

import android.content.Intent;
import android.net.Uri;

import java.util.List;

public interface TakePhoto {
    int REQUEST_CHOOSE = 2000;//选择图片
    int REQUEST_CROP = 2001;//剪切图片
    int REQUEST_TAKE_PHOTO = 2002;//拍照

    void onActivityResult(int requestCode, int resultCode, Intent data);
    void choosePhoto(int photoCount, boolean isCrop);
    void cropPhoto(int size, Uri inputUri, Uri outputUri);
    void chooseOnePhotoAndCrop();

    interface TakeResultListener {
        void takeSuccess(List<Uri> list);
        void takeFail(String msg);
        void takeCancel();
    }
}