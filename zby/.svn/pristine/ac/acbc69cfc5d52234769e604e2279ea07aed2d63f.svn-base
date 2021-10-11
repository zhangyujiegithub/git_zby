package com.biaozhunyuan.tianyi.chatLibary.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;


import com.biaozhunyuan.tianyi.chatLibary.chat.model.GroupModel;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.GroupSession;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.common.view.NoScrollListView;
import com.example.chat.R;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * 群组会话列表（部门聊天和普通群组聊天)
 */
public class GroupSessionActivity extends BaseActivity {

    private BoeryunHeaderView headerView;
    private NoScrollListView lv_dept;
    private NoScrollListView lv_group;
    private TextView tvDept;
    private TextView tvGroup;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_session);
        mContext = GroupSessionActivity.this;
        initViews();
        getGroupSessionList();
        setOnEvent();
    }

    private void initViews() {
        headerView = findViewById(R.id.headerview);
        lv_dept = findViewById(R.id.lv_dept);
        lv_group = findViewById(R.id.lv_group);
        tvDept = findViewById(R.id.tv_dept);
        tvGroup = findViewById(R.id.tv_group);
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


        lv_dept.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GroupModel model = (GroupModel) lv_dept.getItemAtPosition(position);
                GroupSession session = new GroupSession();
                session.setChatId(model.getUuid());
                session.setName(model.getName());
                session.setDepartMent(1);
                session.setAvatar(model.getIcon());
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("chatUser", session);
                startActivity(intent);
            }
        });

        lv_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GroupModel model = (GroupModel) lv_group.getItemAtPosition(position);
                GroupSession session = new GroupSession();
                session.setChatId(model.getUuid());
                session.setName(model.getName());
                session.setAvatar(model.getIcon());
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("chatUser", session);
                startActivity(intent);
            }
        });
    }


    /**
     * 获取群组会话列表
     */
    private void getGroupSessionList() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取会话列表 + "?isSingle=0";

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<GroupModel> groupModels = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), GroupModel.class);
                if (groupModels != null && groupModels.size() > 0) {
                    List<GroupModel> deptGroups = new ArrayList<>();
                    List<GroupModel> tempGroups = new ArrayList<>();
                    for (GroupModel groupModel : groupModels) {
                        if (groupModel.getIsDepartment() == 1) {
                            deptGroups.add(groupModel);
                        } else {
                            tempGroups.add(groupModel);
                        }
                    }

                    if (deptGroups.size() > 0) {
                        tvDept.setVisibility(View.VISIBLE);
                        lv_dept.setAdapter(getAdapter(deptGroups));
                    }
                    if (tempGroups.size() > 0) {
                        tvGroup.setVisibility(View.VISIBLE);
                        lv_group.setAdapter(getAdapter(tempGroups));
                    }

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


    private CommanAdapter<GroupModel> getAdapter(List<GroupModel> list) {
        return new CommanAdapter<GroupModel>(list, mContext, R.layout.item_group_session) {
            @Override
            public void convert(int position, GroupModel item, BoeryunViewHolder viewHolder) {
                viewHolder.setImageById(R.id.iv_head, item.getIcon());
                viewHolder.setTextValue(R.id.tv_name, item.getName());
                if (!TextUtils.isEmpty(item.getStaffCount())) {
                    viewHolder.setTextValue(R.id.tv_count, "(" + item.getStaffCount() + ")");
                } else {
                    viewHolder.setTextValue(R.id.tv_count, "");
                }
                TextView tvTag = viewHolder.getView(R.id.tv_tag);
                if (item.getIsDepartment() == 1) {
                    tvTag.setVisibility(View.VISIBLE);
                } else {
                    tvTag.setVisibility(View.GONE);
                }
            }
        };
    }
}
