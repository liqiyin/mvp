package com.lqy.mvp.gallery;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
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

    public TakePhotoImpl(BaseActivity activity, TakeResultListener listener) {
        this.activity = activity;
        this.listener = listener;
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
                            listener.takeSuccess(gResult.asImageUriList());
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
                        List<Uri> uriList = new ArrayList<>();
                        uriList.add(cropImageUri);
                        listener.takeSuccess(uriList);
                    } else {
                        listener.takeFail("crop photo fail");
                    }
                } else {
                    listener.takeCancel();
                }
                break;
            case REQUEST_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    List<Uri> uriList = new ArrayList<>();
                    uriList.add(takePhotoImageUri);
                    listener.takeSuccess(uriList);
                } else {
                    listener.takeCancel();
                }
                break;
        }
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
        File file = new File(Environment.getExternalStorageDirectory(), File.separator + GalleryConfig.SMART_TEMP_DIR + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
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
        File file = new File(Environment.getExternalStorageDirectory(), File.separator + GalleryConfig.SMART_TEMP_DIR + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();

        //读取路径需分类
        Uri imageUri = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                ? FileProvider.getUriForFile(activity, getProviderName(), new File(gImage.getImagePath()))
                : gImage.getContentUri();
        //crop的输出路径必须是file型uri 否则需给uri授权
        cropImageUri = Uri.fromFile(file);
        cropPhoto(200, imageUri, cropImageUri);
    }

    private String getProviderName() {
        return "com.lqy.mvp.fileProvider";
    }
}
