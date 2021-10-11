package com.biaozhunyuan.tianyi.common.model;

import java.io.Serializable;

/**
 * Created by 王安民 on 2017/8/8.
 * <p>
 * 日志实体类
 */
public class WorkRecord implements Serializable {
    private String creatorId; //创建人ID
    private String creationDepartmentId;  //创建人部门ID
    private String creationTimeToString; //创建时间
    private String creationTime; //创建时间
    private String lastUpdateTime; //最后更新时间
    private String content; //内容
    private String logType; //日志类型：普通日志，周总结，月总结....
    private String attachmentIds; //附件ID
    private String site; // 位置
    private String uuid; //日志ID
    private String creatorName; //创建人名称
    private String creatorAvatar; //创建人头像
    private boolean isLike; //是否已经点赞
    private boolean isFavorite; //是否收藏
    private boolean isLikeNumber; //是否已经点赞
    private int commentNumber; //评论数
    private int likeNumber; // 点赞数
    private int favoriteNumber; //收藏数

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public boolean isLikeNumber() {
        return isLikeNumber;
    }

    public void setLikeNumber(boolean likeNumber) {
        isLikeNumber = likeNumber;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public int getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(int commentNumber) {
        this.commentNumber = commentNumber;
    }

    public int getLikeNumber() {
        return likeNumber;
    }

    public void setLikeNumber(int likeNumber) {
        this.likeNumber = likeNumber;
    }

    public int getFavoriteNumber() {
        return favoriteNumber;
    }

    public void setFavoriteNumber(int favoriteNumber) {
        this.favoriteNumber = favoriteNumber;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreationDepartmentId() {
        return creationDepartmentId;
    }

    public void setCreationDepartmentId(String creationDepartmentId) {
        this.creationDepartmentId = creationDepartmentId;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAttachmentIds() {
        return attachmentIds;
    }

    public void setAttachmentIds(String attachmentIds) {
        this.attachmentIds = attachmentIds;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCreationTimeToString() {
        return creationTimeToString;
    }

    public void setCreationTimeToString(String creationTimeToString) {
        this.creationTimeToString = creationTimeToString;
    }

    public String getCreatorAvatar() {
        return creatorAvatar;
    }

    public void setCreatorAvatar(String creatorAvatar) {
        this.creatorAvatar = creatorAvatar;
    }
}
