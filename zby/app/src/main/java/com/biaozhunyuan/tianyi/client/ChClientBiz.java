package com.biaozhunyuan.tianyi.client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.attendance.LocationListActivity;
import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.model.字典;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.model.DictData;
import com.biaozhunyuan.tianyi.common.model.form.表单字段;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.helper.DictionaryQueryDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.InputSoftHelper;
import com.biaozhunyuan.tianyi.common.helper.PreferceManager;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.utils.RegexUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.view.CityPicker;
import com.biaozhunyuan.tianyi.view.DateAndTimePicker;
import com.biaozhunyuan.tianyi.view.DictIosMultiPicker;
import com.biaozhunyuan.tianyi.common.view.DictIosPickerBottomDialog;
import com.biaozhunyuan.tianyi.view.LastInputEditText;
import com.biaozhunyuan.tianyi.view.TimePickerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Request;

import static com.biaozhunyuan.tianyi.helper.SelectLocationBiz.SELECT_LOCATION_CODE;


/**
 * 长汇客户逻辑处理 create 2016-1-27 参考 GenerateChildViewHelper
 */
public class ChClientBiz {

    private final String TAG = "ChClientBiz";
    private Context mContext;
    /**
     * 相关联的fragment
     */
    private Fragment mRelateFragment;
    private DictionaryQueryDialogHelper mDictDialogHelper;
    private DictIosPickerBottomDialog dialog;

    private DictIosMultiPicker mDictIosMultiPicker;
    private DateAndTimePicker mDateAndTimePicker;
    private List<表单字段> mFormList;
    // 右侧内容输入框 使用Tag保存内容
    private List<EditText> mEditTexts;
    private LinearLayout mRootLayout;// 根布局
    private LayoutInflater mInflater;
    private CityPicker mCityPicker;
    private boolean isEnabled; //是否为只读
    private boolean isShowPRimg = false; //是否添加标题前图标
    private boolean CHECKOUT_REPETITION = true;//校验重复 是否可保存
    private String tableName = ""; //表名
    private String id = ""; //uuid
    private String url = Global.BASE_JAVA_URL + GlobalMethord.校验动态字段重复;

    private List<字典> dictList = new ArrayList<字典>();
    private final DictionaryHelper dictionary;
    private boolean isShowContactAndPhone = true;

    public ChClientBiz(Context context, List<表单字段> mFormList,
                       LinearLayout mRootLayout, boolean isEnabled, String tableName, String id) {
        super();
        this.mContext = context;
        this.mFormList = mFormList;
        this.mRootLayout = mRootLayout;
        this.isEnabled = isEnabled;
        this.tableName = tableName;
        this.id = id;
        dictionary = new DictionaryHelper(mContext);
        mInflater = LayoutInflater.from(mContext);
        mDictDialogHelper = DictionaryQueryDialogHelper.getInstance(mContext);
        dialog = new DictIosPickerBottomDialog(mContext);
        mDictIosMultiPicker = new DictIosMultiPicker(mContext);
        mDateAndTimePicker = new DateAndTimePicker(mContext);
        mEditTexts = new ArrayList<EditText>();
        mCityPicker = CityPicker.getInstance(mContext);
    }

    public ChClientBiz(Context context, List<表单字段> mFormList,
                       LinearLayout mRootLayout, boolean isEnabled, boolean isShowPRimg) {
        super();
        this.mContext = context;
        this.mFormList = mFormList;
        this.mRootLayout = mRootLayout;
        this.isEnabled = isEnabled;
        this.isShowPRimg = isShowPRimg;
        dictionary = new DictionaryHelper(mContext);
        mInflater = LayoutInflater.from(mContext);
        mDictDialogHelper = DictionaryQueryDialogHelper.getInstance(mContext);
        dialog = new DictIosPickerBottomDialog(mContext);
        mDictIosMultiPicker = new DictIosMultiPicker(mContext);
        mDateAndTimePicker = new DateAndTimePicker(mContext);
        mEditTexts = new ArrayList<EditText>();
        mCityPicker = CityPicker.getInstance(mContext);
    }


    /**
     * 生成控件
     */
    public void generateViews() {
        for (final 表单字段 form : mFormList) {
            LinearLayout childView = (LinearLayout) mInflater.inflate(
                    R.layout.item_control_ch_client, null);
            ImageView ivImg = childView.findViewById(R.id.iv_ch);
            TextView tvRedStar = childView.findViewById(R.id.tv_red_star_ch_client);
            TextView tvStatus = (TextView) childView
                    .findViewById(R.id.tv_statuts_control_ch_client);
            TextView tvTitle = (TextView) childView
                    .findViewById(R.id.tv_title_control_ch_client);
            LastInputEditText etValue = (LastInputEditText) childView
                    .findViewById(R.id.et_value_control_ch_client);
            // 保存字典类型编号
            TextView tvId = (TextView) childView
                    .findViewById(R.id.tv_id_control_ch_client);
            ImageView ivIco = (ImageView) childView
                    .findViewById(R.id.iv_img_control_ch_client);
            RelativeLayout rl = (RelativeLayout) childView.findViewById(R.id.rl_root_item_client);
            LinearLayout ll_edit = (LinearLayout) childView.findViewById(R.id.ll_clientBiz_edit);
            LinearLayout ll_sex = (LinearLayout) childView.findViewById(R.id.ll_clientBiz_sex);
            final ImageView iv_man = (ImageView) childView.findViewById(R.id.iv_clentBiz_man);
            final ImageView iv_woman = (ImageView) childView.findViewById(R.id.iv_clentBiz_woman);
            TextView tv_man = (TextView) childView.findViewById(R.id.tv_clentBiz_man);
            TextView tv_woman = (TextView) childView.findViewById(R.id.tv_clentBiz_woman);


            etValue.setTag(form); // 绑定数据
            setTitleText(form, tvTitle);
            initRequired(form, etValue, tvRedStar);
            setIcoInfo(form, ivIco);
//            initColorStatus(form, tvStatus);
            setReadOnlyStyle(childView, etValue);
            setEditTextValue(etValue);
            setOnEvent(tvTitle, ivIco, etValue);
            setEditTextEnabled(etValue);
            setEditTextCheckOutRepetition(form, etValue);//校验重复
            if (isShowPRimg) {
                setPRImageType(form, ivImg);
            }
            mEditTexts.add(etValue);
            if ("编号".equals(form.Name)) {
                if (TextUtils.isEmpty(form.Value)) {
                    form.Value = "0";
                }
                childView.setVisibility(View.GONE);
            }
            if ("uuid".equals(form.Name)) {
                if (TextUtils.isEmpty(form.Value)) {
                    form.Value = "0";
                }
                form.Identify = true;
                childView.setVisibility(View.GONE);
            }
            if ("contact".equals(form.Name) && !isShowContactAndPhone) {
                childView.setVisibility(View.GONE);
            }
            if ("mobile".equals(form.Name) && !isShowContactAndPhone) {
                childView.setVisibility(View.GONE);
            }
            if (("creatorId".equals(form.Name) || "advisorId".equals(form.Name))
                    && TextUtils.isEmpty(form.Value)) {
                form.Value = Global.mUser.getUuid();
                etValue.setText(Global.mUser.getName());
            }

            if (!TextUtils.isEmpty(form.Value) && TextUtils.isEmpty(form.DicText)) {
                getCustomDicts(form.DicTableName, form.Value, etValue);
            }

            if ("性别".equals(form.DisplayName)) {
                ll_edit.setVisibility(View.GONE);
                ll_sex.setVisibility(View.VISIBLE);
                getDict(iv_man, iv_woman, tv_man, tv_woman, form, form.DicTableName);
            }

            iv_man.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    iv_man.setImageResource(R.drawable.ic_single_choose);
                    iv_woman.setImageResource(R.drawable.ic_single_no_choose);
                    if (dictList != null && dictList.size() > 1) {
                        form.Value = dictList.get(0).getUuid();
                    }
                }
            });

            iv_woman.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    iv_woman.setImageResource(R.drawable.ic_single_choose);
                    iv_man.setImageResource(R.drawable.ic_single_no_choose);
                    if (dictList != null && dictList.size() > 1) {
                        form.Value = dictList.get(1).getUuid();
                    }
                }
            });


            mRootLayout.addView(childView);
        }


    }


    /**
     * 校验字段输入值是否已经被录入
     */
    private void setEditTextCheckOutRepetition(表单字段 form, LastInputEditText etValue) {
        if (form.UniqueField) {
            CheckOutRepetition checkOutRepetition = new CheckOutRepetition();
            checkOutRepetition.setFieldName(form.Name);
            checkOutRepetition.setId(id);
            checkOutRepetition.setTableName(tableName);
            etValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!TextUtils.isEmpty(s)) {
                        checkOutRepetition.setValue(s.toString());
                        StringRequest.postAsyn(url, checkOutRepetition, new StringResponseCallBack() {
                            @Override
                            public void onResponse(String response) {
                                String data = JsonUtils.pareseData(response);
                                if ("0".equals(data)) {
                                    form.WhetherRepeat = true;
                                } else {
                                    form.WhetherRepeat = false;
                                }
                            }

                            @Override
                            public void onFailure(Request request, Exception ex) {

                            }

                            @Override
                            public void onResponseCodeErro(String result) {

                            }
                        });
                    } else {
                        form.WhetherRepeat = false;
                    }
                }
            });
        }

    }

    /**
     * 是否只可查看不可更改
     *
     * @param etValue
     */
    private void setEditTextEnabled(LastInputEditText etValue) {
        if (!isEnabled) {
            etValue.setEnabled(false);
        }
    }

    public void setRelateFragment(Fragment fragment) {
        this.mRelateFragment = fragment;
    }

    public void setShowContactAndPhone(boolean showContactAndPhone) {
        isShowContactAndPhone = showContactAndPhone;
    }

    /***
     * 设置左侧标题显示
     *
     * @param form
     * @param tvTitle
     */
    private void setTitleText(表单字段 form, TextView tvTitle) {
        tvTitle.setText(StrUtils.pareseNull(form.DisplayName));
    }

    private void setEditTextValue(final EditText etValue) {
        final 表单字段 form = (表单字段) etValue.getTag();
        etValue.setText(StrUtils.pareseNull(form.DicText));
        String dataType = form.DataType;

        if (isTextType(dataType) || isNumberType(dataType)
                || isDateTimeSelectType(dataType)) {
            if (form.Name.equals("生日")) {
                if (form.Value != null && form.Value.length() > 10) {
                    String str = form.Value.substring(0, 10);
                    etValue.setText(str);
                }
            } else if (!TextUtils.isEmpty(form.Format) && !TextUtils.isEmpty(form.Value)) {
                etValue.setText(ViewHelper.resetDateStringFormat(form.Value, form.Format));
            } else {
                // 如果是文本类型或数值类型 直接显示
                etValue.setText(StrUtils.pareseNull(form.Value));
            }
        } else if (isDictSelectType(dataType)) {
            etValue.setText(StrUtils.pareseNull(form.DicText));
        }
    }

    private void setOnEvent(TextView tvTittle, ImageView iv, final EditText etValue) {
        final 表单字段 form = (表单字段) etValue.getTag();
        String dataType = form.DataType;
        boolean readOnly = form.ReadOnly;

        if (form.DisplayName.equals("理财师") && TextUtils.isEmpty(form.Value)) {
            etValue.setText(Global.mUser.getName());
            form.Value = Global.mUser.getUuid();
        }

        if (form.DisplayName.equals("创建时间") && TextUtils.isEmpty(form.Value)) {
            etValue.setText(ViewHelper.getCurrentFullTime());
            form.Value = ViewHelper.getCurrentFullTime();
        }
        if ("creatorId".equals(form.Name) && "创建人".equals(form.DisplayName) && !TextUtils.isEmpty(form.Value)) {
            etValue.setText(dictionary.getUserNameById(form.Value));
        }
        if ("业务员".equals(form.DisplayName)) {
            String valueBYkey = PreferceManager.getInsance().getValueBYkey("customer" + Global.mUser.getUuid());
            if (valueBYkey.equals("customer_public")) {
                PreferceManager.getInsance().saveValueBYkey("customer" + Global.mUser.getUuid(), "");
            } else {
                if (!TextUtils.isEmpty(form.Value)) {
                    etValue.setText(dictionary.getUserNameById(form.Value));
                } else {
                    etValue.setText(dictionary.getUserNameById(Global.mUser.getUuid()));
                    form.Value = Global.mUser.getUuid();
                }
            }
        }


        if (readOnly) {
            // 只读类型 EditText不可编辑
            etValue.setEnabled(false);
            return;
        }

        if (isTextType(dataType) || isNumberType(dataType)) {

            /*etValue.requestFocus();//获取焦点
            etValue.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtils.i(TAG, "移动光标到最后的位置...");
                    etValue.setSelection(etValue.getText().toString().trim().length());//光标移动到最后
                }
            });*/
            // 输入类型
            if (isNumberType(dataType)) {
                // 设置输入类型为 Number
                etValue.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
            //
            if (form.Name.equals("address")
                    && (form.TypeName.equals("商机信息") || form.TypeName.equals("投资情况")
                    || form.TypeName.equals("联系信息") || form.TypeName.equals("项目信息"))) {
                etValue.setFocusable(false);
                etValue.setEnabled(true);
                etValue.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Double lat = Double.parseDouble(PreferceManager.getInsance().getValueBYkey("add_business_lat"));
                            Double lng = Double.parseDouble(PreferceManager.getInsance().getValueBYkey("add_business_lng"));
                            selectLocation(mContext, lat, lng);
                        } catch (Exception e) {
                            e.printStackTrace();
                            selectLocation(mContext, 39.9088230000, 116.3974700000);
                        }
                    }
                });
            }

        } else {// 不可输入
            etValue.setFocusable(false);
            if ("combobox".equalsIgnoreCase(form.DataType)) {
                if (isPrivinceType(form.DisplayName)) {
                    etValue.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /*mCityPicker.show(mContext);
                            mCityPicker
                                    .setOnCheckedListener(new CityPicker.OnCheckedListener() {
                                        @Override
                                        public void onChecked(
                                                SelectedProvince selectedCity) {
                                            if (selectedCity != null) {
                                                updateCity(selectedCity);
                                            }
                                        }
                                    });*/
                            String parentId = "";
                            if (!TextUtils.isEmpty(form.ParentField)) {
                                for (表单字段 表单字段 : mFormList) {
                                    if (form.ParentField.equals(表单字段.Name)) {
                                        parentId = 表单字段.Value;
                                        break;
                                    }
                                }
                                dialog.show(form.DicTableName, true, "parent='" + parentId + "'", "");
                            } else {
                                dialog.show(form.DicTableName, true, form.filter, "");
                            }

                            dialog.setOnSelectedDictListener(new DictIosPickerBottomDialog.onSelectDictListener() {
                                @Override
                                public void onSelectedDict(字典 dict) {
                                    form.Value = dict.uuid + "";
                                    form.DicText = dict.name;
                                    setEditTextValue(etValue);
                                }
                            });
                        }
                    });

                    iv.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String parentId = "";
                            if (!TextUtils.isEmpty(form.ParentField)) {
                                for (表单字段 表单字段 : mFormList) {
                                    if (form.ParentField.equals(表单字段.Name)) {
                                        parentId = 表单字段.Value;
                                        break;
                                    }
                                }
                                dialog.show(form.DicTableName, true, "parent='" + parentId + "'", "");
                            } else {
                                dialog.show(form.DicTableName, true, form.filter, "");
                            }

                            dialog.setOnSelectedDictListener(new DictIosPickerBottomDialog.onSelectDictListener() {
                                @Override
                                public void onSelectedDict(字典 dict) {
                                    form.Value = dict.uuid + "";
                                    form.DicText = dict.name;
                                    setEditTextValue(etValue);
                                }
                            });
                        }
                    });

                    tvTittle.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String parentId = "";
                            if (!TextUtils.isEmpty(form.ParentField)) {
                                for (表单字段 表单字段 : mFormList) {
                                    if (form.ParentField.equals(表单字段.Name)) {
                                        parentId = 表单字段.Value;
                                        break;
                                    }
                                }
                                dialog.show(form.DicTableName, true, "parent='" + parentId + "'", "");
                            } else {
                                dialog.show(form.DicTableName, true, form.filter, "");
                            }

                            dialog.setOnSelectedDictListener(new DictIosPickerBottomDialog.onSelectDictListener() {
                                @Override
                                public void onSelectedDict(字典 dict) {
                                    form.Value = dict.uuid + "";
                                    form.DicText = dict.name;
                                    setEditTextValue(etValue);
                                }
                            });
                        }
                    });
                } else if (isProductType(form.Name)) {
                    // 选择产品
                    etValue.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mRelateFragment != null) {
//                                ChProductBiz.selectProduct(mRelateFragment);
                            }
                        }
                    });
                } else {
                    etValue.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!TextUtils.isEmpty(form.filter)) {
                                dialog.show(form.DicTableName, true, form.filter, "");
                            } else {
                                dialog.show(form.DicTableName, true);
                            }
                            dialog.setOnSelectedDictListener(new DictIosPickerBottomDialog.onSelectDictListener() {
                                @Override
                                public void onSelectedDict(字典 dict) {
                                    form.Value = dict.uuid + "";
                                    form.DicText = dict.name;
                                    setEditTextValue(etValue);
                                }
                            });
                        }
                    });

                    iv.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!TextUtils.isEmpty(form.filter)) {
                                dialog.show(form.DicTableName, true, form.filter, "");
                            } else {
                                dialog.show(form.DicTableName, true);
                            }
                            dialog.setOnSelectedDictListener(new DictIosPickerBottomDialog.onSelectDictListener() {
                                @Override
                                public void onSelectedDict(字典 dict) {
                                    form.Value = dict.uuid + "";
                                    form.DicText = dict.name;
                                    setEditTextValue(etValue);
                                }
                            });
                        }
                    });

                    tvTittle.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!TextUtils.isEmpty(form.filter)) {
                                dialog.show(form.DicTableName, true, form.filter, "");
                            } else {
                                dialog.show(form.DicTableName, true);
                            }
                            dialog.setOnSelectedDictListener(new DictIosPickerBottomDialog.onSelectDictListener() {
                                @Override
                                public void onSelectedDict(字典 dict) {
                                    form.Value = dict.uuid + "";
                                    form.DicText = dict.name;
                                    setEditTextValue(etValue);
                                }
                            });
                        }
                    });
                }
            } else if (isDateTimeSelectType(dataType)) {
                etValue.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InputSoftHelper.hideKeyboard(etValue);
                        if ("yyyy-MM-dd".equals(form.Format)) {
                            TimePickerView pickerView = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY);
                            pickerView.setRange(1900, 2100); //设置可选年份范围
                            pickerView.setTime(new Date());
                            pickerView.setCyclic(true);
                            pickerView.setCancelable(true);
                            pickerView.show();
                            pickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
                                @Override
                                public void onTimeSelect(Date date) {
                                    String s1 = ViewHelper.formatDateToStr(date, "yyyy-MM-dd");
                                    etValue.setText(s1);
                                    form.Value = s1;
                                    setEditTextValue(etValue);
                                }
                            });
//                            mDateAndTimePicker.showDateWheel("选择"
//                                    + form.DisplayName, etValue, false);
//                            mDateAndTimePicker.setOnSelectedListener(new DateAndTimePicker.ISelected() {
//                                @Override
//                                public void onSelected(String date) {
////                                    String str = ViewHelper.convertStrToFormatDateStr(date, "yyyy-MM-dd");
//                                    etValue.setText(date);
//                                    form.Value = date;
//                                    setEditTextValue(etValue);
//                                }
//                            });
                        } else if ("yyyy-MM".equals(form.Format)) {
                            TimePickerView pickerView = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH);
                            pickerView.setRange(1900, 2100); //设置可选年份范围
                            pickerView.setTime(new Date());
                            pickerView.setCyclic(true);
                            pickerView.setCancelable(true);
                            pickerView.show();
                            pickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
                                @Override
                                public void onTimeSelect(Date date) {
                                    String s1 = ViewHelper.formatDateToStr(date, "yyyy-MM");
                                    etValue.setText(s1);
                                    form.Value = s1;
                                    setEditTextValue(etValue);
                                }
                            });
//                            mDateAndTimePicker.showDateWheel("选择"
//                                    + form.DisplayName, etValue, false);
//                            mDateAndTimePicker.setOnSelectedListener(new DateAndTimePicker.ISelected() {
//                                @Override
//                                public void onSelected(String date) {
////                                    String str = ViewHelper.convertStrToFormatDateStr(date, "yyyy-MM-dd");
//                                    etValue.setText(date);
//                                    form.Value = date;
//                                    setEditTextValue(etValue);
//                                }
//                            });

                        } else {
//                            mDateAndTimePicker.showDateWheel("选择"
//                                    + form.DisplayName, etValue);
                            TimePickerView pickerView = new TimePickerView(mContext, TimePickerView.Type.ALL);
                            pickerView.setRange(1900, 2100); //设置可选年份范围
                            pickerView.setTime(new Date());
                            pickerView.setCyclic(true);
                            pickerView.setCancelable(true);
                            pickerView.show();
                            pickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
                                @Override
                                public void onTimeSelect(Date date) {
                                    String s = ViewHelper.formatDateToStr(date, "yyyy-MM-dd HH:mm:ss");
                                    etValue.setText(s);
                                    form.Value = s;
                                    setEditTextValue(etValue);
                                }
                            });
                        }
                    }
                });

                iv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InputSoftHelper.hideKeyboard(etValue);
                        if ("yyyy-MM-dd".equals(form.Format)) {
//                            mDateAndTimePicker.showDateWheel("选择"
//                                    + form.DisplayName, etValue, false);
//                            mDateAndTimePicker.setOnSelectedListener(new DateAndTimePicker.ISelected() {
//                                @Override
//                                public void onSelected(String date) {
////                                    String str = ViewHelper.convertStrToFormatDateStr(date, "yyyy-MM-dd");
//                                    etValue.setText(date);
//                                    form.Value = date;
//                                    setEditTextValue(etValue);
//                                }
//                            });
                            TimePickerView pickerView = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY);
                            pickerView.setRange(1900, 2100); //设置可选年份范围
                            pickerView.setTime(new Date());
                            pickerView.setCyclic(true);
                            pickerView.setCancelable(true);
                            pickerView.show();
                            pickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
                                @Override
                                public void onTimeSelect(Date date) {
                                    String s1 = ViewHelper.formatDateToStr(date, "yyyy-MM-dd");
                                    etValue.setText(s1);
                                    form.Value = s1;
                                    setEditTextValue(etValue);
                                }
                            });
                        } else if ("yyyy-MM".equals(form.Format)) {
//                            mDateAndTimePicker.showDateWheel("选择"
//                                    + form.DisplayName, etValue);
                            TimePickerView pickerView = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH);
                            pickerView.setRange(1900, 2100); //设置可选年份范围
                            pickerView.setTime(new Date());
                            pickerView.setCyclic(true);
                            pickerView.setCancelable(true);
                            pickerView.show();
                            pickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
                                @Override
                                public void onTimeSelect(Date date) {
                                    String s1 = ViewHelper.formatDateToStr(date, "yyyy-MM");
                                    etValue.setText(s1);
                                    form.Value = s1;
                                    setEditTextValue(etValue);
                                }
                            });
                        } else {
                            TimePickerView pickerView = new TimePickerView(mContext, TimePickerView.Type.ALL);
                            pickerView.setRange(1900, 2100); //设置可选年份范围
                            pickerView.setTime(new Date());
                            pickerView.setCyclic(true);
                            pickerView.setCancelable(true);
                            pickerView.show();
                            pickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
                                @Override
                                public void onTimeSelect(Date date) {
                                    String s1 = ViewHelper.formatDateToStr(date, "yyyy-MM-dd");
                                    etValue.setText(s1);
                                    form.Value = s1;
                                    setEditTextValue(etValue);
                                }
                            });
                        }
                    }
                });

                tvTittle.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InputSoftHelper.hideKeyboard(etValue);
                        if ("yyyy-MM-dd".equals(form.Format)) {
//                            mDateAndTimePicker.showDateWheel("选择"
//                                    + form.DisplayName, etValue, false);
//                            mDateAndTimePicker.setOnSelectedListener(new DateAndTimePicker.ISelected() {
//                                @Override
//                                public void onSelected(String date) {
////                                    String str = ViewHelper.convertStrToFormatDateStr(date, "yyyy-MM-dd");
//                                    etValue.setText(date);
//                                    form.Value = date;
//                                    setEditTextValue(etValue);
//                                }
//                            });
                            TimePickerView pickerView = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY);
                            pickerView.setRange(1900, 2100); //设置可选年份范围
                            pickerView.setTime(new Date());
                            pickerView.setCyclic(true);
                            pickerView.setCancelable(true);
                            pickerView.show();
                            pickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
                                @Override
                                public void onTimeSelect(Date date) {
                                    String s1 = ViewHelper.formatDateToStr(date, "yyyy-MM-dd");
                                    etValue.setText(s1);
                                    form.Value = s1;
                                    setEditTextValue(etValue);
                                }
                            });
                        } else if ("yyyy-MM".equals(form.Format)) {
//                            mDateAndTimePicker.showDateWheel("选择"
//                                    + form.DisplayName, etValue);
                            TimePickerView pickerView = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH);
                            pickerView.setRange(1900, 2100); //设置可选年份范围
                            pickerView.setTime(new Date());
                            pickerView.setCyclic(true);
                            pickerView.setCancelable(true);
                            pickerView.show();
                            pickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
                                @Override
                                public void onTimeSelect(Date date) {
                                    String s1 = ViewHelper.formatDateToStr(date, "yyyy-MM");
                                    etValue.setText(s1);
                                    form.Value = s1;
                                    setEditTextValue(etValue);
                                }
                            });
                        } else {
                            TimePickerView pickerView = new TimePickerView(mContext, TimePickerView.Type.ALL);
                            pickerView.setRange(1900, 2100); //设置可选年份范围
                            pickerView.setTime(new Date());
                            pickerView.setCyclic(true);
                            pickerView.setCancelable(true);
                            pickerView.show();
                            pickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
                                @Override
                                public void onTimeSelect(Date date) {
                                    String s1 = ViewHelper.formatDateToStr(date, "yyyy-MM-dd");
                                    etValue.setText(s1);
                                    form.Value = s1;
                                    setEditTextValue(etValue);
                                }
                            });
                        }
                    }
                });
            } else if (isMultiSelectType(dataType)) { //多选类型
                etValue.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(form.filter)) {
                            mDictIosMultiPicker.showByDictName(mRootLayout.getId(), form.DicTableName, form.filter);
                        } else {
                            mDictIosMultiPicker.showByDictName(mRootLayout.getId(), form.DicTableName);
                        }
                        mDictIosMultiPicker
                                .setOnMultiSelectedListener(new DictIosMultiPicker.OnMultiSelectedListener() {
                                    @Override
                                    public void onSelected(String selectedIds,
                                                           String selectedNames) {
                                        form.Value = selectedIds + "";
                                        form.DicText = selectedNames;
                                        setEditTextValue(etValue);
                                    }
                                });
                    }
                });


                tvTittle.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(form.filter)) {
                            mDictIosMultiPicker.showByDictName(mRootLayout.getId(), form.DicTableName, form.filter);
                        } else {
                            mDictIosMultiPicker.showByDictName(mRootLayout.getId(), form.DicTableName);
                        }
                        mDictIosMultiPicker
                                .setOnMultiSelectedListener(new DictIosMultiPicker.OnMultiSelectedListener() {
                                    @Override
                                    public void onSelected(String selectedIds,
                                                           String selectedNames) {
                                        form.Value = selectedIds + "";
                                        form.DicText = selectedNames;
                                        setEditTextValue(etValue);
                                    }
                                });
                    }
                });

                iv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(form.filter)) {
                            mDictIosMultiPicker.showByDictName(mRootLayout.getId(), form.DicTableName, form.filter);
                        } else {
                            mDictIosMultiPicker.showByDictName(mRootLayout.getId(), form.DicTableName);
                        }
                        mDictIosMultiPicker
                                .setOnMultiSelectedListener(new DictIosMultiPicker.OnMultiSelectedListener() {
                                    @Override
                                    public void onSelected(String selectedIds,
                                                           String selectedNames) {
                                        form.Value = selectedIds + "";
                                        form.DicText = selectedNames;
                                        setEditTextValue(etValue);
                                    }
                                });
                    }
                });

            }

        }

    }

    private void setIcoInfo(表单字段 form, final ImageView ivIco) {
        String dataType = form.DataType;
        boolean readOnly = form.ReadOnly;
        ivIco.setVisibility(View.VISIBLE);
        if (isMultiSelectType(dataType) || isDictSelectType(dataType) || isPrivinceType(dataType)) {
            if (!form.ReadOnly) {
                ivIco.setImageResource(R.drawable.ic_right_arrow_gray);
            } else {
                ivIco.setVisibility(View.GONE);
            }
        } else if (isDateTimeSelectType(dataType)) {
            ivIco.setImageResource(R.drawable.ico_calendar);
        } else {
            ivIco.setVisibility(View.GONE);
        }
    }

    private void setPRImageType(表单字段 form, final ImageView ivIco) {
        String dataType = form.DataType;
        boolean readOnly = form.ReadOnly;
        ivIco.setVisibility(View.VISIBLE);
        if (isMultiSelectType(dataType) || isDictSelectType(dataType) || isPrivinceType(dataType)) {
            if (form.DisplayName.contains("人")) {
                ivIco.setImageResource(R.drawable.icon_client_blue);
            } else {
                ivIco.setImageResource(R.drawable.icon_stage_blue);
            }
        } else if (isDateTimeSelectType(dataType)) {
            ivIco.setImageResource(R.drawable.icon_time_blue);
        } else {
            ivIco.setVisibility(View.GONE);
        }
    }

    /**
     * 设置只读类型的样式
     *
     * @param llLayout
     * @param etValue
     */
    private void setReadOnlyStyle(final LinearLayout llLayout,
                                  final EditText etValue) {
        if (!isEnabled) {
            llLayout.setBackgroundColor(mContext.getResources().getColor(
                    R.color.bg_readonly));
        } else {
            final 表单字段 form = (表单字段) etValue.getTag();
            boolean readOnly = form.ReadOnly;
            if (readOnly) {
                llLayout.setBackgroundColor(mContext.getResources().getColor(
                        R.color.bg_readonly));
            }
        }
    }

    /**
     * 初始化状态色块
     *
     * @param form     表单实体
     * @param tvStatus 色块
     */
    private void initColorStatus(表单字段 form, TextView tvStatus) {
        if (TextUtils.isEmpty(form.Color)) {
            tvStatus.setVisibility(View.GONE);
        } else {
            tvStatus.setVisibility(View.VISIBLE);
            try {
                int bgColor = Color.parseColor(form.Color);
                tvStatus.setBackgroundColor(bgColor);
            } catch (Exception e) {
                tvStatus.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 设置初始化 必填项红色※是否显示
     */
    private void initRequired(表单字段 form, TextView tvRequired, TextView tvRedStar) {
        // 必填红色※
        if (form.Required) {
            tvRedStar.setVisibility(View.VISIBLE);
            tvRequired.setHint("必填");
        } else {
            tvRedStar.setVisibility(View.GONE);
        }
    }

    /***
     * 判断类型是否是文本类型
     *
     * @param dataType
     * @return
     */
    private boolean isTextType(String dataType) {
        return "string".equalsIgnoreCase(dataType);
    }

    /***
     * 判断类型是否是日期选择类型本类型
     *
     * @param dataType
     * @return
     */
    private boolean isDateTimeSelectType(String dataType) {
        return "datetime".equalsIgnoreCase(dataType);
    }

    /***
     * 判断类型是否是数值类型
     *
     * @param dataType
     * @return
     */
    private boolean isNumberType(String dataType) {
        return "int32".equalsIgnoreCase(dataType)
                || "double".equalsIgnoreCase(dataType);
    }

    /***
     * 判断类型是否是字典选择类型
     *
     * @param dataType
     * @return
     */
    private boolean isDictSelectType(String dataType) {
        return "multiselect".equalsIgnoreCase(dataType)
                || "combobox".equalsIgnoreCase(dataType);
    }

    /***
     * 判断类型是否是字典选择类型
     *
     * @param dataType
     * @return
     */
    private boolean isMultiSelectType(String dataType) {
        return "multiselect".equalsIgnoreCase(dataType);
    }

    /***
     * 判断类型是否是省市县选择类型
     *
     * @return
     * @formName 表单名称
     */
    private boolean isPrivinceType(String formName) {
        return "省".equals(formName) || "市".equals(formName)
                || "县".equals(formName);
    }

    /***
     * 判断类型是否选择产品
     *
     * @return
     * @formName 表单名称
     */
    public boolean isProductType(String formName) {
        return "意向产品".equals(formName);
    }

    public ArrayList<表单字段> getFormList() {
        ArrayList<表单字段> formList = new ArrayList<表单字段>();

        for (EditText etValue : mEditTexts) {
            表单字段 form = (表单字段) etValue.getTag();
            if ("string".equalsIgnoreCase(form.DataType)
                    || "double".equalsIgnoreCase(form.DataType)
                    || "datetime".equalsIgnoreCase(form.DataType)
                    || "int32".equalsIgnoreCase(form.DataType)) {
                form.Value = etValue.getText().toString();
            }
            formList.add(form);
        }
        return formList;
    }

    public void setFormListAddress(String mLocation, String mProvince, String mCity) {
        for (EditText etValue : mEditTexts) {
            表单字段 form = (表单字段) etValue.getTag();
            if (form.Name.equals("address")) {
                etValue.setText(mLocation);
            }
            if (form.Name.equals("province")) {
                etValue.setText(mProvince);
            }
            if (form.Name.equals("city")) {
                etValue.setText(mCity);
            }
        }
    }

    /***
     * 返回系统控件
     *
     * @return
     */
    public List<EditText> getEditList() {
        return mEditTexts;
    }

    /**
     * 空校验 和防重校验
     */
    public static String checkNull(List<表单字段> formList) {
        for (表单字段 form : formList) {
            if (form.Required) {
                if (TextUtils.isEmpty(form.Value)) {
                    String categrayInfo = "";
                    if (!TextUtils.isEmpty(form.TypeName)) {
                        categrayInfo = form.TypeName + "分类中的 ";
                    }
                    return categrayInfo + form.DisplayName + "不能为空";
                }
            }
            if (form.UniqueField) {
                if (form.WhetherRepeat) {
                    String categrayInfo = "";
                    if (!TextUtils.isEmpty(form.TypeName)) {
                        categrayInfo = form.TypeName + "分类中的 ";
                    }
                    return categrayInfo + form.DisplayName + "已存在";
                }
            }
        }
        return "";
    }

    /**
     * 正则公式校验
     */
    public static String checkRegEx(List<表单字段> formList) {
        for (表单字段 form : formList) {
            if (!TextUtils.isEmpty(form.Value)
                    && !TextUtils.isEmpty(form.RegEx)) {
                String regStr = form.RegEx;
                String[] regArr = {form.RegEx};
                if (form.RegEx.contains("!errorMsg!")) {
                    regArr = form.RegEx.split("!errorMsg!");
                }

                if (regArr != null && regArr.length > 0) {
                    regStr = regArr[0];
                }
                Logger.i("REGX::" + form.Value + "-----" + regStr);
                if (!RegexUtils.regex(form.Value, regStr)) {
                    if (regArr.length > 1) {
                        return form.DisplayName + "格式非法," + regArr[1];
                    }
                    return form.DisplayName + "格式非法";
                }
            }
        }
        return "";
    }

    /**
     * 如果证件类别是身份证，需要正则校验身份证号
     */
    public static String checkCardRegEx(List<表单字段> formList) {
        String cardType = "";
        String cardNo = "";
        for (表单字段 form : formList) {
            if (!TextUtils.isEmpty(form.Value)) {
                if ("证件类别".equals(form.Name)) {
                    cardType = form.Value;
                }

                if ("证件号".equals(form.Name)) {
                    cardNo = form.Value;
                }
            }
        }

        if (!TextUtils.isEmpty(cardNo) && !TextUtils.isEmpty(cardType)) {
            try {
                int type = Integer.parseInt(cardType);
                if (type == 1) {
                    if (!RegexUtils.isIdCard(cardNo)) {
                        return "非法的查身份证格式，请修改后再次提交";
                    }
                }
            } catch (Exception e) {
            }

        }
        return "";
    }

    /***
     * 更新省市县
     *
     * @param selectedCity
     */
    private void updateCity(SelectedProvince selectedCity) {
        for (EditText etValue : mEditTexts) {
            表单字段 form = (表单字段) etValue.getTag();
            if (form != null && isPrivinceType(form.DisplayName)) {
                if ("省".equals(form.DisplayName) && selectedCity.省 != null) {
                    form.Value = selectedCity.省.编号 + "";
                    form.DicText = selectedCity.省.名称;
                    etValue.setText(selectedCity.省.名称);
                } else if ("市".equals(form.DisplayName) && selectedCity.市 != null) {
                    form.Value = selectedCity.市.编号 + "";
                    form.DicText = selectedCity.市.名称;
                    etValue.setText(selectedCity.市.名称);
                } else if ("县".equals(form.DisplayName) && selectedCity.县 != null) {
                    form.Value = selectedCity.县.编号 + "";
                    form.DicText = selectedCity.县.名称;
                    etValue.setText(selectedCity.县.名称);
                }
            }
        }
    }


    public void getDict(final ImageView iv_man, final ImageView iv_woman, final TextView tv_man, final TextView tv_woman, final 表单字段 form, String dictName) {

        String url = Global.BASE_JAVA_URL + GlobalMethord.获取字典;
        Logger.i("getDict::" + url);
//        List<DictData> dictDatas = new ArrayList<>();
        final DictData dictData = new DictData();
        dictData.setDictionaryName(dictName);
//        dictDatas.add(dictData);
        try {
            StringRequest.postAsyn(url, dictData, new StringResponseCallBack() {
                @Override
                public void onResponse(String response) {

                    ProgressDialogHelper.dismiss();
                    Logger.i("getDict" + response);
                    try {
                        dictList = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), 字典.class);

                        if (dictList != null && dictList.size() > 1) {

                            tv_man.setText(dictList.get(0).getName());
                            tv_woman.setText(dictList.get(1).getName());

                            if (dictList.get(0).uuid.equals(form.Value)) {
                                iv_man.setImageResource(R.drawable.ic_single_choose);
                                iv_woman.setImageResource(R.drawable.ic_single_no_choose);
                            } else if (dictList.get(1).uuid.equals(form.Value)) {
                                iv_woman.setImageResource(R.drawable.ic_single_choose);
                                iv_man.setImageResource(R.drawable.ic_single_no_choose);
                            } else {
//                                form.Value = dictList.get(0).getUuid();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Request request, Exception ex) {
                }

                @Override
                public void onResponseCodeErro(String result) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 根据字典的id显示 内容
     *
     * @param dictTableName
     * @param value
     * @param editText
     */
    public void getCustomDicts(final String dictTableName, final String value, final EditText editText) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取字典;
        Logger.i("getDict" + url);
//        List<DictData> list = new ArrayList<>();
        final DictData dictData = new DictData();
        dictData.setFull(true);
        dictData.setDictionaryName(dictTableName);
//        list.add(dictData);
        StringRequest.postAsyn(url, dictData, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    List<字典> mDictList = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), 字典.class);
                    if (mDictList != null && mDictList.size() > 0) {
                        if (!TextUtils.isEmpty(value)) {
                            String[] split = value.split(",");
                            String str = "";
                            for (字典 dict : mDictList) {
                                for (String s : split) {
                                    if (s.equals(dict.getUuid())) {
                                        str += dict.getName() + ",";
                                    }
                                }
                            }
                            if (str.length() > 0) {
                                str = str.substring(0, str.length() - 1);
                                editText.setText(str);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });

    }

    /***
     * 从参数经纬度附近地址列表选择一个地点
     * SelectLocationBiz.SELECT_LOCATION_CODE
     *
     * @param context
     *            上下文
     * @param lat
     *            经度
     * @param lng
     *            纬度
     */
    private void selectLocation(Context context, double lat, double lng) {

        Intent intent = new Intent(context, LocationListActivity.class);
        if (lng != 0 && lat != 0) {
            intent.putExtra(LocationListActivity.LATITUDE, lat);
            intent.putExtra(LocationListActivity.LONGITUDE, lng);
            intent.putExtra("isShowSearch", true);
        }

        ((Activity) context).startActivityForResult(intent,
                SELECT_LOCATION_CODE);
    }

}
