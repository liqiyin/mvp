package com.lqy.mvp.logic.pub.activity;

import android.Manifest;
import android.os.Bundle;
import android.os.Vibrator;

import com.lqy.mvp.R;
import com.lqy.mvp.library.activity.BaseActivity;
import com.lqy.mvp.library.util.SystemUtils;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;
import com.zhy.m.permission.ShowRequestPermissionRationale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by slam.li on 2017/4/6.
 * 二维码处理界面
 */

public class QrCodeActivity extends BaseActivity implements QRCodeView.Delegate {
    @BindView(R.id.zxingview)
    QRCodeView mQRCodeView;

    //申请相机权限
    public static final int CAMEAR_PERMISSION_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        ButterKnife.bind(this);
        mQRCodeView.setDelegate(this);
        requestCameraPermission();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.startCamera();
        mQRCodeView.showScanRect();
        mQRCodeView.startSpot();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mQRCodeView.stopCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mQRCodeView.onDestroy();
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
    @PermissionGrant(CAMEAR_PERMISSION_REQUEST)
    public void onPermissionSuccess() {
    }

    //授权失败
    @PermissionDenied(CAMEAR_PERMISSION_REQUEST)
    public void onPermissionFail() {
        showToast("请先打开相机权限");
        finish();
    }

    //授权失败之后 若为关键权限则再次提示授权
    @ShowRequestPermissionRationale(CAMEAR_PERMISSION_REQUEST)
    public void onPermissionExplaination() {
        MaterialDialog dialog = new MaterialDialog(this);
        dialog.setTitle("提醒")
                .setMessage("请授予相机权限，否则无法使用扫描二维码功能")
                .setPositiveButton("确定", v -> SystemUtils.jumpToGrantPermission(mActivity))
                .setNegativeButton("取消", v -> {
                    dialog.dismiss();
                    finish();
                })
                .show();
    }

    private void requestCameraPermission() {
        if (!MPermissions.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.CAMERA, CAMEAR_PERMISSION_REQUEST)) {
            MPermissions.requestPermissions(mActivity, CAMEAR_PERMISSION_REQUEST, Manifest.permission.CAMERA);
        }
    }

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
        finish();
    }
}
