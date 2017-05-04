package com.lqy.mvp.gallery;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.lqy.mvp.R;
import com.lqy.mvp.gallery.model.GImage;
import com.lqy.mvp.gallery.model.SelectSpec;
import com.lqy.mvp.gallery.model.SelectionCollection;
import com.lqy.mvp.gallery.widget.CheckView;
import com.lqy.mvp.gallery.widget.GalleryGrid;
import com.lqy.mvp.library.activity.BaseActivity;
import com.lqy.mvp.library.adapter.BaseRecyclerViewAdapter;
import com.lqy.mvp.library.util.SystemUtils;

import java.util.ArrayList;
import java.util.List;

public class GalleryAdapter extends BaseRecyclerViewAdapter<GalleryAdapter.ViewHolder> {
    Context context;
    List<GImage> imageList;
    int mImageResize;
    private SelectionCollection selectionCollection; //管理选中的gimage

    public GalleryAdapter(Context context, List<GImage> imageList) {
        this.context = context;
        this.imageList = imageList;
        selectionCollection = new SelectionCollection();
        selectionCollection.onCreate(null, SelectSpec.getInstance());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        GalleryGrid galleryGrid = new GalleryGrid(context);
        galleryGrid.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return new ViewHolder(galleryGrid, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GImage gImage = imageList.get(position);
        holder.galleryGrid.preBindMedia(new GalleryGrid.PreBindInfo(getImageResize(), R.mipmap.ic_launcher, false, holder));
        holder.galleryGrid.bind(gImage);
        setCheckStatus(gImage, holder.galleryGrid);
        holder.galleryGrid.checkView.setOnClickListener(v -> {
            if (selectionCollection.isSelected(gImage)) {
                holder.galleryGrid.setChecked(false);
                selectionCollection.removeGImage(gImage);
            } else if (!selectionCollection.maxSelectedReached()) {
                holder.galleryGrid.setChecked(true);
                selectionCollection.addGImage(gImage);
            }

            if (onItemClickListener != null && onItemClickListener instanceof OnGalleryGridItemClickListener) {
                ((OnGalleryGridItemClickListener) onItemClickListener).onCheckViewClicked(holder.galleryGrid.checkView, selectionCollection.getSize());
            }
        });

        holder.galleryGrid.image.setOnClickListener(v -> {
            if (onItemClickListener != null && onItemClickListener instanceof OnGalleryGridItemClickListener) {
                ((OnGalleryGridItemClickListener) onItemClickListener).onThumbnailClicked(holder.galleryGrid.image, gImage, selectionCollection);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public GalleryGrid galleryGrid;

        public ViewHolder(View itemView, GalleryAdapter adapter) {
            super(itemView);
            galleryGrid = (GalleryGrid) itemView;
            itemView.setOnClickListener(v -> adapter.onItemHolderClick(ViewHolder.this));
        }
    }

    private int getImageResize() {
        if (mImageResize == 0) {
            mImageResize = SystemUtils.getScreenWidth((BaseActivity) context) / GalleryConfig.GRID_COLUMN / 2;
        }
        return mImageResize;
    }

    public interface OnGalleryGridItemClickListener extends AdapterView.OnItemClickListener {
        void onThumbnailClicked(ImageView thumbnail, GImage item, SelectionCollection selectionCollection);
        void onCheckViewClicked(CheckView checkView, int size);
    }

    private void setCheckStatus(GImage gImage, GalleryGrid galleryGrid) {
        boolean selected = selectionCollection.isSelected(gImage);
        galleryGrid.setChecked(selected);
    }

    public void resetSelection() {
        selectionCollection.reset();
    }

    public void updateSelection(ArrayList<GImage> gImageList) {
        selectionCollection.overwrite(gImageList);
    }

    public ArrayList<GImage> getSelectUriList() {
        return selectionCollection.getGImageList();
    }
}