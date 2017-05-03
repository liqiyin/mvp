package com.lqy.mvp.library.adapter;

import android.support.v7.widget.RecyclerView;
import android.widget.AdapterView;

/**
 * @author baronzhang (baron[dot]zhanglei[at]gmail[dot]com ==>> baronzhang.com)
 *         16/4/15
 *         //slam.li
 *         点击事件的具体实现在view层实现，不在adapter中实现
 *         保证mvp设计模式的代码规范
 */
public abstract class BaseRecyclerViewAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T>{

    protected AdapterView.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {

        this.onItemClickListener = onItemClickListener;
    }

    protected void onItemHolderClick(RecyclerView.ViewHolder itemHolder) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(null, itemHolder.itemView,
                    itemHolder.getAdapterPosition(), itemHolder.getItemId());
        }
    }
}
