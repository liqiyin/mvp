package com.lqy.mvp.gallery.model;

import java.util.List;

/**
 * Created by slam.li on 2017/4/26.
 * 相册
 */

public final class GAlbum {
    public List<GImage> imageList;
    public String albumName;

    public int getSize() {
        return imageList.size();
    }

    public GAlbum(List<GImage> imageList, String albumName) {
        this.imageList = imageList;
        this.albumName = albumName;
    }

    public List<GImage> getImageList() {
        return imageList;
    }

    public String getAlbumName() {
        return albumName;
    }
}
