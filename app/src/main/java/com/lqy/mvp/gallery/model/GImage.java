package com.lqy.mvp.gallery.model;

/**
 * Created by slam.li on 2017/4/26.
 * 单张图片
 */

public class GImage {
    private String imageName;
    private String absolutePath;

    public GImage(String imageName, String absolutePath) {
        this.imageName = imageName;
        this.absolutePath = absolutePath;
    }
}
