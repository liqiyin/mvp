package com.lqy.mvp.library.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.lqy.mvp.library.R;
import com.lqy.mvp.library.adapter.BaseRecyclerViewAdapter;

/**
 * Created by slam.li on 2017/3/27.
 * 自定义刷新加载控件
 */

public class PullRefreshView extends FrameLayout {
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;
    OnPullRefreshManageListener onPullRefreshManageListener;
    boolean canLoadMore = true;    //是否能够加载更多
    boolean isRequesting = false; //是否正在请求

    public PullRefreshView(Context context) {
        super(context);
        init();
    }

    public PullRefreshView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PullRefreshView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View refreshView = View.inflate(getContext(), R.layout.layout_refresh_view, null);
        addView(refreshView);
        recyclerView = (RecyclerView) refreshView.findViewById(R.id.recycler_view);
        refreshLayout = (SwipeRefreshLayout) refreshView.findViewById(R.id.refresh_layout);

        refreshLayout.setOnRefreshListener(() -> {
            if (onPullRefreshManageListener != null) {
                canLoadMore = true;
                onPullRefreshManageListener.onRefresh();
                isRequesting = true;
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                    if (onPullRefreshManageListener != null
                            && newState == RecyclerView.SCROLL_STATE_IDLE
                            && canLoadMore
                            && !isRequesting
                            && !isRefreshing()
                            && (linearLayoutManager.findLastVisibleItemPosition() == recyclerView.getAdapter().getItemCount() - 1)) {
                        onPullRefreshManageListener.onLoadMore();
                        isRequesting = true;
                    }
                }
            }
        });

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void setOnPullRefreshManageListener(OnPullRefreshManageListener listener) {
        this.onPullRefreshManageListener = listener;
    }

    public interface OnPullRefreshManageListener {
        void onRefresh();
        void onLoadMore();
    }

    public void setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
    }

    public boolean isRefreshing() {
        return refreshLayout.isRefreshing();
    }

    public void setAdapter(BaseRecyclerViewAdapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    public void setRefreshing(boolean refreshing) {
        refreshLayout.setRefreshing(refreshing);
    }

    /**
     * 数据请求完成后调用
     */
    public void onRequestCompleted() {
        refreshLayout.setRefreshing(false);
        isRequesting = false;
    }

    /**
     * 配置recycleView
     * @param layoutManager 布局管理类
     * @param prefetchCount 一个页面最少显示的item数量
     */
    public void config(RecyclerView.LayoutManager layoutManager, int prefetchCount) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            layoutManager.setItemPrefetchEnabled(true);
            if (layoutManager instanceof LinearLayoutManager) {
                ((LinearLayoutManager)layoutManager).setInitialPrefetchItemCount(prefetchCount);
            }
        } else {
            layoutManager.setItemPrefetchEnabled(false);
        }
        recyclerView.setLayoutManager(layoutManager);
    }
}
