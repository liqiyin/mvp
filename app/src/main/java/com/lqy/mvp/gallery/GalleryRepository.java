package com.lqy.mvp.gallery;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.lqy.mvp.R;
import com.lqy.mvp.gallery.model.GAlbum;
import com.lqy.mvp.gallery.model.GImage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by slam.li on 2017/4/26.
 *
 */

public class GalleryRepository {
    public static Observable<List<GAlbum>> getImageMap(Context context) {
        return Observable.create(subscriber -> {
            Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            String key_MIME_TYPE = MediaStore.Images.Media.MIME_TYPE;
            String key_DATA = MediaStore.Images.Media.DATA;
            ContentResolver contentResolver = context.getContentResolver();

            // 只查询jpg和png的图片
            Cursor cursor = contentResolver.query(imageUri, new String[]{key_DATA},
                    key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=?",
                    new String[]{"image/jpg", "image/jpeg", "image/png"},
                    MediaStore.Images.Media.DATE_MODIFIED);

            HashMap<String, GAlbum> albumHashMap = null;
            List<GImage> allImageList = null;
            if (cursor != null) {
                if (cursor.moveToLast()) {
                    albumHashMap = new LinkedHashMap<>();
                    allImageList = new ArrayList<>();
                    GAlbum allAlbum = new GAlbum(allImageList, context.getResources().getString(R.string.all));
                    albumHashMap.put("", allAlbum);
                    while (true) {
                        String imagePath = cursor.getString(0);
                        File imageFile = new File(imagePath);
                        File parentFile = imageFile.getParentFile();
                        String parentPath = parentFile.getAbsolutePath();
                        String parentName = parentFile.getName();

                        GImage image = new GImage(imageFile.getName(), Uri.fromFile(imageFile));
                        allImageList.add(image);
                        if (albumHashMap.get(parentPath) == null) {
                            List<GImage> imageList = new ArrayList<>();
                            imageList.add(image);
                            GAlbum album = new GAlbum(imageList, parentName);
                            albumHashMap.put(parentPath, album);
                        } else {
                            albumHashMap.get(parentPath).getImageList().add(image);
                        }
                        if (!cursor.moveToPrevious()) {
                            break;
                        }
                    }
                }
                cursor.close();
            }
            if (albumHashMap != null) {
                subscriber.onNext(new ArrayList<>(albumHashMap.values()));
            } else {
                subscriber.onError(new IllegalStateException("can't get photos"));
            }
            subscriber.onComplete();
        });
    }
}
