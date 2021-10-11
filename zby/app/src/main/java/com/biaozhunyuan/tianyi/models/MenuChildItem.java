package com.biaozhunyuan.tianyi.models;

/**
 * 首页导航菜单 子项
 */
public class MenuChildItem {


    /**
     * 图标
     */
    public int ico;

    /**
     * H5菜单图标
     */
    public String icon;

    /**
     * H5菜单要跳转的地址
     */
    public String URL;

    /**
     * 菜单类别：native （原生）和 H5
     * 默认是原生
     */
    public String category = "native";

    /**
     * 标题
     */
    public String title;

    /**
     * 点击 数量
     */
    public int count;

    /**
     * 未读 数量
     */
    public int unreadCount;

    /**
     * 模块功能点
     */
    public EnumFunctionPoint ponit;

    /***
     * 未读数量默认为0
     *
     * @param ico
     *            图标
     * @param title
     *            标题
     * @param ponit
     *            功能点
     */
    public MenuChildItem(int ico, String title, EnumFunctionPoint ponit) {
        super();
        this.ico = ico;
        this.title = title;
        this.count = 0;
        this.ponit = ponit;
    }

    public MenuChildItem() {

    }

    /***
     *
     * @param ico
     *            图标
     * @param title
     *            标题
     * @param count
     *            未读数量
     * @param ponit
     *            功能点
     */
    public MenuChildItem(int ico, String title, int count,
                         EnumFunctionPoint ponit) {
        super();
        this.ico = ico;
        this.title = title;
        this.count = count;
        this.ponit = ponit;
    }

    @Override
    public String toString() {
        return "MenuChildItem{" +
                "ico=" + ico +
                ", title='" + title + '\'' +
                ", count=" + count +
                ", ponit=" + ponit +
                '}';
    }


}
