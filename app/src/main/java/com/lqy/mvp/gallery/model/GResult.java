package com.lqy.mvp.gallery.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public final class GResult implements Parcelable {
    public List<GImage> gImageList;

    private GResult(List<GImage> gImageList) {
        this.gImageList = gImageList;
    }

    public static GResult of(List<GImage> gImageList) {
        return new GResult(gImageList);
    }

    public static GResult ofUriList(List<Uri> uriList) {
        List<GImage> gImages = new ArrayList<>();
        for (Uri uri: uriList) {
            gImages.add(new GImage("", "", uri));
        }
        return of(gImages);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.gImageList);
    }

    protected GResult(Parcel in) {
        this.gImageList = in.createTypedArrayList(GImage.CREATOR);
    }

    public static final Creator<GResult> CREATOR = new Creator<GResult>() {
        @Override
        public GResult createFromParcel(Parcel source) {
            return new GResult(source);
        }

        @Override
        public GResult[] newArray(int size) {
            return new GResult[size];
        }
    };

    public boolean isEmpty() {
        return gImageList == null || gImageList.size() == 0;
    }
    
    public GImage getCropImage() {
        if (!isEmpty()) {
            return gImageList.get(0);
        }
        return null;
    }
    
    public List<Uri> asImageUriList() {
        List<Uri> uriList = new ArrayList<>();
        for (GImage gImage: gImageList) {
            uriList.add(gImage.getContentUri());
        }
        return uriList;
    }
}
