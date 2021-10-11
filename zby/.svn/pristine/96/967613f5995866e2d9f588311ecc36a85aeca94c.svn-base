package com.biaozhunyuan.tianyi.chatLibary.chat.model;


import android.text.TextUtils;

import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.model.user.User;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by wangAnMin on 2018/4/20.
 */

public class ChatMessage implements Serializable {
    private String id;
    private String chatId;
    private String messageId;
    private Integer isSingle; //是否是单聊
    private String from;
    private int sendStatus; //发送状态
    private String createTime;
    private String groupIcon;
    private String chatType = "staff";  //聊天类型 staff:员工,visitor:游客;  默认为staff
    private String name; //会话的名称
    private String avatar; //会话的名称
    private String staffIds;  //用户id列表,这个对话里面的和你对话的人的id
    private int audioDuration;  //语音类型的时长
    private String audioLocalPath;  //语音本地地址
    private String fileLocalPath;  //文件类型本地地址
    private String uploadState = UPLOAD_STATE_UN_UNLOAD;
    private String downloadState = DOWNLOAD_STATE_UN_UNDOWNLOAD;
    private String token;
    public static final int CHAT_Left = 1001;  //聊天框左边，代表和登录用户聊天的类别
    public static final int CHAT_RIGHT = 1002; //聊天框右边，代表登录用户类别
    public static final int CHAT_LEFT_AUDIO = 1003; //聊天框左边语音
    public static final int CHAT_RIGHT_AUDIO = 1004; //聊天框右边语音
    public static final int CHAT_LEFT_FILE = 1005; //聊天框左边文件
    public static final int CHAT_RIGHT_FILE = 1006; //聊天框右边文件
    public static final int CHAT_TIP = 1007; //提示类型消息
    public static final String FORMAT_VOICE = "aud"; //语音类型
    public static final String FORMAT_FILE = "file"; //文件类型
    public static final String FORMAT_TXT = "txt"; //文本类型
    public static final String FORMAT_IMG = "img"; //图片类型
    public static final String FORMAT_TIP = "tip"; //提示类型消息
    public static final String UPLOAD_STATE_UN_UNLOAD = "UN_UNLOAD"; //文件上传类型：未上传
    public static final String UPLOAD_STATE_UPLOADING = "UPLOADING"; //文件上传类型：上传中
    public static final String UPLOAD_STATE_FINISHED = "UPLOAD_FINISHED"; //文件上传类型：已上传
    public static final String DOWNLOAD_STATE_UN_UNDOWNLOAD = "UN_DOWNLOAD"; //文件下载类型：未下载
    public static final String DOWNLOAD_STATE_DOWNLOADING = "DOWNLOADING"; //文件下载类型：下载中
    public static final String DOWNLOAD_STATE_FINISHED = "DOWNLOADING_FINISHED"; //文件下载类型：已下载
    private int chatCategory;
    /**
     * 如果有多个接收人，则是多个逗号隔开的uuid
     */
    private String talkers;
    private String body;
    private int unReadCount;
    /**
     * login, logout, err, chat, login, logout,红包,voice call, video call
     * 如果是cmd是login, body 是一个json {"c":"新起点";"u":"黄波";"p":"fda324gfxsd323"}
     * 如果是image, wav等类型，text只包含id信息。具体的图片，声音以附件的形式上传
     * error是服务器端返回的错误信息
     * lin     登录
     * cht    发送消息
     * ros    获取在线人
     * rec    获取聊天记录
     * prn   打印
     */
    private String cmd;

    /// <summary>
    /// text,image,wav, rtf。如果为空和等于text都代表纯文本。如果是其他则body是二进制
    /// 聊天时，截图如果带有文字的的话，按rtf发送。如果不带文字按img发送
    /// 如果只有文字按text发送
    /// </summary>
    private String format;
    /// <summary>
    /// 网络传输时无需设置，只是客户端接收到rtf消息后，解析储存用
    /// </summary>
    private String rtf;
    /// <summary>
    /// 网络传输时无需设置，只是客户端接收到img消息后，解析储存用
    /// </summary>
//    private Image img;
    private String mobileLastUpdateTime;
    private String pcLastUpdateTime;
    private String groupUpdateTime;

    private ChatMessage replyMsg; //被回复的消息

    private String sendTime;
    /**
     * 更多的参数，比如关联项目，关联客户。需要转换为Map<String, String>
     */
    private String values;
    /**
     * 网络传输时无需对toUsers赋值，这个是服务器端为方便使用，依据tos生成的User类对象集合
     */
    private List<User> users;
    private HashMap<String, String> keyValues;

    public String getGroupIcon() {
        return groupIcon;
    }

    public void setGroupIcon(String groupIcon) {
        this.groupIcon = groupIcon;
    }

    public ChatMessage() {
        id = UUID.randomUUID().toString().replaceAll("-", "");
//        messageUuid = id;
        sendTime = ViewHelper.getCurrentFullTime();
    }

    public String getTalkersUuid() {
        String uuid = talkers.substring(0, talkers.indexOf("@"));
        return uuid;
    }

    public String getFromUuid() {
        String uuid;
        if (!TextUtils.isEmpty(from)) {
            if (from.contains("@")) {
                uuid = from.substring(0, from.indexOf("@"));
            } else {
                uuid = from;
            }
            return uuid;
        } else {
            return "";
        }
    }

    public String getChatType() {
        return chatType;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
    }

    /**
     * 获取多人聊天的talkers，含有   @+企业号
     *
     * @return
     */
    public String getMutiTalkers() {
        String talkers;
        if (getTalkers().contains(",")) {
            if (CHAT_Left == chatCategory) {
                talkers = getTalkers().replace
                        (Global.mUser.getUuid() + "@" + Global.mUser.getCorpId(), getFrom());
            } else {
                talkers = getTalkers();
            }
        } else {
            if (CHAT_Left == chatCategory) {
                talkers = getFromUuid();
            } else {
                talkers = getTalkersUuid();
            }
        }
        return talkers;
    }

    public ChatMessage getReplyMsg() {
        return replyMsg;
    }

    public void setReplyMsg(ChatMessage replyMsg) {
        this.replyMsg = replyMsg;
    }

    public int getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(int sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMobileLastUpdateTime() {
        return mobileLastUpdateTime;
    }

    public void setMobileLastUpdateTime(String mobileLastUpdateTime) {
        this.mobileLastUpdateTime = mobileLastUpdateTime;
    }

    public String getPcLastUpdateTime() {
        return pcLastUpdateTime;
    }

    public void setPcLastUpdateTime(String pcLastUpdateTime) {
        this.pcLastUpdateTime = pcLastUpdateTime;
    }

    public String getGroupUpdateTime() {
        return groupUpdateTime;
    }

    public void setGroupUpdateTime(String groupUpdateTime) {
        this.groupUpdateTime = groupUpdateTime;
    }

    public HashMap<String, String> getKeyValues() {
        return keyValues;
    }

    public String getDownloadState() {
        return downloadState;
    }

    public void setDownloadState(String downloadState) {
        this.downloadState = downloadState;
    }

    public String getUploadState() {
        return uploadState;
    }

    public void setUploadState(String uploadState) {
        this.uploadState = uploadState;
    }

    public void setKeyValues(HashMap<String, String> keyValues) {
        this.keyValues = keyValues;
    }

    public Integer isSingle() {
        return isSingle;
    }

    public void setSingle(Integer single) {
        isSingle = single;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStaffIds() {
        return staffIds;
    }

    public void setStaffIds(String staffIds) {
        this.staffIds = staffIds;
    }

    public String getFileLocalPath() {
        return fileLocalPath;
    }

    public void setFileLocalPath(String fileLocalPath) {
        this.fileLocalPath = fileLocalPath;
    }

    public String getAudioLocalPath() {
        return audioLocalPath;
    }

    public void setAudioLocalPath(String audioLocalPath) {
        this.audioLocalPath = audioLocalPath;
    }

    public int getAudioDuration() {
        return audioDuration;
    }

    public void setAudioDuration(int audioDuration) {
        this.audioDuration = audioDuration;
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public int getChatCategory() {
        return chatCategory;
    }

    public void setChatCategory(int chatCategory) {
        this.chatCategory = chatCategory;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTalkers() {
        return talkers;
    }

    public void setTalkers(String talkers) {
        this.talkers = talkers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getRtf() {
        return rtf;
    }

    public void setRtf(String rtf) {
        this.rtf = rtf;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    //    public Image getImg() {
//        return img;
//    }

//    public void setImg(Image img) {
//        this.img = img;
//    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
