package com.biaozhunyuan.tianyi.supportAndComment;

import java.io.Serializable;

/**
 * Created by wangAnMin on 2018/3/6.
 * 点赞评论上传的实体类
 */

public class SupportListPost implements Serializable {
    private String dataType; //数据类型
    private String dataId; //数据编号


    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }
}
