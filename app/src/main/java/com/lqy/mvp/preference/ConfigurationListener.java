package com.lqy.mvp.preference;

/**
 * Created by slam.li on 2017/3/20.
 * 配置文件改变监听
 */
public interface ConfigurationListener {
    void onConfigurationChanged(SmartSettings pref, Object newValue);
}
