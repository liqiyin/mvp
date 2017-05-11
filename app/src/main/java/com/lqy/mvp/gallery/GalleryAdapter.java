package com.lqy.mvp.gallery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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

public class GalleryAdapter extends BaseRecyclerViewAdapter<RecyclerView.ViewHolder> {
    private static final int ITEM_CAMERA = 0;
    private static final int ITEM_PHOTO = 1;
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == ITEM_CAMERA) {
            viewHolder = new CameraHolder(LayoutInflater.from(context).inflate(R.layout.gallery_grid_camera, parent, false), this);
        } else {
            GalleryGrid galleryGrid = new GalleryGrid(context);
            galleryGrid.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            viewHolder = new ViewHolder(galleryGrid);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) viewHolder;
            GImage gImage = imageList.get(selectionCollection.isShowCamera() ? position - 1 : position);
            holder.galleryGrid.preBindMedia(new GalleryGrid.PreBindInfo(getImageResize(), R.mipmap.ic_launcher, false, holder, selectionCollection.isShowSelectBtn()));
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
    }

    @Override
    public int getItemViewType(int position) {
        return selectionCollection.isShowCamera() && position == 0 ? ITEM_CAMERA : ITEM_PHOTO;
    }

    @Override
    public int getItemCount() {
        return selectionCollection.isShowCamera() ? imageList.size() + 1 : imageList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public GalleryGrid galleryGrid;

        public ViewHolder(View itemView) {
            super(itemView);
            galleryGrid = (GalleryGrid) itemView;
        }
    }

    static class CameraHolder extends RecyclerView.ViewHolder {
        public CameraHolder(View itemView, GalleryAdapter adapter) {
            super(itemView);
            itemView.setOnClickListener(v -> adapter.onItemHolderClick(CameraHolder.this));
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

    public ArrayList<GImage> getSelectGImageList() {
        return selectionCollection.getGImageList();
    }
}