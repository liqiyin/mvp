package com.lqy.mvp.logic.test.view.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.lqy.mvp.R;
import com.lqy.mvp.gallery.GalleryActivity;
import com.lqy.mvp.library.activity.BaseActivity;
import com.lqy.mvp.library.util.SystemUtils;
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
import butterknife.OnClick;
import butterknife.Unbinder;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * 可使用fragment和activity层作为view层 虚实现contract类中定义的接口
 * 注意subscribe()和unsubscribe()一定要调用
 */
public class TestActivity extends BaseActivity implements TestContract.View {
    private static final int CONTACT_PERMISSION_REQUEST_CODE = 1;

    TestContract.Presenter presenter;
    Unbinder unbinder;
    @BindView(R.id.prv_main)
    PullRefreshView pullRefreshView;
    @BindView(R.id.tv_channel)
    TextView tvChannel;
    @BindView(R.id.gallery_result)
    TextView galleryResult;

    List<InTheatersResp.SubjectsBean> dataList;
    TestAdapter adapter;
    int pageStart = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        unbinder = ButterKnife.bind(this);
        initView();
        presenter = new TestPresenter(this);
        presenter.subscribe();
        new Handler().post(() -> {
            pullRefreshView.setRefreshing(true);
            presenter.loadTestContent(pageStart);
        });
        setStatusBarColor(R.color.colorPrimaryDark);
    }

    void initView() {
        dataList = new ArrayList<>();
        adapter = new TestAdapter(this, dataList);
        pullRefreshView.config(new LinearLayoutManager(context), 9);
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
        tvChannel.setText(SystemUtils.getQudao(mActivity));
    }

    @OnClick(R.id.btn)
    void onPermissionClick() {
        if (!MPermissions.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.READ_CONTACTS, 4)) {
            MPermissions.requestPermissions(mActivity, CONTACT_PERMISSION_REQUEST_CODE, Manifest.permission.READ_CONTACTS);
        }
    }

    @OnClick(R.id.btn_patch)
    void onPatchClick() {
        showToast("热修复");
    }

    @OnClick(R.id.btn_scan)
    void onScanClick() {
        ActivityUtils.jumpToQrCodeActivity(mActivity);
    }

    @OnClick(R.id.btn_gallery)
    void onGalleryClick() {
        ActivityUtils.jumpToGalleryActivity(mActivity);
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
        adapter.stopAdAutoScroll();
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
    @PermissionGrant(CONTACT_PERMISSION_REQUEST_CODE)
    public void onPermissionSuccess() {
        showToast("授权");
    }

    //
    //授权失败
    @PermissionDenied(CONTACT_PERMISSION_REQUEST_CODE)
    public void onPermissionFail() {
        showToast("拒绝");
    }

    //授权失败之后 若为关键权限则再次提示授权
    @ShowRequestPermissionRationale(CONTACT_PERMISSION_REQUEST_CODE)
    public void onPermissionExplaination() {
        showToast("解释");
        new MaterialDialog(this)
                .setTitle("提醒")
                .setMessage("请授予联系人权限")
                .setPositiveButton("确定", v -> SystemUtils.jumpToGrantPermission(mActivity))
                .setNegativeButton("取消", v -> {
                    //TODO
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case GalleryActivity.REQUEST_IMAGE:
                ArrayList<Uri> uriArrayList = data.getParcelableArrayListExtra(GalleryActivity.RESULT_IMAGE_LIST);
                StringBuilder builder = new StringBuilder();
                for (Uri uri: uriArrayList) {
                    builder.append(uri.toString()+"\n");
                }
                galleryResult.setText(builder.toString());
                break;
        }
    }
}
