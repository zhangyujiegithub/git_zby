package com.biaozhunyuan.tianyi.common.model.form;

/**
 * Created by Administrator on 2017/7/12.
 */
public class ReturnDict {
    public String value;  //字典id
    public String text;   //字典名称

    public ReturnDict(String key, String value) {
        this.value = key;
        this.text = value;
    }
}
