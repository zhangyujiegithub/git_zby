package com.biaozhunyuan.tianyi.login.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.login.LoginActivity;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.AlertDialog;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Request;

/**
 * Created by wangAnMin on 2018/12/19.
 */

public class RegisterSecondActivity extends BaseActivity {

    private Context mContext;

    private BoeryunHeaderView headerView;
    private EditText etCropName;
    private EditText etContactName;
    private EditText etPwd;
    private EditText etPwdConfirm;
    private EditText etInviteCode;
    private Button btnNextStep;

    private String phoneNum; //手机号码
    private String validateCode;//验证码
    private String cropName;//企业名称
    private String contactName;//联系人名称
    private String pwd;//密码
    private String pwdConfirm;//密码确认
    private String inviteCode;//邀请码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_second);
        mContext = RegisterSecondActivity.this;
        initViews();
        initData();
        setOnEvent();
    }

    private void initData() {
        if (getIntent().getExtras() != null) {
            phoneNum = getIntent().getStringExtra("PhoneNum");
            validateCode = getIntent().getStringExtra("validateCode");
        }
    }

    private void initViews() {
        headerView = findViewById(R.id.header_find_pass_word);
        etCropName = findViewById(R.id.et_register_sec_crop_name);
        etContactName = findViewById(R.id.et_register_sec_contact_name);
        etPwd = findViewById(R.id.et_register_sec_pwd);
        etPwdConfirm = findViewById(R.id.et_register_sec_pwd_confirm);
        etInviteCode = findViewById(R.id.et_register_sec_invite_code);
        btnNextStep = findViewById(R.id.btn_next_step);
    }

    private void setOnEvent() {
        headerView.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickListener() {
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

        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCanNext()) {
                    saveInfo();
                }
            }
        });
    }

    /**
     * 保存注册信息
     */
    private void saveInfo() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.注册保存注册信息;
        JSONObject jo = new JSONObject();
        try {
            jo.put("enterpriseName", cropName);
            jo.put("contractName", contactName);
            jo.put("password", pwd);
            jo.put("repassword", pwdConfirm);
            jo.put("mobilePhone", phoneNum);
            jo.put("validateCode", validateCode);
            jo.put("invitationCodeId", inviteCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringRequest.postAsyn(url, jo, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                showAlertDialog();
            }

            private void showAlertDialog() {
                AlertDialog dialog = new AlertDialog(mContext).builder()
                        .setTitle("注册成功")
                        .setMsg("您已注册成功，请登录")
                        .setCancelable(false)
                        .setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, LoginActivity.class);
                                intent.putExtra("corpName", cropName);
                                intent.putExtra("contacts", contactName);
                                intent.putExtra("pwd", pwd);
                                startActivity(intent);
                                finish();
                            }
                        });
                dialog.show();
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                showShortToast(JsonUtils.pareseData(result));
            }
        });
    }


    /**
     * 信息是否完整和正确
     */
    private boolean isCanNext() {
        cropName = etCropName.getText().toString();
        contactName = etContactName.getText().toString();
        pwd = etPwd.getText().toString();
        pwdConfirm = etPwdConfirm.getText().toString();
        inviteCode = etInviteCode.getText().toString();

        if (TextUtils.isEmpty(cropName)) {
            showShortToast("请输入企业名称");
            return false;
        }
        if (TextUtils.isEmpty(contactName)) {
            showShortToast("请输入联系人名称");
            return false;
        }
        if (TextUtils.isEmpty(pwd)) {
            showShortToast("请输入密码");
            return false;
        }
        if (pwd.length() < 6) {
            showShortToast("密码至少为6位");
            return false;
        }
        if (TextUtils.isEmpty(pwdConfirm)) {
            showShortToast("请再次输入密码");
            return false;
        }
        if (!pwd.equals(pwdConfirm)) {
            showShortToast("两次密码不一致");
            return false;
        }
        return true;
    }
}
