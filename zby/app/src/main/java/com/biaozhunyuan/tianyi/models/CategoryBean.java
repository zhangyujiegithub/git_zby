package com.biaozhunyuan.tianyi.models;

import java.io.Serializable;
import java.util.List;


public class CategoryBean implements Serializable {

    String iconName;
    int icon;
    int viewId;

    public CategoryBean(String iconName, int icon, int viewId) {
        this.iconName = iconName;
        this.icon = icon;
        this.viewId = viewId;
    }

    public int getViewId() {
        return viewId;
    }

    public String getIconName() {
        return iconName;
    }

    public int getIcon() {
        return icon;
    }

}
