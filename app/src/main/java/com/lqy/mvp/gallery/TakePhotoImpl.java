package com.lqy.mvp.gallery;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.lqy.mvp.gallery.model.GImage;
import com.lqy.mvp.gallery.model.GResult;
import com.lqy.mvp.gallery.model.SelectSpec;
import com.lqy.mvp.library.activity.BaseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by slam.li on 2017/5/4.
 * 拍照实现类
 */

public class TakePhotoImpl implements TakePhoto {
    BaseActivity activity;
    TakeResultListener listener;
    boolean needCrop;
    private Uri cropImageUri; //剪裁图片的输出路径uri
    private Uri takePhotoImageUri;//拍照图片的输出路径uri

    private List<Uri> resultUriList;//拍照结果的uri列表

    public TakePhotoImpl(BaseActivity activity, TakeResultListener listener) {
        this.activity = activity;
        this.listener = listener;
        if (listener == null) {
            throw new IllegalStateException("TakeResultListener can't be null");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHOOSE:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        GResult gResult = data.getParcelableExtra(GalleryConfig.IMAGE_RESULT);
                        if (gResult == null || gResult.isEmpty()) {
                            listener.takeCancel();
                            return;
                        }
                        if (needCrop) {
                            onCrop(gResult.getCropImage());
                        } else {
                            resultUriList = gResult.asImageUriList();
                            listener.takeSuccess(resultUriList);
                        }
                    } else {
                        listener.takeFail("choose photo fail");
                    }
                } else {
                    listener.takeCancel();
                }
                break;
            case REQUEST_CROP:
                needCrop = false;
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        handleOnePhotoSuccess(cropImageUri);
                    } else {
                        listener.takeFail("crop photo fail");
                    }
                } else {
                    listener.takeCancel();
                }
                break;
            case REQUEST_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    handleOnePhotoSuccess(takePhotoImageUri);
                } else {
                    listener.takeCancel();
                }
                break;
        }
    }

    /**
     * 处理获取一张图片成功的情况
     */
    private void handleOnePhotoSuccess(Uri uri) {
        List<Uri> uriList = new ArrayList<>();
        uriList.add(uri);
        resultUriList = uriList;
        listener.takeSuccess(resultUriList);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null) return;
        needCrop = savedInstanceState.getBoolean("needCrop");
        cropImageUri = savedInstanceState.getParcelable("cropImageUri");
        takePhotoImageUri = savedInstanceState.getParcelable("takePhotoImageUri");
        if (cropImageUri != null) {
            handleOnePhotoSuccess(cropImageUri);
        }
        if (takePhotoImageUri != null) {
            if (needCrop) {
                onRestoreCrop(takePhotoImageUri);
            } else {
                handleOnePhotoSuccess(takePhotoImageUri);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("needCrop", needCrop);
        outState.putParcelable("cropImageUri", cropImageUri);
        outState.putParcelable("takePhotoImageUri", takePhotoImageUri);
    }

    @Override
    public void chooseOnePhotoAndCrop() {
        choosePhoto(1, true);
    }

    @Override
    public void choosePhoto(int photoCount, boolean isCrop) {
        Intent intent = new Intent(activity, GalleryActivity.class);
        SelectSpec selectSpec = SelectSpec.getCleanInstance();
        selectSpec.maxSelectable = photoCount;
        selectSpec.showCamera = photoCount == 1;
        needCrop = isCrop && photoCount == 1;
        activity.startActivityForResult(intent, REQUEST_CHOOSE);
    }

    @Override
    public void cropPhoto(int size, Uri inputUri, Uri outputUri) {
        Intent intent = new Intent();
        intent.setAction("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(inputUri, "image/*");
        intent.putExtra("crop", "true");

        // 华为相机若想实现圆形剪切框 aspect值设为998
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        activity.startActivityForResult(intent, TakePhoto.REQUEST_CROP);
    }

    @Override
    public void takePhoto() {
        File file = new File(Environment.getExternalStorageDirectory(), File.separator + GalleryConfig.SMART_PHOTO_DIR + System.currentTimeMillis() + ".jpg");
        if (file.getParentFile() != null && !file.getParentFile().exists()) file.getParentFile().mkdirs();
        takePhotoImageUri = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                ? FileProvider.getUriForFile(activity, getProviderName(), file)
                : Uri.fromFile(file);
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, takePhotoImageUri);//将拍取的照片保存到指定URI
        activity.startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    private void onCrop(GImage gImage) {
        File file = getCropOutFile();
        if (file.getParentFile() != null && !file.getParentFile().exists()) file.getParentFile().mkdirs();

        //读取路径需分类
        Uri imageUri = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                ? FileProvider.getUriForFile(activity, getProviderName(), new File(gImage.getImagePath()))
                : gImage.getContentUri();

        cropImageUri = Uri.fromFile(file);
        cropPhoto(200, imageUri, cropImageUri);
    }

    /**
     * 页面恢复时使用
     */
    private void onRestoreCrop(Uri inputUri) {
        File file = getCropOutFile();
        if (file.getParentFile() != null && !file.getParentFile().exists()) file.getParentFile().mkdirs();
        cropImageUri = Uri.fromFile(file);
        cropPhoto(200, inputUri, cropImageUri);
    }

    private File getCropOutFile() {
        return new File(Environment.getExternalStorageDirectory(), File.separator + GalleryConfig.SMART_CROP_DIR + System.currentTimeMillis() + ".jpg");
    }

    private String getProviderName() {
        return "com.lqy.mvp.fileProvider";
    }
}
