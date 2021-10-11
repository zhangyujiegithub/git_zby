package com.biaozhunyuan.tianyi.common.model.form;

import java.io.Serializable;

public class FormRelatedDataResult implements Serializable
    {
        private static final long serialVersionUID = 7882905347391704439L;
        /// <summary>
        /// 表列席
        /// 1：CurrentTable 2：MainTable 3：DetailTable
        /// </summary>
        public int TableType ;
        /// <summary>
        /// 列名
        /// </summary>
        public String FieldName ;
        /// <summary>
        /// 值
        /// </summary>
        public String Value ;
    }