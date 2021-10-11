package com.biaozhunyuan.tianyi.apply.model;

import java.io.Serializable;

/**
 * Created by 王安民 on 2017/9/10.
 * 申请详情单元格信息
 */

public class CellInfo implements Serializable {

    private Boolean Required;
    private String LoadRelated;
    private String LoadDetailRelatedFields;
    private String Binding;
    private String DefaultValue;
    private String Value;
    private int ColumnSpan;
    private boolean isDetails;

    /// <summary>
    /// 对应的（如客户）弹出新建的界面的js方法名称，
    /// 需要在js时面预先定义js方法
    /// </summary>
    private String NewMethod;
    /// <summary>
    /// 长度限制（0为不限制），主区域有效
    /// </summary>
    private int Length;
    //提示文本
    private String LabelText;

    // todo:MinLength/MaxLength
    /// <summary>
    /// 计算公式，这个公式只用于对明细区域进行集合运算的公式，比如 SUM, AVG, COUNT
    /// </summary>
    private String Expression;

    /// <summary>
    /// 计算公式，这个公式只用于主区域的单元格的四则运算，比如 "价格*数量"
    /// </summary>
    private String MasterCellExpression;
    /// <summary>
    /// 类型限制为string int double date
    /// </summary>
    private String DataType;
    private String CategoryMemberPath;
    private String Dict;
    /// <summary>
    /// 字典是临时从服务器上加载（客户，staffId，产品等比较大的字典应该用这种方式），还是页面打开的时候已经加载完成（小字典）
    /// </summary>
    private Boolean Remote;
    /// <summary>
    /// 字典的过滤器，有时候想不使用整个字典时，可以设置此属性来过滤
    /// </summary>
    private String Filter;
    private String Remoteurl;
    private Boolean ReadOnly;
    /// <summary>
    /// 完全不显示，不占位的隐藏
    /// </summary>
    private Boolean Hidden;

    private Boolean Invisible;
    private Boolean IsEncrypted = false; //是否密文显示

    /// <summary>
    /// 类型限制为textbox combobox datepicker autocompletebox checkbox image checkboxlist product
    /// </summary>
    private String CellStyle;
    private String Text;
    private String Format;
    private String OnChange;
    private String OnBlur;
    private String OnFocus;
    private String OnClick;
    private String OnKeydown;
    private String OnKeyup;
    private String DisplayMemberPath;  //字典表中的字段名
    private String ValueMemberPath;

    private String MinDate;
    private String MaxDate;

    /// <summary>
    /// 逗号隔开的功能点的编号，比如 23,22 或者 32
    /// 表示登录用户有这些功能点的权限，才能编辑此单元格
    /// 如果此属性为空则忽略此属性。
    /// 如果ReadOnly属性为true，也忽略此属性
    /// </summary>
    private String EditablePrivilege;

    private Boolean IsDetailCell;
    private Boolean IsDetailHeaderCell;


    private Boolean Multiple;
    private int LeftBorderStrokeWidth;
    private String LeftBorderStrokeColor;
    private int RightBorderStrokeWidth;
    private String RightBorderStrokeColor;
    private int TopBorderStrokeWidth;
    private String TopBorderStrokeColor;
    private String BottomBorderStrokeColor;
    private int BottomBorderStrokeWidth;
    private String Regex;

    private String ParentFieldName;

    private String ChildFieldName;
    //button 弹出框 选择列表数据 2017-08-02 谭海兵
    //button 弹出框 选择列表数据 column 查询字段描述表使用
    private String TableName;
    //button 弹出框 选择列表数据 请求的url 注意url中的&要替换为&amp;
    private String Url;
    //button 弹出框 选择列表数据 关联的类型 detail和master
    private String RelationType;
    //button 弹出框 选择列表数据 关联的字段 {目标字段:来源字段,目标字段2:来源字段2}
    private String RelationField;
    //button 弹出框 选择列表数据 显示的信息格式化 {name}-{department}
    private String ShowInfoFormat;

    //控制字段显示、隐藏、只读和必填
    private String FieldsSwitch;

    public boolean isDetails() {
        return isDetails;
    }

    public void setDetails(boolean details) {
        isDetails = details;
    }

    public Boolean getRequired() {
        return Required;
    }

    public void setRequired(Boolean required) {
        Required = required;
    }

    public String getLoadRelated() {
        return LoadRelated;
    }

    public void setLoadRelated(String loadRelated) {
        LoadRelated = loadRelated;
    }

    public String getLoadDetailRelatedFields() {
        return LoadDetailRelatedFields;
    }

    public void setLoadDetailRelatedFields(String loadDetailRelatedFields) {
        LoadDetailRelatedFields = loadDetailRelatedFields;
    }

    public String getBinding() {
        return Binding;
    }

    public void setBinding(String binding) {
        Binding = binding;
    }

    public String getFieldsSwitch() {
        return FieldsSwitch;
    }

    public void setFieldsSwitch(String fieldsSwitch) {
        FieldsSwitch = fieldsSwitch;
    }

    public int getColumnSpan() {
        return ColumnSpan;
    }

    public void setColumnSpan(int columnSpan) {
        ColumnSpan = columnSpan;
    }

    public String getNewMethod() {
        return NewMethod;
    }

    public void setNewMethod(String newMethod) {
        NewMethod = newMethod;
    }

    public int getLength() {
        return Length;
    }

    public void setLength(int length) {
        Length = length;
    }

    public String getLabelText() {
        return LabelText;
    }

    public void setLabelText(String labelText) {
        LabelText = labelText;
    }

    public String getExpression() {
        return Expression;
    }

    public void setExpression(String expression) {
        Expression = expression;
    }

    public String getMasterCellExpression() {
        return MasterCellExpression;
    }

    public void setMasterCellExpression(String masterCellExpression) {
        MasterCellExpression = masterCellExpression;
    }

    public String getDataType() {
        return DataType;
    }

    public void setDataType(String dataType) {
        DataType = dataType;
    }

    public String getCategoryMemberPath() {
        return CategoryMemberPath;
    }

    public void setCategoryMemberPath(String categoryMemberPath) {
        CategoryMemberPath = categoryMemberPath;
    }

    public Boolean getEncrypted() {
        return IsEncrypted;
    }

    public void setEncrypted(Boolean encrypted) {
        IsEncrypted = encrypted;
    }

    public String getDict() {
        return Dict;
    }

    public void setDict(String dict) {
        Dict = dict;
    }

    public Boolean getRemote() {
        return Remote;
    }

    public void setRemote(Boolean remote) {
        Remote = remote;
    }

    public String getFilter() {
        return Filter;
    }

    public void setFilter(String filter) {
        Filter = filter;
    }

    public String getRemoteurl() {
        return Remoteurl;
    }

    public void setRemoteurl(String remoteurl) {
        this.Remoteurl = remoteurl;
    }

    public Boolean getReadOnly() {
        return ReadOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        ReadOnly = readOnly;
    }

    public Boolean getHidden() {
        return Hidden;
    }

    public void setHidden(Boolean hidden) {
        Hidden = hidden;
    }

    public Boolean getInvisible() {
        return Invisible;
    }

    public void setInvisible(Boolean invisible) {
        Invisible = invisible;
    }

    public String getCellStyle() {
        return CellStyle;
    }

    public void setCellStyle(String cellStyle) {
        CellStyle = cellStyle;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getFormat() {
        return Format;
    }

    public void setFormat(String format) {
        Format = format;
    }

    public String getOnChange() {
        return OnChange;
    }

    public void setOnChange(String onChange) {
        OnChange = onChange;
    }

    public String getOnBlur() {
        return OnBlur;
    }

    public void setOnBlur(String onBlur) {
        OnBlur = onBlur;
    }

    public String getOnFocus() {
        return OnFocus;
    }

    public void setOnFocus(String onFocus) {
        OnFocus = onFocus;
    }

    public String getOnClick() {
        return OnClick;
    }

    public void setOnClick(String onClick) {
        OnClick = onClick;
    }

    public String getOnKeydown() {
        return OnKeydown;
    }

    public void setOnKeydown(String onKeydown) {
        OnKeydown = onKeydown;
    }

    public String getOnKeyup() {
        return OnKeyup;
    }

    public void setOnKeyup(String onKeyup) {
        OnKeyup = onKeyup;
    }

    public String getDisplayMemberPath() {
        return DisplayMemberPath;
    }

    public void setDisplayMemberPath(String displayMemberPath) {
        DisplayMemberPath = displayMemberPath;
    }

    public String getValueMemberPath() {
        return ValueMemberPath;
    }

    public void setValueMemberPath(String valueMemberPath) {
        ValueMemberPath = valueMemberPath;
    }

    public String getEditablePrivilege() {
        return EditablePrivilege;
    }

    public void setEditablePrivilege(String editablePrivilege) {
        EditablePrivilege = editablePrivilege;
    }

    public Boolean getDetailCell() {
        return IsDetailCell;
    }

    public void setDetailCell(Boolean detailCell) {
        IsDetailCell = detailCell;
    }

    public Boolean getDetailHeaderCell() {
        return IsDetailHeaderCell;
    }

    public void setDetailHeaderCell(Boolean detailHeaderCell) {
        IsDetailHeaderCell = detailHeaderCell;
    }

    public Boolean getMultiple() {
        return Multiple;
    }

    public void setMultiple(Boolean multiple) {
        Multiple = multiple;
    }

    public int getLeftBorderStrokeWidth() {
        return LeftBorderStrokeWidth;
    }

    public void setLeftBorderStrokeWidth(int leftBorderStrokeWidth) {
        LeftBorderStrokeWidth = leftBorderStrokeWidth;
    }

    public String getLeftBorderStrokeColor() {
        return LeftBorderStrokeColor;
    }

    public void setLeftBorderStrokeColor(String leftBorderStrokeColor) {
        LeftBorderStrokeColor = leftBorderStrokeColor;
    }

    public int getRightBorderStrokeWidth() {
        return RightBorderStrokeWidth;
    }

    public void setRightBorderStrokeWidth(int rightBorderStrokeWidth) {
        RightBorderStrokeWidth = rightBorderStrokeWidth;
    }

    public String getRightBorderStrokeColor() {
        return RightBorderStrokeColor;
    }

    public void setRightBorderStrokeColor(String rightBorderStrokeColor) {
        RightBorderStrokeColor = rightBorderStrokeColor;
    }

    public int getTopBorderStrokeWidth() {
        return TopBorderStrokeWidth;
    }

    public void setTopBorderStrokeWidth(int topBorderStrokeWidth) {
        TopBorderStrokeWidth = topBorderStrokeWidth;
    }

    public String getTopBorderStrokeColor() {
        return TopBorderStrokeColor;
    }

    public void setTopBorderStrokeColor(String topBorderStrokeColor) {
        TopBorderStrokeColor = topBorderStrokeColor;
    }

    public String getBottomBorderStrokeColor() {
        return BottomBorderStrokeColor;
    }

    public void setBottomBorderStrokeColor(String bottomBorderStrokeColor) {
        BottomBorderStrokeColor = bottomBorderStrokeColor;
    }

    public int getBottomBorderStrokeWidth() {
        return BottomBorderStrokeWidth;
    }

    public void setBottomBorderStrokeWidth(int bottomBorderStrokeWidth) {
        BottomBorderStrokeWidth = bottomBorderStrokeWidth;
    }

    public String getRegex() {
        return Regex;
    }

    public void setRegex(String regex) {
        Regex = regex;
    }

    public String getParentFieldName() {
        return ParentFieldName;
    }

    public void setParentFieldName(String parentFieldName) {
        ParentFieldName = parentFieldName;
    }

    public String getChildFieldName() {
        return ChildFieldName;
    }

    public void setChildFieldName(String childFieldName) {
        ChildFieldName = childFieldName;
    }

    public String getTableName() {
        return TableName;
    }

    public void setTableName(String tableName) {
        TableName = tableName;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getRelationType() {
        return RelationType;
    }

    public void setRelationType(String relationType) {
        RelationType = relationType;
    }

    public String getRelationField() {
        return RelationField;
    }

    public void setRelationField(String relationField) {
        RelationField = relationField;
    }

    public String getShowInfoFormat() {
        return ShowInfoFormat;
    }

    public void setShowInfoFormat(String showInfoFormat) {
        ShowInfoFormat = showInfoFormat;
    }

    public String getDefaultValue() {
        return DefaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        DefaultValue = defaultValue;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }


    public String getMinDate() {
        return MinDate;
    }

    public void setMinDate(String minDate) {
        MinDate = minDate;
    }

    public String getMaxDate() {
        return MaxDate;
    }

    public void setMaxDate(String maxDate) {
        MaxDate = maxDate;
    }
}
