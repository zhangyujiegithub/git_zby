package com.biaozhunyuan.tianyi.apply.model;

import java.util.List;

/**
 * Created by 王安民 on 2017/12/16.
 * 多明细，实体类
 */

public class MuitiDetails {
    private String DetailName;
    private String detailTitle;
    private List<List<CellInfo>> content;
    private int rowCount;

    public String getDetailTitle() {
        return detailTitle;
    }

    public void setDetailTitle(String detailTitle) {
        this.detailTitle = detailTitle;
    }

    public String getDetailName() {
        return DetailName;
    }

    public void setDetailName(String detailName) {
        DetailName = detailName;
    }

    public List<List<CellInfo>> getContent() {
        return content;
    }

    public void setContent(List<List<CellInfo>> content) {
        this.content = content;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }
}
