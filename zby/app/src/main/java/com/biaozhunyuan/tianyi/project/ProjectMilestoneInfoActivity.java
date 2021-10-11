package com.biaozhunyuan.tianyi.project;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.view.LastInputEditText;
import com.biaozhunyuan.tianyi.view.TimePickerView;

import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Request;

/**
 * 里程碑详情
 */
public class ProjectMilestoneInfoActivity extends BaseActivity {

    private Project mProject;
    private Boolean isCanSave = false;
    private TextView tvName;//项目名称
    private BoeryunHeaderView headerview;
    private TextView tvJieduan;
    private LastInputEditText etMianji;
    private EditText etRemark;
    private TextView tvMounth;
    private TextView tvStatus;
    private TextView tvStatus1;
    private TimePickerView pickerView;
    private Context mContext;
    private String projectName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_milestone_info);
        getIntentData();
        initView();
        initData();
        setTouchEvent();
    }

    private void setTouchEvent() {
        headerview.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickRightListener() {
            @Override
            public void onRightTextClick() {
                saveMilestone();
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
        tvMounth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerView.show();
                pickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
                        String format = simpleDateFormat.format(date);
                        tvMounth.setText(format);
                        mProject.setMonth(format);
                    }
                });
            }
        });
    }

    private void saveMilestone() {
        mProject.setAdvisorId(Global.mUser.getUuid());
        mProject.setRemark(etRemark.getText().toString().trim());
        mProject.setPaintArea(etMianji.getText().toString().trim());
        String url = Global.BASE_JAVA_URL + GlobalMethord.里程碑保存;
        StringRequest.postAsyn(url, mProject, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                showShortToast("保存成功");
                ProjectMilestoneFragment.isReasume = true;
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

    private void initData() {
        mContext = this;
        pickerView = new TimePickerView(mContext, TimePickerView.Type.ALL);
        pickerView.setRange(1900,2100);
        pickerView.setTime(new Date());
        pickerView.setCyclic(true);
        pickerView.setCancelable(true);

        if(isCanSave){
            headerview.setRightTitleVisible(true);
        }else {
            headerview.setRightTitleVisible(false);
        }
        try {
            tvName.setText(projectName);
            tvJieduan.setText(mProject.getStageName());
            tvMounth.setText(mProject.getMonth());
            etMianji.setText(mProject.getPaintArea());
            tvStatus.setText(mProject.getStatusName());
            etRemark.setText(mProject.getRemark());
            tvStatus1.setText(mProject.getProjectStatusName());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initView() {
        headerview = findViewById(R.id.boeryun_headerview);
        tvName = findViewById(R.id.tv_project_name);
        tvJieduan = findViewById(R.id.tv_project_jieduan);
        tvStatus1 = findViewById(R.id.tv_project_status1);
        etMianji = findViewById(R.id.et_project_mianji);
        tvMounth = findViewById(R.id.tv_project_mounth);
        tvStatus = findViewById(R.id.tv_project_status);
        etRemark = findViewById(R.id.et_project_remark);
    }

    private void getIntentData() {
        if(getIntent().getSerializableExtra("Project") !=null){
            mProject = (Project) getIntent().getSerializableExtra("Project");
            isCanSave = getIntent().getBooleanExtra("isCanSave",false);
            projectName = getIntent().getStringExtra("projectName");
        }
    }
}
