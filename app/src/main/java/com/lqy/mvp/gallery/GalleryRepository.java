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
    private static final Uri QUERY_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    private static final String[] PROJECTION = {
            MediaStore.Images.Media.DATA,
            MediaStore.Files.FileColumns._ID,
            MediaStore.MediaColumns.SIZE
    };

    private static final String SELECTION_ALL =
            "(" + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
            + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
            + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?)"
            + " AND " + MediaStore.MediaColumns.SIZE + ">0";

    private static final String[] SELECTION_ALL_ARGS = {
            "image/jpg", "image/jpeg", "image/png"
    };

    /**
     * 获取系统中的所有图片
     */
    public static Observable<List<GAlbum>> getImageMap(Context context) {
        return Observable.create(subscriber -> {
            ContentResolver contentResolver = context.getContentResolver();

            // 只查询jpg和png的图片
            Cursor cursor = contentResolver.query(QUERY_URI,
                    PROJECTION,
                    SELECTION_ALL,
                    SELECTION_ALL_ARGS,
                    MediaStore.Images.Media.DATE_MODIFIED);

            HashMap<String, GAlbum> albumHashMap = null;
            List<GImage> allImageList = null;
            if (cursor != null) {
                albumHashMap = new LinkedHashMap<>();
                allImageList = new ArrayList<>();
                GAlbum allAlbum = new GAlbum(allImageList, context.getResources().getString(R.string.all));
                albumHashMap.put("", allAlbum);
                if (cursor.moveToLast()) {
                    while (true) {
                        String imagePath = cursor.getString(0);
                        int imageID = cursor.getInt(1);
                        long imageSize = cursor.getLong(2);

                        File imageFile = new File(imagePath);
                        File parentFile = imageFile.getParentFile();
                        String parentPath = parentFile.getAbsolutePath();
                        String parentName = parentFile.getName();
                        if (parentName.equals(GalleryConfig.SMART_CROP_DIR)) {
                            if (!cursor.moveToPrevious()) {
                                break;
                            } else {
                                continue; //过滤剪裁图片
                            }
                        }

                        String uriStr = MediaStore.Images.Media.EXTERNAL_CONTENT_URI + File.separator + imageID;

                        GImage image = new GImage(imageFile.getName(), imagePath, Uri.parse(uriStr));
                        //所有相册中 过滤掉10k以下的图片
                        if (imageSize > 10 * 1024) {
                            allImageList.add(image);
                        }
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
