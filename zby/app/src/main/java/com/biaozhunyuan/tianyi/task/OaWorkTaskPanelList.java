package com.biaozhunyuan.tianyi.task;

import java.io.Serializable;

/**
 * 看板列表 条目实体类
 */

public class OaWorkTaskPanelList implements Serializable{

    private String classifyId;
    private String content;
    private String createrTim;
    private String creator;
    private String panelStyle;
    private String title;
    private String uuid;

    public OaWorkTaskPanelList(String content, String title) {
        this.content = content;
        this.title = title;
    }

    public String getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(String classifyId) {
        this.classifyId = classifyId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreaterTim() {
        return createrTim;
    }

    public void setCreaterTim(String createrTim) {
        this.createrTim = createrTim;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getPanelStyle() {
        return panelStyle;
    }

    public void setPanelStyle(String panelStyle) {
        this.panelStyle = panelStyle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
