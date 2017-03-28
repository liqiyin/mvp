package com.lb.duoduo.logic.test.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lb.duoduo.R;
import com.lb.duoduo.logic.test.model.http.response.InTheatersResp;
import com.lb.duouo.library.adapter.BaseRecyclerViewAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by slam.li on 2017/3/27.
 * recycleview写法Demo
 * holder中每个按钮的点击事件 通过接口回调交由view层处理
 * 遵守mvp编码规范
 */

public class TestAdapter extends BaseRecyclerViewAdapter<TestAdapter.ViewHolder> {
    List<InTheatersResp.SubjectsBean> dataList;
    Context context;

    public TestAdapter(Context context, List<InTheatersResp.SubjectsBean> dataList) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public TestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(TestAdapter.ViewHolder holder, int position) {
        InTheatersResp.SubjectsBean subject = dataList.get(position);
        holder.imageIcon.setImageURI(Uri.parse(subject.images.large));
        holder.textTitle.setText(subject.title);

        holder.imageIcon.setOnClickListener(v -> {
            InTheatersResp.SubjectsBean bean = dataList.get(holder.getAdapterPosition());
            dataList.remove(bean);
            notifyItemRemoved(holder.getAdapterPosition());
            if (onItemClickListener != null && onItemClickListener instanceof OnTestManagerItemClickListener) {
                ((OnTestManagerItemClickListener) onItemClickListener).onIconClick(subject.title);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_icon)
        public SimpleDraweeView imageIcon;
        @BindView(R.id.text_title)
        public TextView textTitle;

        ViewHolder(View view, TestAdapter adapter) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(v -> adapter.onItemHolderClick(ViewHolder.this));
        }
    }

    public interface OnTestManagerItemClickListener extends AdapterView.OnItemClickListener {
        void onIconClick(String title);
    }
}
