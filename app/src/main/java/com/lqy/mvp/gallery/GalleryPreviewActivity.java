package com.lqy.mvp.gallery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.lqy.mvp.R;
import com.lqy.mvp.gallery.model.GImage;
import com.lqy.mvp.gallery.model.SelectionCollection;
import com.lqy.mvp.gallery.widget.CheckView;
import com.lqy.mvp.gallery.widget.PreviewViewPager;
import com.lqy.mvp.library.activity.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lqy.mvp.gallery.GalleryConfig.EXTRA_RESULT_BUNDLE;

/**
 * Created by slam.li on 2017/5/3.
 */

public class GalleryPreviewActivity extends BaseActivity {
    @BindView(R.id.vp_preview)
    PreviewViewPager vpPreview;
    @BindView(R.id.check_view)
    CheckView checkView;
    @BindView(R.id.button_apply)
    TextView buttonApply;

    SelectionCollection selectionCollection;
    List<GImage> gImageList;

    GImage curGImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_preview);
        ButterKnife.bind(this);

        initData();
        initView();
    }

    private void initData() {
        selectionCollection = new SelectionCollection(getIntent().getBundleExtra(GalleryConfig.EXTRA_DEFAULT_BUNDLE));
    }

    private void initView() {
        gImageList = getIntent().getParcelableArrayListExtra(GalleryConfig.EXTRA_ALBUM);
        curGImage = getIntent().getParcelableExtra(GalleryConfig.EXTRA_ITEM);
        int index = gImageList.indexOf(curGImage);
        GalleryPreviewPagerAdapter previewPagerAdapter = new GalleryPreviewPagerAdapter(getSupportFragmentManager(), gImageList);
        vpPreview.setAdapter(previewPagerAdapter);
        vpPreview.setCurrentItem(index, false);

        vpPreview.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                curGImage = gImageList.get(position);
                setCheckViewStatus();
                setSelectedNum();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        setCheckViewStatus();
        setSelectedNum();
    }

    private void setCheckViewStatus() {
        checkView.setChecked(selectionCollection.isSelected(curGImage));
    }

    private void setSelectedNum() {
        buttonApply.setEnabled(selectionCollection.getSize() > 0);
        buttonApply.setText(String.format(getString(R.string.apply_holder), selectionCollection.getSize()+""));
    }

    @Override
    public void onBackPressed() {
        onBackClick();
    }

    @OnClick(R.id.button_back)
    void onBackClick() {
        sendBackResult();
        finish();
    }

    @OnClick(R.id.check_view)
    void onCheckViewClick() {
        if (selectionCollection.isSelected(curGImage)) {
            selectionCollection.removeGImage(curGImage);
            checkView.setChecked(false);
        } else if (!selectionCollection.maxSelectedReached()) {
            selectionCollection.addGImage(curGImage);
            checkView.setChecked(true);
        }
        setSelectedNum();
    }

    @OnClick(R.id.button_apply)
    void onApplyClick() {
        sendBackResult();
        finish();
    }

    private void sendBackResult() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_RESULT_BUNDLE, selectionCollection.getDataWithBundle());
        setResult(Activity.RESULT_OK, intent);
    }
}
