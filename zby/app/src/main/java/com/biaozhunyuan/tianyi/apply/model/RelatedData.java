package com.biaozhunyuan.tianyi.apply.model;

/**
 * Created by 王安民 on 2017/10/2.
 */

public class RelatedData {
    private String vSheetFieldName;
    private String dataSourceFieldName;
    private String value;

    public String getvSheetFieldName() {
        return vSheetFieldName;
    }

    public void setvSheetFieldName(String vSheetFieldName) {
        this.vSheetFieldName = vSheetFieldName;
    }

    public String getDataSourceFieldName() {
        return dataSourceFieldName;
    }

    public void setDataSourceFieldName(String dataSourceFieldName) {
        this.dataSourceFieldName = dataSourceFieldName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
