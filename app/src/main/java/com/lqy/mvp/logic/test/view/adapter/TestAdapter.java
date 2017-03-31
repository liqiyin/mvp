package com.lqy.mvp.logic.test.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lqy.mvp.R;
import com.lqy.mvp.library.adapter.BaseRecyclerViewAdapter;
import com.lqy.mvp.logic.test.model.http.response.InTheatersResp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

/**
 * Created by slam.li on 2017/3/27.
 * recycleview写法Demo
 * holder中每个按钮的点击事件 通过接口回调交由view层处理
 * 遵守mvp编码规范
 */

public class TestAdapter extends BaseRecyclerViewAdapter<RecyclerView.ViewHolder> {
    private final int ITEM_TYPE_AD = 0;
    private final int ITEM_TYPE_NORMAL = 1;
    List<InTheatersResp.SubjectsBean> dataList;
    Context context;

    public TestAdapter(Context context, List<InTheatersResp.SubjectsBean> dataList) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        if (viewType == ITEM_TYPE_AD) {
            itemView = LayoutInflater.from(context).inflate(R.layout.list_item_ad, parent, false);
            return new AdViewHolder(itemView);
        } else {
            itemView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
            return new NormalViewHolder(itemView, this);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AdViewHolder) {
            AdViewHolder adHolder = (AdViewHolder) holder;
            if (adHolder.viewPager.getAdapter() == null) {
                List<Integer> list = new ArrayList<>();
                list.add(R.mipmap.ic_launcher);
                list.add(R.mipmap.ic_launcher_round);
                adHolder.viewPager.setAdapter(new TestAdAdapter(context, list).setInfiniteLoop(true));
                adHolder.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

                adHolder.viewPager.setInterval(5000);
                adHolder.viewPager.startAutoScroll();
                adHolder.viewPager.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % list.size());
                adHolder.viewPager.setStopScrollWhenTouch(true);
                adHolder.viewPager.setScrollDurationFactor(4);
            }
        }

        if (holder instanceof NormalViewHolder) {
            NormalViewHolder normalHolder = (NormalViewHolder) holder;
            InTheatersResp.SubjectsBean subject = dataList.get(position - 1);
            normalHolder.imageIcon.setImageURI(Uri.parse(subject.images.large));
            normalHolder.textTitle.setText(subject.title);

            normalHolder.imageIcon.setOnClickListener(v -> {
                InTheatersResp.SubjectsBean bean = dataList.get(holder.getAdapterPosition());
                dataList.remove(bean);
                notifyItemRemoved(holder.getAdapterPosition());
                if (onItemClickListener != null && onItemClickListener instanceof OnTestManagerItemClickListener) {
                    ((OnTestManagerItemClickListener) onItemClickListener).onIconClick(subject.title);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? ITEM_TYPE_AD : ITEM_TYPE_NORMAL;
    }

    static class NormalViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_icon)
        public SimpleDraweeView imageIcon;
        @BindView(R.id.text_title)
        public TextView textTitle;


        NormalViewHolder(View view, TestAdapter adapter) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(v -> adapter.onItemHolderClick(NormalViewHolder.this));
        }
    }

    static class AdViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.vp)
        public AutoScrollViewPager viewPager;

        public AdViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnTestManagerItemClickListener extends AdapterView.OnItemClickListener {
        void onIconClick(String title);
    }
}
