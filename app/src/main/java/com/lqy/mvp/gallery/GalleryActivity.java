package com.lqy.mvp.gallery;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.lqy.mvp.R;
import com.lqy.mvp.gallery.model.GAlbum;
import com.lqy.mvp.gallery.model.GImage;
import com.lqy.mvp.gallery.model.GResult;
import com.lqy.mvp.gallery.model.SelectionCollection;
import com.lqy.mvp.gallery.widget.CheckView;
import com.lqy.mvp.gallery.widget.GalleryGridInset;
import com.lqy.mvp.library.activity.BaseActivity;
import com.lqy.mvp.library.util.SystemUtils;
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

import static com.lqy.mvp.gallery.GalleryConfig.IMAGE_RESULT;

public class GalleryActivity extends BaseActivity implements GalleryContract.View {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    private static final int WRITE_PERMISSION_REQUEST_CODE = 2;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.spinner_album)
    Spinner spinnerAlbum;
    @BindView(R.id.button_apply)
    TextView buttonApply;

    private GalleryAdapter adapter;
    private GalleryPresenter presenter;

    private List<GAlbum> albumList;
    private List<GImage> imageList;

    private Unbinder unbinder;

    GallerySpinnerAdapter spinnerAdapter;

    TakePhoto takePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        unbinder = ButterKnife.bind(this);
        presenter = new GalleryPresenter(this, this);
        initView();
        requestWritePermission();
    }

    private void initView() {
        imageList = new ArrayList<>();
        recyclerView.setLayoutManager(new GridLayoutManager(this, GalleryConfig.GRID_COLUMN));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new GalleryGridInset(GalleryConfig.GRID_COLUMN, GalleryConfig.GRID_DIVIDER_WIDTH));
        adapter = new GalleryAdapter(mActivity, imageList);
        adapter.setOnItemClickListener(new GalleryAdapter.OnGalleryGridItemClickListener() {
            @Override
            public void onThumbnailClicked(ImageView thumbnail, GImage item, SelectionCollection selectionCollection) {
                if (selectionCollection.isShowCamera()) {
                    ArrayList<GImage> list = new ArrayList<>();
                    list.add(item);
                    sendResult(false, GResult.of(list));
                    finish();
                } else {
                    Intent intent = new Intent(mActivity, GalleryPreviewActivity.class);
                    intent.putParcelableArrayListExtra(GalleryConfig.EXTRA_ALBUM, (ArrayList<? extends Parcelable>) imageList);
                    intent.putExtra(GalleryConfig.EXTRA_ITEM, item);
                    intent.putExtra(GalleryConfig.EXTRA_DEFAULT_BUNDLE, selectionCollection.getDataWithBundle());
                    startActivityForResult(intent, TakePhoto.REQUEST_CHOOSE);
                }
            }

            @Override
            public void onCheckViewClicked(CheckView checkView, int selectSize) {
                setSelectedNum(selectSize);
            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) return;
                requestCameraPermission();
            }
        });
        recyclerView.setAdapter(adapter);

        albumList = new ArrayList<>();
        spinnerAdapter = new GallerySpinnerAdapter(mActivity, albumList);
        spinnerAlbum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GAlbum album = albumList.get(position);
                spinnerAdapter.setCurPosition(position);

                imageList.clear();
                imageList.addAll(album.getImageList());
                adapter.resetSelection();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        spinnerAlbum.setAdapter(spinnerAdapter);

        takePhoto = new TakePhotoImpl(mActivity, new TakePhoto.TakeResultListener() {
            @Override
            public void takeSuccess(List<Uri> list) {
                sendResult(false, GResult.ofUriList(list));
                finish();
            }

            @Override
            public void takeFail(String msg) {}

            @Override
            public void takeCancel() {}
        });
    }

    @Override
    protected void onDestroy() {
        presenter.unSubscribe();
        unbinder.unbind();
        super.onDestroy();
    }

    private void requestWritePermission() {
        if (!MPermissions.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_PERMISSION_REQUEST_CODE)) {
            MPermissions.requestPermissions(mActivity, WRITE_PERMISSION_REQUEST_CODE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    private void requestCameraPermission() {
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
        takePhoto.takePhoto();
    }

    @PermissionDenied(CAMERA_PERMISSION_REQUEST_CODE)
    public void onCameraFail() {
        showToast("相机拒绝");
    }

    @ShowRequestPermissionRationale(CAMERA_PERMISSION_REQUEST_CODE)
    public void onCameraExplaination() {
        showToast("解释");
        final MaterialDialog dialog = new MaterialDialog(this);
            dialog.setTitle("提醒")
                .setMessage("请授予相机权限")
                .setPositiveButton("确定", v -> {
                    SystemUtils.jumpToGrantPermission(mActivity);
                    dialog.dismiss();
                })
                .setNegativeButton("取消", v -> dialog.dismiss());
        dialog.show();
    }

    //写权限
    @PermissionGrant(WRITE_PERMISSION_REQUEST_CODE)
    public void onWriteSuccess() {
        presenter.subscribe();
    }

    @PermissionDenied(WRITE_PERMISSION_REQUEST_CODE)
    public void onWriteFail() {
        finish();
    }

    @ShowRequestPermissionRationale(WRITE_PERMISSION_REQUEST_CODE)
    public void onWriteExplaination() {
        showToast("解释");
        final MaterialDialog dialog = new MaterialDialog(this)
                .setTitle("提醒")
                .setMessage("请授予写权限")
                .setPositiveButton("确定", v -> {
                    SystemUtils.jumpToGrantPermission(mActivity);
                    finish();
                })
                .setNegativeButton("取消", v -> finish());
        dialog.show();
    }

    @Override
    public void displayGallery(List<GAlbum> albumList) {
        this.albumList.addAll(albumList);

        GAlbum album = albumList.get(0);
        imageList.addAll(album.getImageList());
        adapter.notifyDataSetChanged();
        spinnerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TakePhoto.REQUEST_CHOOSE:
                if (resultCode != RESULT_OK) return;
                Bundle bundle = data.getBundleExtra(GalleryConfig.EXTRA_RESULT_BUNDLE);
                ArrayList<GImage> stateList = bundle.getParcelableArrayList(GalleryConfig.STATE_SELECTION);
                adapter.updateSelection(stateList);
                adapter.notifyDataSetChanged();
                setSelectedNum(stateList == null ? 0 : stateList.size());
                break;
            default:
                takePhoto.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @OnClick(R.id.button_apply)
    void onButtonApply() {
        sendResult(false, GResult.of(adapter.getSelectGImageList()));
        finish();
    }

    @Override
    public void onBackPressed() {
        sendResult(true, null);
        finish();
    }

    public void sendResult(boolean isCancel, GResult gResult) {
        Intent intent = new Intent();
        intent.putExtra(IMAGE_RESULT, gResult);
        setResult(isCancel ? RESULT_CANCELED : RESULT_OK, intent);
    }

    private void setSelectedNum(int size) {
        buttonApply.setEnabled(size > 0);
        buttonApply.setText(String.format(getString(R.string.apply_holder), size + ""));
    }
}