package com.biaozhunyuan.tianyi.task;


import com.biaozhunyuan.tianyi.view.bragboard.model.DragColumn;

import java.util.List;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2018/4/3
 * @discription 列表项，每个列表项包含一个子项的List {@link List<Item>}
 * @usage null
 */
public class OaWorkLaneList implements DragColumn {
    private final List<WorkScheduleList> itemList;
    private String type;
    private String uuid;
    private String title;
    private String oaWorkTaskPanelIid;
    private List<WorkScheduleList>workScheduleList;

    public OaWorkLaneList(String title,String oaWorkTaskPanelIid,String type,String uuid, List<WorkScheduleList> items) {
        this.title = title;
        this.itemList = items;
        this.type = type;
        this.uuid = uuid;
        this.oaWorkTaskPanelIid = oaWorkTaskPanelIid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOaWorkTaskPanelIid() {
        return oaWorkTaskPanelIid;
    }

    public void setOaWorkTaskPanelIid(String oaWorkTaskPanelIid) {
        this.oaWorkTaskPanelIid = oaWorkTaskPanelIid;
    }

    public List<WorkScheduleList> getWorkScheduleList() {
        return workScheduleList;
    }

    public void setWorkScheduleList(List<WorkScheduleList> workScheduleList) {
        this.workScheduleList = workScheduleList;
    }

    public List<WorkScheduleList> getItemList() {
        return itemList;
    }

    @Override
    public int getColumnIndex() {
        return 0;
    }

    @Override
    public void setColumnIndex(int columnIndexInHorizontalRecycleView) {

    }
}
