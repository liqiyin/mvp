package com.lqy.mvp.library.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.lqy.mvp.library.R;
import com.lqy.mvp.library.util.SystemUtils;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by slam.li on 2017/4/7.
 * 二维码基类
 * 模板模式
 */

public abstract class QrCodeBaseActivity extends BaseActivity implements QRCodeView.Delegate {
    private final static int CAMERA_REQUEST = 1;
    QRCodeView mQRCodeView;

    String[] permissions = {Manifest.permission.CAMERA};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mQRCodeView = getQrCodeView();
        mQRCodeView.setDelegate(this);
        requestCameraPermission();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.showScanRect();
        mQRCodeView.startCamera();
        mQRCodeView.startSpot();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mQRCodeView.stopSpot();
        mQRCodeView.stopCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mQRCodeView.onDestroy();
        mQRCodeView.setDelegate(null);
    }

    private void requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int i = ContextCompat.checkSelfPermission(this, permissions[0]);
            boolean canRequest = shouldShowRequestPermissionRationale(permissions[0]);
            if (canRequest) {
                ActivityCompat.requestPermissions(mActivity, permissions, CAMERA_REQUEST);
            } else if (i != PackageManager.PERMISSION_GRANTED) {
                showPermissionDialog();
            }
        }
    }

    abstract protected int getLayoutId();
    abstract protected QRCodeView getQrCodeView();

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != CAMERA_REQUEST || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            showPermissionDialog();
        }
    }

    private void showPermissionDialog() {
        try {
            BaseActivity activity = getDialogActivity();
            MaterialDialog dialog = new MaterialDialog(activity);
            dialog.setTitle(R.string.tip)
                    .setMessage(R.string.qrcode_tip)
                    .setPositiveButton(R.string.go_to_permission, v -> {
                        SystemUtils.jumpToGrantPermission(mActivity);
                        dialog.dismiss();
                    })
                    .setNegativeButton(R.string.cancel, v -> {
                        dialog.dismiss();
                        finish();
                    })
                    .show();
        } catch (Exception e){
            finish();
        }
    }

    abstract protected BaseActivity getDialogActivity();

    protected void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String s) {
        showToast(s);
        vibrate();
        mQRCodeView.startSpot();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        showToast(R.string.fail_to_open_camera);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
