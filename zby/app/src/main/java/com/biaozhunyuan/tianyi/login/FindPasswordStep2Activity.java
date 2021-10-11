package com.biaozhunyuan.tianyi.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;

import okhttp3.Request;

/**
 * <p/>
 * 忘记密码的第二步 :输入新密码
 */

public class FindPasswordStep2Activity extends AppCompatActivity {


    private BoeryunHeaderView headerView;
    private EditText etPwd;
    private EditText etConfimPwd;
    private Button btnFinish;

    private String pwd;
    private String confirmPwd;

    private FindPasswordModel mModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password_step2);
        initData();
        initView();
        setOnEvent();

    }

    private void initView() {
        headerView = (BoeryunHeaderView) findViewById(R.id.header_find_pass_word_step_2);
        etPwd = (EditText) findViewById(R.id.et_password_find_password);
        etConfimPwd = (EditText) findViewById(R.id.et_confirm_password_find_password);
        btnFinish = (Button) findViewById(R.id.btn_finish_password);
    }

    private void initData() {
        if (getIntent().getExtras() != null) {
            mModel = (FindPasswordModel) getIntent().getExtras().getSerializable("findPassword");
        }
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

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEmpty()) {
                    findPwd();
                }
            }
        });
    }

    /**
     * 修改密码
     */
    private void findPwd() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.更改密码;

        if (mModel != null) {
            mModel.setPassword(pwd);
            mModel.setVerifyCode(mModel.getValidateCode());
        }

        StringRequest.postAsyn(url, mModel, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(FindPasswordStep2Activity.this, "设置密码成功！", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FindPasswordStep2Activity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                Toast.makeText(FindPasswordStep2Activity.this, request.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponseCodeErro(String result) {
                Toast.makeText(FindPasswordStep2Activity.this, JsonUtils.parseLoginMessage(result), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean isEmpty() {

        pwd = etPwd.getText().toString();
        confirmPwd = etConfimPwd.getText().toString();

        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(FindPasswordStep2Activity.this, "请输入密码", Toast.LENGTH_SHORT).show();
            return true;
        } else if(pwd.length() <6 ){
            Toast.makeText(FindPasswordStep2Activity.this, "密码长度不能小于6位", Toast.LENGTH_SHORT).show();
            return true;
        } else if (TextUtils.isEmpty(confirmPwd)) {
            Toast.makeText(FindPasswordStep2Activity.this, "请再次输入密码", Toast.LENGTH_SHORT).show();
            return true;
        } else if (!pwd.equals(confirmPwd)) {
            Toast.makeText(FindPasswordStep2Activity.this, "两次输入密码不一致,请重新输入", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }


}
