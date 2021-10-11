package com.biaozhunyuan.tianyi.apply.model;

import java.util.List;

/**
 * Created by 王安民 on 2017/10/2.
 * 表单联动
 */

public class LoadRelatedData {
    private String dataSourceTableName;
    private String specialUrl;
    private List<RelatedData> requestFieldMaps;
    private List<RelatedData> resultFieldMaps;

    public String getDataSourceTableName() {
        return dataSourceTableName;
    }

    public void setDataSourceTableName(String dataSourceTableName) {
        this.dataSourceTableName = dataSourceTableName;
    }

    public String getSpecialUrl() {
        return specialUrl;
    }

    public void setSpecialUrl(String specialUrl) {
        this.specialUrl = specialUrl;
    }

    public List<RelatedData> getRequestFieldMaps() {
        return requestFieldMaps;
    }

    public void setRequestFieldMaps(List<RelatedData> requestFieldMaps) {
        this.requestFieldMaps = requestFieldMaps;
    }

    public List<RelatedData> getResultFieldMaps() {
        return resultFieldMaps;
    }

    public void setResultFieldMaps(List<RelatedData> resultFieldMaps) {
        this.resultFieldMaps = resultFieldMaps;
    }
}
