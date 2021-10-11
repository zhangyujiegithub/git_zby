package com.biaozhunyuan.tianyi.newuihome;

import java.util.List;

/**
 * Created by wangAnMin on 2018/6/5.
 */

public class FunctionMenu {
    public String id;
    public String name;
    public String iconClass;
    public String link;
    private List<FunctionMenu> childrenSubmenu;


    public List<FunctionMenu> getChildrenSubmenu() {
        return childrenSubmenu;
    }

    public void setChildrenSubmenu(List<FunctionMenu> childrenSubmenu) {
        this.childrenSubmenu = childrenSubmenu;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconClass() {
        return iconClass;
    }

    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
