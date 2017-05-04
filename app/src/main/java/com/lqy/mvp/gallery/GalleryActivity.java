package com.lqy.mvp.gallery;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
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
import com.lqy.mvp.gallery.model.SelectionCollection;
import com.lqy.mvp.gallery.widget.CheckView;
import com.lqy.mvp.gallery.widget.GalleryGridInset;
import com.lqy.mvp.library.activity.BaseActivity;
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

public class GalleryActivity extends BaseActivity implements GalleryContract.View {
    public static final int REQUEST_IMAGE = 1000;
    public static final String RESULT_IMAGE_LIST = "result_image_list";

    private static final int STORAGE_PERMISSION_REQUEST_CODE = 7000; //申请内存读写权限
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        unbinder = ButterKnife.bind(this);
        presenter = new GalleryPresenter(this, this);
        initView();
        requestPermission();
        presenter.subscribe();
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
                Intent intent = new Intent(mActivity, GalleryPreviewActivity.class);
                intent.putParcelableArrayListExtra(GalleryConfig.EXTRA_ALBUM, (ArrayList<? extends Parcelable>) imageList);
                intent.putExtra(GalleryConfig.EXTRA_ITEM, item);
                intent.putExtra(GalleryConfig.EXTRA_DEFAULT_BUNDLE, selectionCollection.getDataWithBundle());
                startActivityForResult(intent, TakePhoto.REQUEST_CHOOSE);
            }

            @Override
            public void onCheckViewClicked(CheckView checkView, int selectSize) {
                setSelectedNum(selectSize);
            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {}
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
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerAlbum.setAdapter(spinnerAdapter);
    }

    @Override
    protected void onDestroy() {
        presenter.unSubscribe();
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case TakePhoto.REQUEST_CHOOSE:
                Bundle bundle = data.getBundleExtra(GalleryConfig.EXTRA_RESULT_BUNDLE);
                ArrayList<GImage> stateList = bundle.getParcelableArrayList(GalleryConfig.STATE_SELECTION);
                adapter.updateSelection(stateList);
                adapter.notifyDataSetChanged();
                setSelectedNum(stateList == null ? 0 : stateList.size());
                break;
        }
    }

    @OnClick(R.id.button_apply)
    void onButtonApply() {
        sendResult(false);
        finish();
    }

    @Override
    public void onBackPressed() {
        sendResult(true);
        finish();
    }

    public void sendResult(boolean isCancel) {
        Intent intent = new Intent();
        if (!isCancel) {
            intent.putParcelableArrayListExtra(RESULT_IMAGE_LIST, adapter.getSelectUriList());
        }
        setResult(RESULT_OK, intent);
    }

    private void setSelectedNum(int size) {
        buttonApply.setEnabled(size > 0);
        buttonApply.setText(String.format(getString(R.string.apply_holder), size + ""));
    }
}