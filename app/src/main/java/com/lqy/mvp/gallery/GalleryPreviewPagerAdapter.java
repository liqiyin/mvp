package com.lqy.mvp.gallery;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.lqy.mvp.gallery.model.GImage;

import java.util.List;

public class GalleryPreviewPagerAdapter extends FragmentPagerAdapter {
    private List<GImage> images;

    public GalleryPreviewPagerAdapter(FragmentManager fm, List<GImage> gImages) {
        super(fm);
        this.images = gImages;
    }

    @Override
    public Fragment getItem(int position) {
        return GalleryPreviewItemFragment.newInstance(images.get(position));
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
    }
}
