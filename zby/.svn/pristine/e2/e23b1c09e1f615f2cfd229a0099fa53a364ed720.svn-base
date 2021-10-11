package com.biaozhunyuan.tianyi.attch;

import java.io.Serializable;

/**
 * 照片信息模型类
 *
 * @author bohr
 */
public class PhotoInfo implements Serializable {
    public static final long serialVersionUID = 3727834192828106572L;
    public int _id; // 数据库id
    public String id; // 照片编号

    /**
     * 照片序列号编号，对应一组照片
     */
    public String photoSerialNo;

    public String name; // 照片名称

    /**
     * 照片全路径（包括照片名）
     */
    public String Address;

    /**
     * 照片缓存路径（包括照片名）
     */
    public String cachePath;

    public String photoTime;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getCachePath() {
        return cachePath;
    }

    public void setCachePath(String cachePath) {
        this.cachePath = cachePath;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPhotoTime() {
        return photoTime;
    }

    public void setPhotoTime(String photoTime) {
        this.photoTime = photoTime;
    }

    public String getPhotoSerialNo() {
        return photoSerialNo;
    }

    public void setPhotoSerialNo(String photoSerialNo) {
        this.photoSerialNo = photoSerialNo;
    }

    @Override
    public String toString() {
        return "PhotoInfo [_id=" + _id + ", id=" + id + ", photoSerialNo="
                + photoSerialNo + ", name=" + name + ", Address=" + Address
                + ", photoTime=" + photoTime + "]";
    }
}
