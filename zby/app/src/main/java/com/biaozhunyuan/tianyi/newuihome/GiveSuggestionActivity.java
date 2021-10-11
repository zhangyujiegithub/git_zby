package com.biaozhunyuan.tianyi.newuihome;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;

public class GiveSuggestionActivity extends AppCompatActivity {
    private Context mContext;

//    private 意见反馈 mOpinion;
    private Toast toast;
    private EditText etContent;
    private EditText etContact;
    private BoeryunHeaderView headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_suggestion);
        initData();
        initViews();
        setOnEvent();
    }
    private void initData() {
//        mContext = GiveSuggestionActivity.this;
//        mOpinion = new 意见反馈();
    }
    private void initViews() {
        headerView = findViewById(R.id.boeryun_headerview);
        etContent = (EditText) findViewById(R.id.et_content_suggestion);
        etContact = (EditText) findViewById(R.id.et_contact_suggestions);
    }
    public void setOnEvent(){
        headerView.setmButtonClickRightListener(new BoeryunHeaderView.OnButtonClickRightListener() {
            @Override
            public void onRightTextClick() {
                String content = etContent.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    showShortToast("请填写反馈意见");
                } else if (content.length() <= 10) {
                    showShortToast("反馈意见内容不少于10字");
                } else {
                    showShortToast("提交成功");
                    finish();
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
    private void saveOpinion(String content) {


    }
    /**
     * 弹出短Toast提示信息
     */
    protected void showShortToast(String info) {
        if (toast == null) {
            toast = Toast.makeText(this, info, Toast.LENGTH_SHORT);
        } else {
            toast.setText(info);
        }
        toast.show();
    }
}
