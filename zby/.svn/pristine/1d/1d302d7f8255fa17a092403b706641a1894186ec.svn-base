package com.biaozhunyuan.tianyi.activity;

import java.io.Serializable;
import java.util.Map;

/**
 * 进销存物品实体类
 */

public class Inventory implements Serializable{

    private String name;
    private String uuid;
    private String barcode;
    private String itemstyle; //规格
    private String balance; //库存
    private int num;
    private Map<String,String> cargoList;

    public Map<String, String> getCargoList() {
        return cargoList;
    }

    public void setCargoList(Map<String, String> cargoList) {
        this.cargoList = cargoList;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getItemstyle() {
        return itemstyle;
    }

    public void setItemstyle(String itemstyle) {
        this.itemstyle = itemstyle;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Inventory(String name, String uuid, int num) {
        this.name = name;
        this.uuid = uuid;
        this.num = num;
    }

    public Inventory() {
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
