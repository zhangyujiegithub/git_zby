package com.biaozhunyuan.tianyi.activity;

import java.io.Serializable;
import java.util.List;

/**
 * 进销存物品实体类 用于页面传递
 */

public class InventoryList implements Serializable{
    private List<Inventory> list;

    public InventoryList(List<Inventory> list) {
        this.list = list;
    }

    public List<Inventory> getList() {
        return list;
    }

    public void setList(List<Inventory> list) {
        this.list = list;
    }
}
