package com.lqy.mvp.gallery;

/**
 * Created by slam.li on 2017/5/2.
 * 图库设置
 */

public interface GalleryConfig {
    int GRID_COLUMN = 3; //预览图片列
    int GRID_DIVIDER_WIDTH = 10; //间隔
    int MAX_SELECT_SIZE = 5; //最多选择数量

    String EXTRA_ALBUM = "extra_album";
    String EXTRA_ITEM = "extra_item";
    String EXTRA_DEFAULT_BUNDLE = "extra_default_bundle";
    String EXTRA_RESULT_BUNDLE = "extra_result_bundle";
    String EXTRA_RESULT_APPLY = "extra_result_apply";

    String IMAGE_RESULT = "image_result";

    String STATE_SELECTION = "state_selection";

    String SMART_TEMP_DIR = "SmartTmp/";
}
