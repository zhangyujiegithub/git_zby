package com.biaozhunyuan.tianyi.global;

/**
 * Created by wangAnMin on 2018/12/29.
 * 刷新事件类
 */

public class EventBusResumeEvent {

    private String event; //事件名称

    private boolean isResume; //是否刷新

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public boolean isResume() {
        return isResume;
    }

    public void setResume(boolean resume) {
        isResume = resume;
    }
}
