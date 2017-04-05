package com.lqy.mvp.preference;

/**
 * @author baronzhang (baron[dot]zhanglei[at]gmail[dot]com ==>> baronzhang.com)
 */
public enum SmartSettings {

    /*默认配置项*/
    SETTINGS_FIRST_USE("first_use", Boolean.TRUE);

    private final String mId;
    private final Object mDefaultValue;

    SmartSettings(String id, Object defaultValue) {
        this.mId = id;
        this.mDefaultValue = defaultValue;
    }

    public String getId() {
        return this.mId;
    }

    public Object getDefaultValue() {
        return this.mDefaultValue;
    }

    public static SmartSettings fromId(String id) {
        SmartSettings[] values = values();
        for (SmartSettings value : values) {
            if (value.mId.equals(id)) {
                return value;
            }
        }
        return null;
    }
}
