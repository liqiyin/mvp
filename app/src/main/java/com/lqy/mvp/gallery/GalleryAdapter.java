package com.lqy.mvp.gallery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.lqy.mvp.R;
import com.lqy.mvp.gallery.model.GImage;
import com.lqy.mvp.library.adapter.BaseRecyclerViewAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryAdapter extends BaseRecyclerViewAdapter<GalleryAdapter.ViewHolder> {
    Context context;
    List<GImage> imageList;
    public GalleryAdapter(Context context, List<GImage> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.list_gallery, parent, false);
        return new ViewHolder(convertView, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GImage gImage = imageList.get(position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        public GImageView image;

        public ViewHolder(View itemView, GalleryAdapter adapter) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> adapter.onItemHolderClick(ViewHolder.this));
        }
    }

    public interface OnGalleryItemClickListener extends AdapterView.OnItemClickListener {
    }
}