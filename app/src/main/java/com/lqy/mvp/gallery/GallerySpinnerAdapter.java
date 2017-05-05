package com.lqy.mvp.gallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lqy.mvp.R;
import com.lqy.mvp.gallery.model.GAlbum;
import com.lqy.mvp.gallery.widget.CheckView;
import com.lqy.mvp.library.util.CommonUtils;
import com.lqy.mvp.util.PicassoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by slam.li on 2017/5/2.
 * 选择相册
 */

public class GallerySpinnerAdapter extends BaseAdapter {
    Context context;
    List<GAlbum> albumList;

    int curPosition = 0;

    public GallerySpinnerAdapter(Context context, List<GAlbum> albumList) {
        this.context = context;
        this.albumList = albumList;
    }

    @Override
    public int getCount() {
        return albumList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        GAlbum album = albumList.get(position);
        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        tv.setText(album.albumName);
        tv.setTextSize(15);
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.gallery_album_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GAlbum album = albumList.get(position);
        int resize = CommonUtils.dp2px(context, 60);
        PicassoUtils.loadSquareThumbnailInList(resize, holder.imageAlbum, album.getImageList().size() == 0 ? null : album.getImageList().get(0).getContentUri(), R.mipmap.ic_launcher_round);
        holder.textAlbum.setText(album.getAlbumName());
        holder.checkView.setChecked(position == curPosition);

        return convertView;
    }

    public static class ViewHolder {
        @BindView(R.id.image_album)
        ImageView imageAlbum;
        @BindView(R.id.text_album)
        TextView textAlbum;
        @BindView(R.id.check_view)
        CheckView checkView;

        public View itemView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            this.itemView = view;
        }
    }

    public void setCurPosition(int position) {
        if (curPosition == position) return;
        curPosition = position;
        notifyDataSetChanged();
    }
}
