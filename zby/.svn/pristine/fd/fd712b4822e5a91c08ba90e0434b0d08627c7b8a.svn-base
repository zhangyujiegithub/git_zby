package com.biaozhunyuan.tianyi.newuihome;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.models.StaffModel;

import okhttp3.Request;

/**
 * 作者： bohr
 * 日期： 2020-07-15 18:54
 * 描述：员工详情页面
 */
public class StaffInfoActivity extends BaseActivity {

    private BoeryunHeaderView header;
    private TextView tvCompany;//单位名称
    private TextView tvName;//姓名
    private TextView tvDept;//部门
    private TextView tvPosition;//职务
    private TextView tvWorkPhone;//工作电话
    private TextView tvTotalPhone;//总机
    private TextView tvPartPhone;//分机
    private TextView tvEmail;//电子邮件
    private TextView tvMobile;//手机号
    private TextView tvHomePhone;//家庭电话

    private StaffModel staffModel;

    private String department = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_info);
        initView();
        getIntentData();
    }

    private void getIntentData() {
        if (getIntent().getExtras() != null) {
            staffModel = (StaffModel) getIntent().getSerializableExtra("StaffInfo");
            department =  getIntent().getStringExtra("department");
            if (staffModel != null) {
                initData();
            }
            if(department != null){
                tvDept.setText(department);
            }
        }
    }

    private void initView() {
        header = findViewById(R.id.header);
        tvCompany = findViewById(R.id.tv_company);
        tvName = findViewById(R.id.tv_name);
        tvDept = findViewById(R.id.tv_dept);
        tvPosition = findViewById(R.id.tv_position);
        tvWorkPhone = findViewById(R.id.tv_phone_work);
        tvTotalPhone = findViewById(R.id.tv_phone_total);
        tvPartPhone = findViewById(R.id.tv_phone_part);
        tvEmail = findViewById(R.id.tv_email);
        tvMobile = findViewById(R.id.tv_mobile);
        tvHomePhone = findViewById(R.id.tv_phone_home);

        header.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickListener() {
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

    private void initData() {
        getDept();


        tvCompany.setText("中国标准化研究院");
//        tvCompany.setText(staffModel.getCorpName());
//        tvDept.setText(staffModel.getCorpName());
        tvName.setText(staffModel.getName());
        tvPosition.setText(staffModel.getStation());
        tvWorkPhone.setText(staffModel.getWorkPhone());
//        tvTotalPhone.setText(staffModel.getSwitchboard());
        tvPartPhone.setText(staffModel.getTelephone());
        tvEmail.setText(staffModel.getEmail());
//        tvMobile.setText(staffModel.getMobilePhone());
        if(staffModel.getMobilePhone() != null && staffModel.getMobilePhone().length() > 10)
            tvTotalPhone.setText(staffModel.getMobilePhone().substring(0, 3) + "****" + staffModel.getMobilePhone().substring(7, 11));
        tvHomePhone.setText(staffModel.getFamilyPhone());
    }


    private void getDept() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取字典2 + "?tableName=base_department&ids=" + Global.mUser.getDepartmentId();

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                if (!TextUtils.isEmpty(JsonUtils.pareseData(response))) {
//                    tvDept.setText(JsonUtils.pareseData(response));
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
}
