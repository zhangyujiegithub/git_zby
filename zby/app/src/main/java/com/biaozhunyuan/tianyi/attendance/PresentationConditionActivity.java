package com.biaozhunyuan.tianyi.attendance;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.InputSoftHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;

import okhttp3.Request;

public class PresentationConditionActivity extends BaseActivity {

    private BoeryunHeaderView headerview;
    private EditText content;
    private int because;
    private String uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation_condition);
        initData();
        initView();
        setOnTouch();
    }

    private void initData() {
        Bundle extras = getIntent().getExtras();
        because = extras.getInt("because");
        uuid = extras.getString("uuid");
    }

    private void setOnTouch() {
        headerview.setmButtonClickRightListener(new BoeryunHeaderView.OnButtonClickRightListener() {
            @Override
            public void onRightTextClick() {
                send();
            }

            @Override
            public void onClickBack() {
                finish();
                InputSoftHelper.hideKeyboard(content);
            }

            @Override
            public void onClickFilter() {

            }

            @Override
            public void onClickSaveOrAdd() {

            }
        });
    }

    private void initView() {
        headerview = findViewById(R.id.boeryun_headerview);
        content = findViewById(R.id.et_content);
        if(because==1){
            headerview.setTitle("迟到原因");
        }else if(because == 2){
            headerview.setTitle("早退原因");
        }
    }
    public void send(){
        String str = content.getText().toString().trim();
        String url = Global.BASE_JAVA_URL + GlobalMethord.原因说明 + "?uuid=" + uuid + "&reason="+ str + "&reasonType=" + because;
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                finish();
                InputSoftHelper.hideKeyboard(content);
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                Toast.makeText(PresentationConditionActivity.this,"提交失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponseCodeErro(String result) {
                Toast.makeText(PresentationConditionActivity.this,"提交失败",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
