package com.biaozhunyuan.tianyi.chatLibary.chat.group;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;


import com.biaozhunyuan.tianyi.chatLibary.chat.model.GroupSession;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.example.chat.R;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

public class GroupManageActivity extends BaseActivity {

    private BoeryunHeaderView headerView;
    private Switch aSwitch;
    private GroupSession groupSession;
    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_manage);
        initViews();
        getIntentData();
        setOnEvent();
    }

    private void initViews() {
        aSwitch = findViewById(R.id.switch_view);
        headerView = findViewById(R.id.headerview);
    }

    private void getIntentData() {
        if (getIntent().getExtras() != null) {
            groupSession = (GroupSession) getIntent().getSerializableExtra("groupInfo");
            getGroupManagerAddAndDeleteStaffOnly();
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

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isFirst) {
                    setGroupManagerAddAndDeleteStaffOnly(isChecked);
                }
                isFirst = false;
            }
        });
    }

    private void getGroupManagerAddAndDeleteStaffOnly() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取群组是否只群主可以添加成员
                + "?groupId=" + groupSession.getChatId();

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Boolean checked = Boolean.valueOf(JsonUtils.pareseData(response));
                if (!checked) {
                    isFirst = false;
                }
                aSwitch.setChecked(checked);
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });
    }

    private void setGroupManagerAddAndDeleteStaffOnly(boolean managerOnly) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.设置群组是否只群主可以添加成员;

        Map<String, String> map = new HashMap<>();
        map.put("groupId", groupSession.getChatId());
        map.put("managerOnly", String.valueOf(managerOnly));
        StringRequest.postAsyn(url, map, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                showShortToast("设置成功");
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                showShortToast("设置失败:" + JsonUtils.pareseMessage(result));
            }
        });
    }
}
