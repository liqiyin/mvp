package com.lqy.mvp.gallery.model;

import android.net.Uri;
import android.os.Bundle;

import com.lqy.mvp.gallery.GalleryConfig;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class SelectionCollection {

    private Set<GImage> imageSet;

    private int maxSelectSize = GalleryConfig.MAX_SELECT_SIZE;

    public SelectionCollection() {
        this(null);
    }

    public SelectionCollection(Bundle bundle) {
        if (bundle == null) {
            imageSet = new LinkedHashSet<>();
        } else {
            imageSet = new LinkedHashSet<>(bundle.getParcelableArrayList(GalleryConfig.STATE_SELECTION));
        }
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
        return imageSet.size() >= maxSelectSize;
    }

    public int getSize() {
        return imageSet.size();
    }

    public void reset() {
        imageSet.clear();
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

    public ArrayList<Uri> asListOfUri() {
        ArrayList<Uri> uris = new ArrayList<>();
        for (GImage item : imageSet) {
            uris.add(item.getContentUri());
        }
        return uris;
    }
}
