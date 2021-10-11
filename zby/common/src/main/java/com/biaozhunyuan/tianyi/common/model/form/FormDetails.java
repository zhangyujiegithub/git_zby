package com.biaozhunyuan.tianyi.common.model.form;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 王安民 on 2017/9/14.
 * 申请表明细，上传实体类
 */

public class FormDetails implements Serializable {
    private String id;
    private String detailName;
    private List<TabCell> fields;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDetailName() {
        return detailName;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName;
    }

    public List<TabCell> getFields() {
        return fields;
    }

    public void setFields(List<TabCell> fields) {
        this.fields = fields;
    }
}
