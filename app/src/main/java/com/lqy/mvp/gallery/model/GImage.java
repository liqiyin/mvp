package com.lqy.mvp.gallery.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by slam.li on 2017/4/26.
 * 单张图片
 */

public class GImage implements Parcelable {
    private String imageName;

    public String getImagePath() {
        return imagePath;
    }

    private String imagePath;

    public Uri getContentUri() {
        return uri;
    }

    private Uri uri;

    public GImage(String imageName, String imagePath, Uri uri) {
        this.imageName = imageName;
        this.imagePath = imagePath;
        this.uri = uri;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GImage)) {
            return false;
        }

        GImage other = (GImage) obj;
        return other.uri.equals(uri);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + uri.hashCode();
        result = 31 * result + imageName.hashCode();
        result = 31 * result + imagePath.hashCode();
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageName);
        dest.writeString(this.imagePath);
        dest.writeParcelable(this.uri, flags);
    }

    protected GImage(Parcel in) {
        this.imageName = in.readString();
        this.imagePath = in.readString();
        this.uri = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Creator<GImage> CREATOR = new Creator<GImage>() {
        @Override
        public GImage createFromParcel(Parcel source) {
            return new GImage(source);
        }

        @Override
        public GImage[] newArray(int size) {
            return new GImage[size];
        }
    };
}
