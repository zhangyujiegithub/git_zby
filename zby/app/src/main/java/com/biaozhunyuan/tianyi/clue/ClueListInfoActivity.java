package com.biaozhunyuan.tianyi.clue;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.model.字典;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.ParseException;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.InputSoftHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.common.view.DictIosPickerBottomDialog;
import com.biaozhunyuan.tianyi.view.TimePickerView;

import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Request;

public class ClueListInfoActivity extends BaseActivity {

    public static String CLUE_LISTINFO_EXTRA = "CLUE_LISTINFO_EXTRA";
    private TextView tv_come;        //来源
    private TextView tv_classify;    //分类
    private TextView tv_product;     //意向产品
    private TextView tv_time;        //预计采购时间
    private EditText et_people;      //联系人
    private EditText et_phone;       //联系电话
    private EditText et_companyname; //公司名称
    private EditText et_amount;      //金额
    private EditText et_remark;      //备注
    private String dict_customer_source = "dict_customer_source"; //来源
    private String dict_customer_type = "dict_customer_type";     //分类
    private String dict_salechance_inproduct = "inv_sku"; //意向产品
    private DictIosPickerBottomDialog dialog_come;//来源
    private DictIosPickerBottomDialog dialog_classify;//分类
    private DictIosPickerBottomDialog dialog_product;//意向产品
    private TimePickerView pickerView;
    private Clue mClue = new Clue();
    private Context mContext;
    private Activity mActivity;
    private BoeryunHeaderView headerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clue_list_info);
        initView();
        initData();
        getIntentData();
        setOnTouchEvent();
    }


    private void initData() {
        mContext = this;
        mActivity = this;
        pickerView = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY);
        pickerView.setTime(new Date());
        pickerView.setCyclic(true);
        pickerView.setCancelable(true);

        dialog_come = new DictIosPickerBottomDialog(mContext);
        dialog_classify = new DictIosPickerBottomDialog(mContext);
        dialog_product = new DictIosPickerBottomDialog(mContext);
    }

    private void setOnTouchEvent() {
        //来源
        tv_come.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_come.show(dict_customer_source, true);
                dialog_come.setOnSelectedDictListener(new DictIosPickerBottomDialog.onSelectDictListener() {
                    @Override
                    public void onSelectedDict(字典 dict) {
                        tv_come.setText(dict.getName());
                        mClue.setSource(dict.getUuid());
                    }
                });
            }
        });
        //分类
        tv_classify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_classify.show(dict_customer_type, true);
                dialog_classify.setOnSelectedDictListener(new DictIosPickerBottomDialog.onSelectDictListener() {
                    @Override
                    public void onSelectedDict(字典 dict) {
                        tv_classify.setText(dict.getName());
                        mClue.setClassification(dict.getUuid());
                    }
                });
            }
        });
        //意向产品
        tv_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_product.show(dict_salechance_inproduct, true);
                dialog_product.setOnSelectedDictListener(new DictIosPickerBottomDialog.onSelectDictListener() {
                    @Override
                    public void onSelectedDict(字典 dict) {
                        tv_product.setText(dict.getName());
                        mClue.setInProduct(dict.getUuid());
                    }
                });
            }
        });
        //预计采购时间
        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputSoftHelper.hideKeyboard(tv_time);
                pickerView.show();
                pickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
                    @SuppressLint("SimpleDateFormat")
                    @Override
                    public void onTimeSelect(Date date) {
                        String time = new SimpleDateFormat("yyyy-MM-dd").format(date);
                        mClue.setExpectPurshTime(time);
                        tv_time.setText(time);
                    }
                });
            }
        });
        headerview.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickRightListener() {
            @Override
            public void onRightTextClick() {
                if (isCanSave()) {
                    saveClue();
                }
            }

            @Override
            public void onClickBack() {
                finish();
            }

            @Override
            public void onClickFilter() {

            }

            @Override
            public void onClickSaveOrAdd() {

            }
        });

    }

    /**
     * 保存线索
     */
    private void saveClue() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.线索保存;
        StringRequest.postAsyn(url, mClue, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                showShortToast(JsonUtils.pareseData(response));
                ClueListActivity.isResume = true;
                finish();
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });
    }

    /**
     * 判断必填项是否为空 (暂无必填项)
     *
     * @return
     */
    private boolean isCanSave() {
        String amount = et_amount.getText().toString();
        String companyname = et_companyname.getText().toString();
        String people = et_people.getText().toString();
        String remark = et_remark.getText().toString();
        String phone = et_phone.getText().toString();

        mClue.setContactPer(people);
        mClue.setContactPho(phone);
        mClue.setCompanyName(companyname);
        mClue.setRemark(remark);
        mClue.setExpectMoney(amount);
        return true;
    }

    private void getIntentData() {
//        if (getIntent().getSerializableExtra(CLUE_LISTINFO_EXTRA) != null) {
//            mClue = (Clue) getIntent().getSerializableExtra(CLUE_LISTINFO_EXTRA);
//            headerview.setTitle("编辑线索");
//            setViewData();
//        } else {
//            headerview.setTitle("新建线索");
//            mClue.setUuid("0");
//        }
        if (getIntent().getSerializableExtra(CLUE_LISTINFO_EXTRA) != null) {
            String exturaUrl = (String) getIntent().getSerializableExtra(CLUE_LISTINFO_EXTRA);
            getClueInfo(exturaUrl);
        }
    }

    private void getClueInfo(String exturaUrl) {
        StringRequest.getAsyn(exturaUrl, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    mClue = JsonUtils.jsonToEntity(JsonUtils.pareseData(response),Clue.class);
                    if(mClue!=null){
                        setViewData();
                    }
                } catch (ParseException e) {
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

    /**
     * 从列表传进详情的数据回显
     */
    private void setViewData() {
        et_people.setText(mClue.getContactPer());
        et_phone.setText(mClue.getContactPho());
        et_companyname.setText(mClue.getCompanyName());
        et_remark.setText(mClue.getRemark());
        et_amount.setText(mClue.getExpectMoney());
        tv_product.setText(mClue.getInProductName());
        tv_classify.setText(mClue.getClassificationName());
        tv_come.setText(mClue.getSourceName());
        tv_time.setText(ViewHelper.getDateStringFormat(mClue.getExpectPurshTime(), "yyyy-MM-dd"));
    }

    private void initView() {
        headerview = findViewById(R.id.boeryun_headerview);
        tv_come = findViewById(R.id.tv_come);              //来源
        tv_classify = findViewById(R.id.tv_classify);      //分类
        tv_product = findViewById(R.id.tv_product);        //意向产品
        tv_time = findViewById(R.id.tv_time);              //预计采购时间
        et_people = findViewById(R.id.et_people);          //联系人
        et_phone = findViewById(R.id.et_phone);            //联系电话
        et_companyname = findViewById(R.id.et_companyname);//公司名称
        et_amount = findViewById(R.id.et_amount);          //金额
        et_remark = findViewById(R.id.et_remark);          //备注
    }

}
