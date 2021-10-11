package com.biaozhunyuan.tianyi.chatLibary.chat.model;

/**
 * Created by wangAnMin on 2018/11/29.
 * 命令行实体类
 */

public class CommandModel {
    private String name; //快捷键名称（释义）

    /**
     * 命令快捷键缩写，例如 /rw(新建任务),rz(新建日志)
     */
    private String shortCut;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortCut() {
        return shortCut;
    }

    public void setShortCut(String shortCut) {
        this.shortCut = shortCut;
    }
}
