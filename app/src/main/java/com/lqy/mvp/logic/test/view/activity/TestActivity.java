package com.lqy.mvp.logic.test.view.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lqy.mvp.R;
import com.lqy.mvp.gallery.TakePhoto;
import com.lqy.mvp.gallery.TakePhotoImpl;
import com.lqy.mvp.library.activity.BaseActivity;
import com.lqy.mvp.library.util.SystemUtils;
import com.lqy.mvp.library.widget.PullRefreshView;
import com.lqy.mvp.logic.test.contract.TestContract;
import com.lqy.mvp.logic.test.model.http.response.InTheatersResp;
import com.lqy.mvp.logic.test.presenter.TestPresenter;
import com.lqy.mvp.logic.test.view.adapter.TestAdapter;
import com.lqy.mvp.util.ActivityUtils;
import com.lqy.mvp.util.PicassoUtils;
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
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;

    TestContract.Presenter presenter;
    Unbinder unbinder;
    @BindView(R.id.prv_main)
    PullRefreshView pullRefreshView;
    @BindView(R.id.tv_channel)
    TextView tvChannel;
    @BindView(R.id.image_test)
    ImageView imageTest;

    List<InTheatersResp.SubjectsBean> dataList;
    TestAdapter adapter;
    int pageStart = 0;
    TakePhoto takePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        unbinder = ButterKnife.bind(this);
        initView();

        //必须调用的p层逻辑
        presenter = new TestPresenter(this);
        presenter.subscribe();

        //刷新状态
        new Handler().post(() -> {
            pullRefreshView.setRefreshing(true);
            presenter.loadTestContent(pageStart);
        });

        //设置沉浸式状态栏
        setStatusBarColor(R.color.colorPrimaryDark);
    }

    void initView() {
        //下拉刷新相关
        dataList = new ArrayList<>();
        adapter = new TestAdapter(this, dataList);
        pullRefreshView.config(new LinearLayoutManager(context), 9);
        adapter.setOnItemClickListener(new TestAdapter.OnTestManagerItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {}

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

        //渠道
        tvChannel.setText(SystemUtils.getQudao(mActivity));

        //图片相关
        takePhoto = new TakePhotoImpl(mActivity, new TakePhoto.TakeResultListener() {
            @Override
            public void takeSuccess(List<Uri> list) {
                PicassoUtils.loadNormalImage(imageTest, list.get(0), R.mipmap.ic_launcher);
            }

            @Override
            public void takeFail(String msg) {
            }

            @Override
            public void takeCancel() {
            }
        });
    }

    @OnClick(R.id.btn)
    void onPermissionClick() {
        requestPermission();
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
        takePhoto.choosePhoto(5, false);
    }

    @OnClick(R.id.btn_takephoto)
    void onTakePhotoClick() {
        takePhoto.takePhoto();
    }

    @OnClick(R.id.btn_cropphoto)
    void onCropPhotoClick() {
        takePhoto.chooseOnePhotoAndCrop();
    }

    //请求数据之后回调
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        takePhoto.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        takePhoto.onRestoreInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * 权限相关
     */
    private void requestPermission() {
        if (!MPermissions.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.CAMERA, CAMERA_PERMISSION_REQUEST_CODE)) {
            MPermissions.requestPermissions(mActivity, CAMERA_PERMISSION_REQUEST_CODE, Manifest.permission.CAMERA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //相机权限
    @PermissionGrant(CAMERA_PERMISSION_REQUEST_CODE)
    public void onCameraSuccess() {
        showToast("相机授权");
    }

    @PermissionDenied(CAMERA_PERMISSION_REQUEST_CODE)
    public void onCameraFail() {
        showToast("相机拒绝");
    }

    @ShowRequestPermissionRationale(CAMERA_PERMISSION_REQUEST_CODE)
    public void onCameraExplaination() {
        showToast("解释");
        new MaterialDialog(this)
                .setTitle("提醒")
                .setMessage("请授予相机权限")
                .setPositiveButton("确定", v -> SystemUtils.jumpToGrantPermission(mActivity))
                .setNegativeButton("取消", v -> {})
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //交由takephoto处理图片相关的回调
        takePhoto.onActivityResult(requestCode, resultCode, data);
    }
}