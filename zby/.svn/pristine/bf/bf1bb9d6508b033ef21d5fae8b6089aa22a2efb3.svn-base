package com.biaozhunyuan.tianyi.common.model.user;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by 王安民 on 2017/9/1.
 * 组织的实体类
 */

public class Organize {
    @DatabaseField(generatedId = true, unique = true)
    int _Id;//部门id
    @DatabaseField
    private String parent; //上级部门id
    @DatabaseField
    private String name; //部门名称
    @DatabaseField
    private String staffNumber; //员工数量
    @DatabaseField
    private String uuid; //部门id



    private boolean selected = false;//是否被选中,选择员工时使用,默认值是未被选中

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void set_Id(int _Id) {
        this._Id = _Id;
    }



    public int get_Id() {
        return _Id;
    }



    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStaffNumber() {
        return staffNumber;
    }

    public void setStaffNumber(String staffNumber) {
        this.staffNumber = staffNumber;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
