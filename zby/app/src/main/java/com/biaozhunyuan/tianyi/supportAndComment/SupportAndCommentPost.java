package com.biaozhunyuan.tianyi.supportAndComment;

import java.io.Serializable;

/**
 * Created by wangAnMin on 2018/3/6.
 * 点赞评论上传的实体类
 */

public class SupportAndCommentPost implements Serializable {
    private String uuid; //编号
    private String fromId; //评论人uuid
    private String content; //  评论内容
    private String time; //  时间
    private String replyCommentId; // 被回复评论uuid
    private String toId; //被评论人uuid
    private String dataType; //数据类型
    private String dataId; //数据编号j
    private String isDeleted; //  是否删除
    private String creatorId; //  是否删除
    private String createTime; //  是否删除

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReplyCommentId() {
        return replyCommentId;
    }

    public void setReplyCommentId(String replyCommentId) {
        this.replyCommentId = replyCommentId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }
}
