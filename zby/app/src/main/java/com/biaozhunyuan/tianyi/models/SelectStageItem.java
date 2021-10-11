package com.biaozhunyuan.tianyi.models;

import com.biaozhunyuan.tianyi.common.model.字典;

public class SelectStageItem extends 字典 {
    private boolean isSelect = false;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
