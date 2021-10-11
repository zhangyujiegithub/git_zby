package com.biaozhunyuan.tianyi.apply.model;

import java.util.List;

/**
 * 表单单元格控制器：控制单元格属性：显示、隐藏、必填、只读
 */
public class TabCellsController {
    private String value; //对应的值
    private List<Field> hideFields; //值所对应的需要隐藏的字段的binding
    private List<Field> requiredFields;//值所对应的需要设置为必填的字段的binding
    private List<Field> readonlyFields;//值所对应的需要设置为只读的字段的binding

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<Field> getHideFields() {
        return hideFields;
    }

    public void setHideFields(List<Field> hideFields) {
        this.hideFields = hideFields;
    }

    public List<Field> getRequiredFields() {
        return requiredFields;
    }

    public void setRequiredFields(List<Field> requiredFields) {
        this.requiredFields = requiredFields;
    }

    public List<Field> getReadonlyFields() {
        return readonlyFields;
    }

    public void setReadonlyFields(List<Field> readonlyFields) {
        this.readonlyFields = readonlyFields;
    }

    public class Field {
        private String field;

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }
    }
}
