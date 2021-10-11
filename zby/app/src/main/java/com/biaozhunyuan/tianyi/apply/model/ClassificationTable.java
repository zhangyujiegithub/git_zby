package com.biaozhunyuan.tianyi.apply.model;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;
import java.util.Date;

/**
 * 流程分类实体类
 *
 */
public class ClassificationTable implements Serializable {
        private static final long serialVersionUID = 8029862849470268988L;

        @DatabaseField(generatedId = true, unique = true)
        public int _Id;
        @DatabaseField
        public int 编号;

        @DatabaseField
        public String 名称;

        @DatabaseField
        public String 表单配置文件;

        @DatabaseField
        public String 手机表单配置文件;

        @DatabaseField
        public String 工作流配置文件;

        @DatabaseField
        public int 上级;

        @DatabaseField
        public String 代码;

        @DatabaseField
        public String 表单名称;

        @DatabaseField
        public String 字段名称;

        @DatabaseField
        public Date UpdateTime;

        /***
         * 最近使用为1，默认所有0
         */
        @DatabaseField
        public int isLatest;

        public int get_Id() {
            return _Id;
        }

        public void set_Id(int _Id) {
            this._Id = _Id;
        }

        @Override
        public String toString() {
            return super.toString();
        }

}
