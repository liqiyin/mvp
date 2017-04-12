package com.lqy.mvp.logic.pub.activity;

import com.lqy.mvp.CurrentActivityManager;
import com.lqy.mvp.R;
import com.lqy.mvp.library.activity.BaseActivity;
import com.lqy.mvp.library.activity.QrCodeBaseActivity;

import cn.bingoogolapple.qrcode.core.QRCodeView;

/**
 * Created by slam.li on 2017/4/6.
 * 二维码处理界面
 */

public class QrCodeActivity extends QrCodeBaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_qrcode;
    }

    @Override
    protected QRCodeView getQrCodeView() {
        return (QRCodeView) findViewById(R.id.zxingview);
    }

    @Override
    protected BaseActivity getDialogActivity() {
        return CurrentActivityManager.getInstance().getCurrentActivity();
    }
}
