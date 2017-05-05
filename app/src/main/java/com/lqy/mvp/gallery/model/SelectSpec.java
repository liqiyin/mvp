package com.lqy.mvp.gallery.model;

public final class SelectSpec {
    /**
     * 管理参数
     */
    private static volatile SelectSpec selectSpec;
    private SelectSpec(){}
    public static SelectSpec getInstance() {
        if (selectSpec == null) {
            synchronized (SelectSpec.class) {
                if (selectSpec == null) {
                    selectSpec = new SelectSpec();
                }
            }
        }
        return selectSpec;
    }

    public static SelectSpec getCleanInstance() {
        SelectSpec selectSpec = getInstance();
        selectSpec.reset();
        return selectSpec;
    }

    public int maxSelectable; //最大选择的图片数量
    public boolean showCamera;  //是否显示拍照按钮
    private void reset() {
        maxSelectable = 0;
        showCamera = false;
    }
}
