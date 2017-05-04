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
import com.lqy.mvp.gallery.model.SelectSpec;
import com.lqy.mvp.library.activity.BaseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by slam.li on 2017/5/4.
 * 拍照类
 */

public class TakePhotoImpl implements TakePhoto {
    BaseActivity activity;
    TakeResultListener listener;
    boolean needCrop;
    private Uri cropImageUri;

    public TakePhotoImpl(BaseActivity activity, TakeResultListener listener) {
        this.activity = activity;
        this.listener = listener;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_CHOOSE:
                if (data != null) {
                    ArrayList<GImage> gImageArrayList = data.getParcelableArrayListExtra(GalleryActivity.RESULT_IMAGE_LIST);
                    if (gImageArrayList == null || gImageArrayList.size() == 0) {
                        listener.takeCancel();
                        return;
                    }
                    if (needCrop) {
                        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
                        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();

                        GImage gImage = gImageArrayList.get(0);
                        Uri imageUri = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            imageUri = FileProvider.getUriForFile(activity, "com.lqy.mvp.fileProvider", new File(gImage.getImagePath()));
                        } else {
                            imageUri = gImage.getContentUri();
                        }
                        cropImageUri = Uri.fromFile(file);
                        cropPhoto(200, imageUri, cropImageUri);
                    } else {
                        List<Uri> uriList = new ArrayList<>();
                        for (GImage gImage: gImageArrayList) {
                            uriList.add(gImage.getContentUri());
                        }
                        listener.takeSuccess(uriList);
                    }
                } else {
                    listener.takeFail("choose photo fail");
                }
                break;
            case REQUEST_CROP:
                needCrop = false;
                if (data != null) {
                    List<Uri> uriList = new ArrayList<>();
                    uriList.add(cropImageUri);
                    listener.takeSuccess(uriList);
                } else {
                    listener.takeFail("crop photo fail");
                }
                break;
            case REQUEST_TAKE_PHOTO:
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
}
