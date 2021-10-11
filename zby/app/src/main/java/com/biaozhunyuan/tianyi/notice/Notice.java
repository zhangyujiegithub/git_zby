package com.biaozhunyuan.tianyi.notice;

import java.io.Serializable;

/**
 * Auto-generated: 2017-08-07 17:58:59
 *
 * @author 王安民
 */
public class Notice implements Serializable {

    private String creatorId;                       //创建人Id
    private String creatorName;                       //创建人名称
    public String creationDepartmentId;           //创建部门id
    private String creationDepartmentName;           //创建部门名称
    private String creationTime;            //创建时间
    private String lastUpdateTime;          //最后修改时间
    private String content;                      //任务内容
    public String attachmentIds;                 //附件
    private String title;                        //标题
    private String categoryId;                   //分类Id
    public String recipient;                    //接收人
    private Boolean isTop;                      //是否制定
    private Integer recipientCount;              //接受总人数
    private Boolean isHtml;                     //是否富文本
    private Boolean isSubmit;                    //是否提交
    private Integer readCount;                   //阅读人数
    private String uuid;
    private boolean isLike;              //是否已经点赞
    private int likeNumber;             // 点赞数
    private int commentNumber;          //评论数
    private String reciveCategory;              //接收人类别,发送范围(应该是 按人/按部门/按群组，按群组情况下，receptDepartments为群组的ids)
    private String categoryName;
    private String favoriteNumber;


    private String recipientIds;                    //接收人，多个
    private String toDepartmentIds;              //接收部门，多个
    private String expirationTime;      //过期时间
    private String startTime;            //启动时间

    public String getFavoriteNumber() {
        return favoriteNumber;
    }

    public void setFavoriteNumber(String favoriteNumber) {
        this.favoriteNumber = favoriteNumber;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setRecipientIds(String recipientIds) {
        this.recipientIds = recipientIds;
    }

    public void setToDepartmentIds(String toDepartmentIds) {
        this.toDepartmentIds = toDepartmentIds;
    }

    public void setExpirationTime(String expirationTime) {
        this.expirationTime = expirationTime;
    }


    public String getRecipientIds() {
        return recipientIds;
    }

    public String getToDepartmentIds() {
        return toDepartmentIds;
    }

    public String getExpirationTime() {
        return expirationTime;
    }


    public int getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(int commentNumber) {
        this.commentNumber = commentNumber;
    }

    public String getCreatorIDName() {
        return "creatorId";
    }

    public String getcategoryIdName() {
        return "categoryId";
    }

    public String getCreatorId() {
        return creatorId;
    }
    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public int getLikeNumber() {
        return likeNumber;
    }

    public void setLikeNumber(int likeNumber) {
        this.likeNumber = likeNumber;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getReciveCategory() {
        return reciveCategory;
    }

    public void setReciveCategory(String reciveCategory) {
        this.reciveCategory = reciveCategory;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }


    public boolean getIsTop() {
        return isTop;
    }

    public void setIsTop(boolean isTop) {
        this.isTop = isTop;
    }

    public boolean getIsHtml() {
        return isHtml;
    }

    public void setIsHtml(boolean isHtml) {
        this.isHtml = isHtml;
    }

    public boolean getIsSubmit() {
        return isSubmit;
    }

    public void setIsSubmit(boolean isSubmit) {
        this.isSubmit = isSubmit;
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Boolean getTop() {
        return isTop;
    }

    public void setTop(Boolean top) {
        isTop = top;
    }

    public Integer getRecipientCount() {
        return recipientCount;
    }

    public void setRecipientCount(Integer recipientCount) {
        this.recipientCount = recipientCount;
    }

    public Boolean getHtml() {
        return isHtml;
    }

    public void setHtml(Boolean html) {
        isHtml = html;
    }

    public Boolean getSubmit() {
        return isSubmit;
    }

    public void setSubmit(Boolean submit) {
        isSubmit = submit;
    }

    public Integer getReadCount() {
        return readCount;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreationDepartmentName() {
        return creationDepartmentName;
    }

    public void setCreationDepartmentName(String creationDepartmentName) {
        this.creationDepartmentName = creationDepartmentName;
    }
}
