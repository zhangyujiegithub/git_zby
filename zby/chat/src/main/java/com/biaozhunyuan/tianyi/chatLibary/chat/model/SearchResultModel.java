package com.biaozhunyuan.tianyi.chatLibary.chat.model;

import java.util.List;

public class SearchResultModel {
    private List<Staff> staffList;
    private List<GroupModel> groupNameIncludeKeyList;  //群组
    private List<UnreadMessage.UnreadMessageContent> messageIncludeKeyList;  //聊天

    public class Staff {
        public String name;
        public String uuid;
    }

    public List<Staff> getStaffList() {
        return staffList;
    }

    public void setStaffList(List<Staff> staffList) {
        this.staffList = staffList;
    }

    public List<GroupModel> getGroupNameIncludeKeyList() {
        return groupNameIncludeKeyList;
    }

    public void setGroupNameIncludeKeyList(List<GroupModel> groupNameIncludeKeyList) {
        this.groupNameIncludeKeyList = groupNameIncludeKeyList;
    }

    public List<UnreadMessage.UnreadMessageContent> getMessageIncludeKeyList() {
        return messageIncludeKeyList;
    }

    public void setMessageIncludeKeyList(List<UnreadMessage.UnreadMessageContent> messageIncludeKeyList) {
        this.messageIncludeKeyList = messageIncludeKeyList;
    }
}
