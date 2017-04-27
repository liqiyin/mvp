package com.lqy.mvp.gallery;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lqy.mvp.R;
import com.lqy.mvp.library.activity.BaseActivity;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;
import com.zhy.m.permission.ShowRequestPermissionRationale;

import java.util.ArrayList;

public class GalleryActivity extends BaseActivity implements GalleryContract.View {
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 1;
    private RecyclerView recyclerView;
    private GalleryAdapter adapter;
    private GalleryPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        presenter = new GalleryPresenter(this, this);
        initView();
        requestPermission();
        presenter.subscribe();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new GalleryAdapter(mActivity, new ArrayList<>());
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void requestPermission() {
        if (!MPermissions.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_REQUEST_CODE)) {
            MPermissions.requestPermissions(mActivity, STORAGE_PERMISSION_REQUEST_CODE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    @PermissionGrant(STORAGE_PERMISSION_REQUEST_CODE)
    public void onPermissionSuccess() {
        showToast("授权");
    }

    @PermissionDenied(STORAGE_PERMISSION_REQUEST_CODE)
    public void onPermissionFail() {
        showToast("拒绝");
    }

    @ShowRequestPermissionRationale(STORAGE_PERMISSION_REQUEST_CODE)
    public void onPermissionExplaination() {
    }

    @Override
    public void displayGallery() {

    }
}