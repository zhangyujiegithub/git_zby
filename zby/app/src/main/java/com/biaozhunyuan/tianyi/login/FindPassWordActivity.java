package com.biaozhunyuan.tianyi.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.LogUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.utils.RegexUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;

import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Request;

/**
 * <p/>
 * 新的找回密码页面  发送验证码到手机，输入验证码验证
 */

public class FindPassWordActivity extends BaseActivity {

    private BoeryunHeaderView headerView;

    private EditText etCorpName;
    private EditText etUserName;
    private EditText etPhone;
    private EditText etCode;//验证码
    private TextView tvGetCode;//获取验证码
    private Button btnNext;


    private String corpName = "";
    private String phoneNum = "";
    private String userName = "";

    private String validateCode = "";//验证码

    private Timer timer = new Timer();

    private int time = 120; //倒计时 时长
    private FindPasswordModel model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pass_word);

        initView();
        initData();
        setOnEvent();
    }

    private void initData() {
        if (getIntent().getExtras() != null) {
            corpName = getIntent().getStringExtra("corpName");
            userName = getIntent().getStringExtra("userName");
            etCorpName.setText(corpName);
            etUserName.setText(userName);
        }
    }

    private void initView() {
        headerView = (BoeryunHeaderView) findViewById(R.id.header_find_pass_word);
        etCorpName = (EditText) findViewById(R.id.et_corpName_find_password);
        etPhone = (EditText) findViewById(R.id.et_phone_find_password);
        etUserName = (EditText) findViewById(R.id.et_userName_find_password);
        etCode = (EditText) findViewById(R.id.et_code_find_password);
        btnNext = (Button) findViewById(R.id.btn_next_step);
        tvGetCode = (TextView) findViewById(R.id.tv_get_code_find_password);
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
                    checkEnterpriseExit();
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEmpty()) {
                    validateCode = etCode.getText().toString().trim();
                    if (TextUtils.isEmpty(validateCode)) {
                        showShortToast("请输入验证码!");
                        return;
                    }
                    checkCodeValidate();
                }
            }
        });
    }

    /**
     * 判断验证码是否可用
     *
     * @return
     */
    private boolean isValidate() {

        if (TextUtils.isEmpty(etCode.getText().toString())) {
            return false;
        }
        if (etCode.getText().toString().equals(validateCode)) {
            return true;
        }
        return false;
    }


    /**
     * 获取验证码
     */
    private void getCode() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.发送验证码 + "?mobile=" + phoneNum;

        StringRequest.getAsyn(url, new StringResponseCallBack() {

            @Override
            public void onResponse(String response) {
                time = 120;
                countDown();
                showShortToast("验证码发送成功");
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                Toast.makeText(FindPassWordActivity.this, "获取验证码失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponseCodeErro(String result) {
                LogUtils.i("FindPassWordActivity", result);
                Toast.makeText(FindPassWordActivity.this, "系统繁忙", Toast.LENGTH_SHORT).show();
            }
        });


    }


    /**
     * 检查企业名是否存在
     */
    private void checkEnterpriseExit() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.企业是否存在 + "?enterpriseName=" + corpName + "&mobile=" + phoneNum;

        StringRequest.getAsyn(url, new StringResponseCallBack() {

            @Override
            public void onResponse(String response) {
                getCode();
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                String data = JsonUtils.pareseData(result);
                if(!TextUtils.isEmpty(data)){
                    showShortToast(data);
                } else {
                    showShortToast("该企业未注册");
                }
            }
        });


    }


    /**
     * 检查验证码是否可用
     */
    private void checkCodeValidate() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.校验验证码是否正确
                + "?mobile=" + phoneNum + "&verifyCode=" + validateCode + "&enterpriseName=" + corpName;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Intent intent = new Intent(FindPassWordActivity.this, FindPasswordStep2Activity.class);
                Bundle bundle = new Bundle();
                model = new FindPasswordModel();
                model.setValidateCode(validateCode);
                model.setEnterpriseName(corpName);
                model.setMobile(phoneNum);

                bundle.putSerializable("findPassword", model);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                showShortToast("验证码校验失败，请重新发送验证码");
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
        corpName = etCorpName.getText().toString();
        phoneNum = etPhone.getText().toString();
        userName = etUserName.getText().toString();

        if (TextUtils.isEmpty(corpName)) {
            Toast.makeText(FindPassWordActivity.this, "企业名不能为空", Toast.LENGTH_SHORT).show();
            return true;
        } else if (TextUtils.isEmpty(phoneNum)) {
            Toast.makeText(FindPassWordActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return true;
        } else if (!RegexUtils.isMobile(phoneNum)) {
            Toast.makeText(FindPassWordActivity.this, "手机格式不正确", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }


}
