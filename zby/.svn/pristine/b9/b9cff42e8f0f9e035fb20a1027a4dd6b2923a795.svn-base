package com.biaozhunyuan.tianyi.attch;


import com.biaozhunyuan.tianyi.common.attach.Attach;

import java.io.Serializable;

/***
 * 附件信息，上传附件专用
 *
 * @author K 2016-04-18
 */
public class AttachInfo implements Serializable {

    /**
     * 附件号
     */
    private int id;

    private String uuid;

    /**
     * 附件本地路径
     */
    private String localPath;

    /**
     * 附件类型 @EnumAttachType
     */
    private EnumAttachType type;

    /**
     * 附件信息
     */
    private Attach attach;

    /**
     *
     */
    private static final long serialVersionUID = 6985018807275743612L;

    public AttachInfo() {
        super();
    }

    /***
     *
     * @param id
     *            附件号
     * @param localPath
     *            本地路径
     * @param url
     *            附件网络路径
     */
//    public AttachInfo(int id, String localPath) {
//        super();
//        this.id = id;
//        this.localPath = localPath;
//    }

    /***
     *
     * @param idStr
     *            附件号
     * @param localPath
     *            本地路径
     * @param url
     *            附件网络路径
     */
    public AttachInfo(String idStr, String localPath) {
        super();
        setUuid(idStr);
        setType(EnumAttachType.附件号);
    }

    public int getId() {
        return id;
    }

    public void setId(String id) {
        setUuid(id);
    }


    public void setIdAndUpdateType(String idStr) {
        setUuid(idStr);
        setType(EnumAttachType.附件号);
    }

//    public void setIdAndUpdateType(String id) {
////        setId(id);
//        setUuid(id);
//        setType(EnumAttachType.附件号);
//    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public void setLocalPathUpdateType(String localPath) {
        setLocalPath(localPath);
        setType(EnumAttachType.本地路径);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public EnumAttachType getType() {
        return type;
    }

    public void setType(EnumAttachType type) {
        this.type = type;
    }

    public Attach getAttach() {
        return attach;
    }

    public void setAttach(Attach attach) {
        this.attach = attach;
    }

}
