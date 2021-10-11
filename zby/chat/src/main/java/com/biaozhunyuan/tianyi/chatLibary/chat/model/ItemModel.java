package com.biaozhunyuan.tianyi.chatLibary.chat.model;

import java.io.Serializable;

/**
 * Created by WangChang on 2016/4/28.
 */
public class ItemModel implements Serializable {

    public static final int CHAT_Left = 1001;
    public static final int CHAT_RIGHT = 1002;
    public int type;
    public Object object;

    public ItemModel(int type, Object object) {
        this.type = type;
        this.object = object;
    }
}
