package com.lqy.mvp.gallery.model;

import android.os.Bundle;

import com.lqy.mvp.gallery.GalleryConfig;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public final class SelectionCollection {

    private Set<GImage> imageSet;
    SelectSpec selectSpec;

    public SelectionCollection() {}

    public void onCreate(Bundle bundle, SelectSpec selectSpec) {
        if (bundle == null) {
            imageSet = new LinkedHashSet<>();
        } else {
            imageSet = new LinkedHashSet<>(bundle.getParcelableArrayList(GalleryConfig.STATE_SELECTION));
        }
        this.selectSpec = selectSpec;
    }

    public boolean isSelected(GImage image) {
        return imageSet.contains(image);
    }

    public void addGImage(GImage image) {
        imageSet.add(image);
    }

    public void removeGImage(GImage image) {
        imageSet.remove(image);
    }

    public boolean maxSelectedReached() {
        return imageSet.size() >= selectSpec.maxSelectable;
    }

    public int getSize() {
        return imageSet.size();
    }

    public void reset() {
        imageSet.clear();
    }

    public boolean isShowCamera() {
        return selectSpec.showCamera;
    }

    public boolean isShowSelectBtn() {
        return selectSpec.maxSelectable > 1;
    }

    public Bundle getDataWithBundle() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(GalleryConfig.STATE_SELECTION, new ArrayList<>(imageSet));
        return bundle;
    }

    public void overwrite(ArrayList<GImage> gImages) {
        imageSet.clear();
        imageSet.addAll(gImages);
    }

    public ArrayList<GImage> getGImageList() {
        return new ArrayList<>(imageSet);
    }
}
