package com.biaozhunyuan.tianyi.login.register;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.Md5Util;
import com.biaozhunyuan.tianyi.login.FindPasswordModel;
import com.biaozhunyuan.tianyi.login.ServiceItemActivity;
import com.biaozhunyuan.tianyi.common.utils.LogUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.utils.RegexUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;

import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Request;

/**
 * Created by wangAnMin on 2018/12/19.
 */

public class RegisterFirstActivity extends BaseActivity {
    private BoeryunHeaderView headerView;

    private EditText etPhone;
    private EditText etCode;//验证码
    private TextView tvGetCode;//获取验证码
    private TextView tvServiceTerms;//服务条款
    private Button btnNext;
    private CheckBox cbServiceTerms;


    private String phoneNum = "";

    private String validateCode = "";//验证码

    private Timer timer = new Timer();

    private int time = 120; //倒计时 时长
    private FindPasswordModel model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_first);

        initView();
        setOnEvent();
    }

    private void initView() {
        headerView = (BoeryunHeaderView) findViewById(R.id.header_find_pass_word);
        etPhone = (EditText) findViewById(R.id.et_phone_find_password);
        etCode = (EditText) findViewById(R.id.et_code_find_password);
        btnNext = (Button) findViewById(R.id.btn_next_step);
        tvGetCode = (TextView) findViewById(R.id.tv_get_code_find_password);
        tvServiceTerms = (TextView) findViewById(R.id.tv_service_terms);
        cbServiceTerms = (CheckBox) findViewById(R.id.checkbox_register_first);

        tvServiceTerms.setText("《天衣云服务条款》");
        tvServiceTerms.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
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

        tvGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEmpty()) {
                    checkPhoneNumber();
                }
            }
        });


        tvServiceTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterFirstActivity.this, ServiceItemActivity.class);
                startActivity(intent);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateCode = etCode.getText().toString().trim();
                phoneNum = etPhone.getText().toString();
                if (TextUtils.isEmpty(validateCode)) {
                    showShortToast("请输入验证码!");
                    return;
                }
                if (!cbServiceTerms.isChecked()) {
                    showShortToast("请阅读并同意天衣云服务条款!");
                    return;
                }
                checkCodeValidate();
            }
        });
    }


    /**
     * 获取验证码
     */
    private void getCode() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.发送验证码 + "?mobile=" + phoneNum + "&inCode=" + Md5Util.md5(phoneNum + "tysoft");

        StringRequest.getAsyn(url, new StringResponseCallBack() {

            @Override
            public void onResponse(String response) {
                showShortToast("验证码发送成功");
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                Toast.makeText(RegisterFirstActivity.this, "获取验证码失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponseCodeErro(String result) {
                LogUtils.i("RegisterFirstActivity", result);
                Toast.makeText(RegisterFirstActivity.this, "系统繁忙", Toast.LENGTH_SHORT).show();
            }
        });


    }


    /**
     * 检查手机号
     */
    private void checkPhoneNumber() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.注册检查手机号 + "?num=" + phoneNum;

        StringRequest.getAsyn(url, new StringResponseCallBack() {

            @Override
            public void onResponse(String response) {
                time = 120;
                countDown();
                getCode();
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
     * 检查验证码是否可用
     */
    private void checkCodeValidate() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.注册检查验证码是否正确
                + "?mobile=" + phoneNum + "&validateCode=" + validateCode;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Intent intent = new Intent(RegisterFirstActivity.this, RegisterSecondActivity.class);
                intent.putExtra("PhoneNum", phoneNum);
                intent.putExtra("validateCode", validateCode);
                startActivity(intent);
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
     * 倒计时
     */
    private void countDown() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (time > 0) {
                            time--;
                            tvGetCode.setText("重新获取(" + time + ")");
                            tvGetCode.setTextColor(Color.GRAY);
                            tvGetCode.setEnabled(false);
                        } else if (time == 0) {
                            tvGetCode.setEnabled(true);
                            tvGetCode.setText("获取验证码");
                            tvGetCode.setTextColor(getResources().getColor(R.color.color_tag_text_activated));
                        }
                    }
                });
            }
        };
        timer.schedule(task, 0, 1000);
    }

    private boolean isEmpty() {
        phoneNum = etPhone.getText().toString();

        if (TextUtils.isEmpty(phoneNum)) {
            Toast.makeText(RegisterFirstActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return true;
        } else if (!RegexUtils.isMobile(phoneNum)) {
            Toast.makeText(RegisterFirstActivity.this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }



}
