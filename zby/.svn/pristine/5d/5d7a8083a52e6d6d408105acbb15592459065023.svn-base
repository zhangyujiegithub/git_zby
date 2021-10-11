package com.biaozhunyuan.tianyi.apply.model;

import com.biaozhunyuan.tianyi.common.model.form.FormRelatedData;

import java.io.Serializable;
import java.util.List;

/**
 * 英文名作为字段的FieldInfo
 *
 * @author KJX 2015/03/10 10:50
 */
public class FieldInfo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 5934725307124998570L;
    /**
     * FieldInfo增加了 fieldLabel属性，以便手机表单可以灵活设置字段的Label提示文字
     */
    public String fieldLabel;
    /**
     * 字段名
     */
    public String fieldName;

    /**
     * 字段值
     */
    public String fieldValue;

    /**
     * 控件类型 如EditText,DateTimePicker
     */
    public String fieldStyle;

    // 废弃
    // /** 控件类型 如EditText,DateTimePicker */
    // public String fieldType;

    /**
     * 默认值
     */
    public String defaultValue;

    /**
     * 字典项 名称
     */
    public String fieldDict;

    /**
     * 是否必填项
     */
    public String required;

    /**
     * 数据类型：int/double/String
     */
    public String dataType;

    /**
     * 正则校验表达式
     */
    public String regex;

    public String header;
    public String expression;

    /**
     * 格式化字段
     */
    public String format;

    /***
     * 是否是只读，true,false
     */
    public String readOnly;


    /// <summary>
    /// 表单关联
    /// </summary>
    public String loaddetailrelatedfieldStr;


    public List<FormRelatedData> loaddetailrelatedfields;


    // / 如果这个字段的值是下拉的话，可能别的字段选择后，这个字段的值要缩小选择范围
    // / 比如产品型号和产品分类，如果产品分类选择了某个分类，产品型号就只能列出这个分类下的型号了
    // / 这种情况下parentFieldName=产品分类

    public String parentFieldName;


    public String childFieldName;


    /**
     * 控件的X坐标
     */
    public int MobileX;

    /**
     * 控件的Y坐标
     */
    public int MobileY;

    /**
     * 控件的文本字体大小
     */
    public int MobileFontSize;

    /**
     * 控件的文本字体颜色
     */
    public String MobileFontColor;

}