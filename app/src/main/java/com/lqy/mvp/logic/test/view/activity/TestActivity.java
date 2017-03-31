package com.lqy.mvp.logic.test.view.activity;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.lqy.mvp.R;
import com.lqy.mvp.library.activity.BaseActivity;
import com.lqy.mvp.library.widget.PullRefreshView;
import com.lqy.mvp.logic.test.contract.TestContract;
import com.lqy.mvp.logic.test.model.http.response.InTheatersResp;
import com.lqy.mvp.logic.test.presenter.TestPresenter;
import com.lqy.mvp.logic.test.view.adapter.TestAdapter;
import com.lqy.mvp.util.ActivityUtils;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;
import com.zhy.m.permission.ShowRequestPermissionRationale;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * 可使用fragment和activity层作为view层 虚实现contract类中定义的接口
 * 注意subscribe()和unsubscribe()一定要调用
 */
public class TestActivity extends BaseActivity implements TestContract.View {

    TestContract.Presenter presenter;
    Unbinder unbinder;
    @BindView(R.id.prv_main)
    PullRefreshView pullRefreshView;
    @BindView(R.id.btn)
    Button btn;

    List<InTheatersResp.SubjectsBean> dataList;
    TestAdapter adapter;
    int pageStart = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        unbinder = ButterKnife.bind(this);
        presenter = new TestPresenter(this);
        presenter.subscribe();
        new Handler().post(() -> {
            pullRefreshView.setRefreshing(true);
            presenter.loadTestContent(pageStart);
        });
        setStatusBarColor(R.color.colorPrimaryDark);
        initView();
    }

    void initView() {
        dataList = new ArrayList<>();
        adapter = new TestAdapter(this, dataList);
        adapter.setOnItemClickListener(new TestAdapter.OnTestManagerItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onIconClick(String title) {
                Toast.makeText(TestActivity.this, title, Toast.LENGTH_SHORT).show();
            }
        });

        pullRefreshView.setOnPullRefreshManageListener(new PullRefreshView.OnPullRefreshManageListener() {
            @Override
            public void onRefresh() {
                pageStart = 0;
                presenter.loadTestContent(pageStart);
            }

            @Override
            public void onLoadMore() {
                presenter.loadTestContent(pageStart);
            }
        });

        pullRefreshView.setAdapter(adapter);

        btn.setOnClickListener(v -> {
            if (!MPermissions.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.READ_CONTACTS, 4)) {
                MPermissions.requestPermissions(mActivity, 4, Manifest.permission.READ_CONTACTS);
            }
        });
    }

    @Override
    public void displayRequestContent(List<InTheatersResp.SubjectsBean> subjectsBeanList) {
        pullRefreshView.onRequestCompleted();
        if (subjectsBeanList == null) return;
        if (pageStart == 0) {
            dataList.clear();
        }
        if (subjectsBeanList.size() > 0) {
            pageStart++;
        } else {
            pullRefreshView.setCanLoadMore(false);
            Toast.makeText(TestActivity.this, "暂无更多内容", Toast.LENGTH_SHORT).show();
            return;
        }
        dataList.addAll(subjectsBeanList);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        presenter.unSubscribe();
    }

    /**
     * 权限相关
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //授权成功
    @PermissionGrant(4)
    public void onPermissionSuccess() {
        showToast("授权");
    }
//
    //授权失败
    @PermissionDenied(4)
    public void onPermissionFail() {
        showToast("拒绝");
    }

    //授权失败之后 若为关键权限则再次提示授权
    @ShowRequestPermissionRationale(4)
    public void onPermissionExplaination() {
        showToast("解释");
        new MaterialDialog(this)
                .setTitle("提醒")
                .setMessage("请授予联系人权限")
                .setPositiveButton("确定", v -> ActivityUtils.jumpToGrantPermission(mActivity))
                .setNegativeButton("取消", v -> {
                    //TODO
                })
                .show();
    }
}
