package com.biaozhunyuan.tianyi.common.model.dict;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;
import java.util.Date;

/***
 * 最近选择字典项
 *
 * @author K
 *
 */
public class LatestSelectedDict implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -1267962615327529272L;

    @DatabaseField(generatedId = true, unique = true)
    private int _id;

    @DatabaseField
    private String uuid;

    @DatabaseField
    private String name;

    @DatabaseField
    private String DictName;

    @DatabaseField
    private Date updateTime;

    /**
     *
     */
    public LatestSelectedDict() {
        super();
    }

    /**
     * @param id       字典项Id
     * @param name     字典项名称
     * @param dictName 字典表名称
     */
    public LatestSelectedDict(String id, String name, String dictName) {
        uuid = id;
        this.name = name;
        DictName = dictName;
        this.updateTime = new Date();
    }

    /**
     * @param id         字典项Id
     * @param name       字典项名称
     * @param dictName   字典表名称
     * @param updateTime 最后更新时间，默认为当前
     */
    public LatestSelectedDict(String id, String name, String dictName,
                              Date updateTime) {
        uuid = id;
        this.name = name;
        DictName = dictName;
        this.updateTime = updateTime;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String id) {
        uuid = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDictName() {
        return DictName;
    }

    public void setDictName(String dictName) {
        DictName = dictName;
    }

}
