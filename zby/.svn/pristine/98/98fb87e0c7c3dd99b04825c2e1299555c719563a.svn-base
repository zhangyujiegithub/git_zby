package com.biaozhunyuan.tianyi.chatLibary.chat.group;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.example.chat.R;


/**
 * 修改群组名称页面
 */
public class UpdateGroupNameActivity extends BaseActivity {

    private BoeryunHeaderView headerView;
    private EditText etName;
    private ImageView ivClear;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_group_name);
        mContext = UpdateGroupNameActivity.this;
        initViews();
        getIntentData();
    }

    private void initViews() {
        headerView = findViewById(R.id.headerview);
        etName = findViewById(R.id.et_name);
        ivClear = findViewById(R.id.iv_clear);

        headerView.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickRightListener() {
            @Override
            public void onRightTextClick() {
                String name = etName.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    showShortToast("群名称不能为空");
                    return;
                }
                Intent intent = new Intent(mContext, GroupInfoActivity.class);
                intent.putExtra("GroupName", name);
                setResult(Activity.RESULT_OK, intent);
                finish();
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

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String name = s.toString();
                if (name.length() > 30) {
                    showShortToast("群名称最长不能超过30个字符");
                    name = name.substring(0, 30);
                    etName.setText(name);
                }
            }
        });


        ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etName.setText("");
            }
        });

    }

    private void getIntentData() {
        if (getIntent().getExtras() != null) {
            String name = getIntent().getStringExtra("groupName");
            etName.setText(name);
            etName.setSelection(etName.getText().toString().length());
        }
    }
}
