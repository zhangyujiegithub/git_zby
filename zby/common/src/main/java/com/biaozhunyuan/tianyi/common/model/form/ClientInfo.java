package com.biaozhunyuan.tianyi.common.model.form;

import java.util.List;

/**
 * Created by 王安民 on 2017/9/23.
 * 客户详情保存
 */

public class ClientInfo {
    private List<表单字段> jsonData;

    private String type;

    public List<表单字段> getJsonData() {
        return jsonData;
    }

    public void setJsonData(List<表单字段> jsonData) {
        this.jsonData = jsonData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
