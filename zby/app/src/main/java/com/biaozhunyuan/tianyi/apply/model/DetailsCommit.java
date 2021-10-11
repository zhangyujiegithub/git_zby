package com.biaozhunyuan.tianyi.apply.model;

import com.biaozhunyuan.tianyi.common.model.form.FormDetails;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 王安民 on 2017/9/14.
 */

public class DetailsCommit implements Serializable {
    private List<CommitFormDetails> detailData;
    private List<FormDetails> detailData2;

    public List<CommitFormDetails> getDetailData() {
        return detailData;
    }

    public void setDetailData(List<CommitFormDetails> detailData) {
        this.detailData = detailData;
    }

    public List<FormDetails> getDetailData2() {
        return detailData2;
    }

    public void setDetailData2(List<FormDetails> detailData2) {
        this.detailData2 = detailData2;
    }
}
