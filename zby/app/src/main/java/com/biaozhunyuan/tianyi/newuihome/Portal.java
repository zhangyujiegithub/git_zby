package com.biaozhunyuan.tianyi.newuihome;

import com.biaozhunyuan.tianyi.common.global.GlobalModel;

/**
 * Created by wangAnMin on 2019/4/11.
 * 首页看板实体类
 */

public class Portal extends GlobalModel{
    private String title;//看板名称
    private String uri;//看板要显示的内容的URL
    private String moreUri;//看板点击查看更多内容的URL
    private String sortId;//看板可根据这个属性来排序
    private boolean check;//看板是否显示在首页（是否选中）

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMoreUri() {
        return moreUri;
    }

    public void setMoreUri(String moreUri) {
        this.moreUri = moreUri;
    }

    public String getSortId() {
        return sortId;
    }

    public void setSortId(String sortId) {
        this.sortId = sortId;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
