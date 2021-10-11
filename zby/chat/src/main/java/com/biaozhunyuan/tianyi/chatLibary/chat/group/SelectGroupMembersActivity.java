package com.biaozhunyuan.tianyi.chatLibary.chat.group;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.biaozhunyuan.tianyi.chatLibary.chat.UserInfoActivity;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.GroupMembers;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.GroupSession;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.model.user.UserList;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.common.view.BoeryunSearchViewNoButton;
import com.example.chat.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

public class SelectGroupMembersActivity extends BaseActivity {


    private BoeryunHeaderView headerView;
    private BoeryunSearchViewNoButton searchView;
    private LinearLayout bottom_select;
    private TextView tvSize;
    private TextView tv_selected_text;
    private TextView tvReturn;
    private TextView tvSelectAll;
    private LinearLayout llSelectAll;
    private ListView lv;

    private Context mContext;
    private boolean isDeleteMembers = false;
    private List<GroupMembers> members = new ArrayList<>();
    private CommanAdapter<GroupMembers> adapter;
    private List<GroupMembers> selectMembers;
    private GroupSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_members);
        initViews();
        getIntentData();
    }

    private void getIntentData() {
        if (getIntent().getExtras() != null) {
            session = (GroupSession) getIntent().getExtras().getSerializable("GroupSession");
            headerView.setTitle("选择@的人");
            llSelectAll.setVisibility(View.VISIBLE);
            getAllMembers();
        }
    }

    private void initViews() {
        mContext = SelectGroupMembersActivity.this;
        headerView = findViewById(R.id.headerview);
        llSelectAll = findViewById(R.id.ll_select_all);
        bottom_select = findViewById(R.id.bottom_select);
        tvSize = findViewById(R.id.tv_count_select_user);
        tv_selected_text = findViewById(R.id.tv_selected_text);
        tvReturn = findViewById(R.id.tv_sure_select_user);
        tvSelectAll = findViewById(R.id.tv_select_all);
        searchView = findViewById(R.id.search_view);
        lv = findViewById(R.id.lv_list);

        llSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupMembers user = new GroupMembers();
                user.setName("所有人");
                selectMembers.clear();
                selectMembers.add(user);
                Intent intent = new Intent();
                intent.putExtra("selectUser", (Serializable) selectMembers);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    private void initData() {
        selectMembers = new ArrayList<>();
        adapter = getAdapter(members);
        lv.setAdapter(adapter);

        setOnEvent();
    }

    private void getAllMembers() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取群组成员 + "?groupId=" + session.getChatId();

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                members = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), GroupMembers.class);
                if (members != null) {
                    //@去除掉自己，不能@自己
                    for (int i = 0; i < members.size(); i++) {
                        if (Global.mUser.getUuid().equals(members.get(i).getUuid())) {
                            members.remove(i);
                            break;
                        }
                    }
                }
                initData();
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });
    }

    private void setOnEvent() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                GroupMembers item = adapter.getItem(position);
                item.setSelected(!item.isSelected());
                if (item.isSelected()) {
                    selectMembers.add(item);
                } else {
                    selectMembers.remove(item);
                }
                tvSize.setText(selectMembers.size() + "人");
                adapter.notifyDataSetChanged();
            }
        });

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

        tv_selected_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startSelectUserAcitivity();
            }
        });

        tvSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startSelectUserAcitivity();
            }
        });

        tvReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("selectUser", (Serializable) selectMembers);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        searchView.setOnSearchedListener(new BoeryunSearchViewNoButton.OnSearchedListener() {
            @Override
            public void OnSearched(String str) {
                if (!TextUtils.isEmpty(str)) {

                    List<GroupMembers> list = new ArrayList<>();
                    for (GroupMembers member : members) {
                        if (member.getName().contains(str)) {
                            list.add(member);
                        }
                    }
                    adapter = getAdapter(list);
                    lv.setAdapter(adapter);
                } else {
                    adapter = getAdapter(members);
                    lv.setAdapter(adapter);
                }
            }
        });
    }

    private void startSelectUserAcitivity() {
        ComponentName comp = new ComponentName(mContext, "com.biaozhunyuan.tianyi.notice.SelectedUserListActivity");
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        UserList userList = new UserList();
        userList.setUsers(turnToUserList(selectMembers));
        bundle.putSerializable("userlist", userList);
        intent.putExtras(bundle);
        intent.setComponent(comp);
        intent.setAction("android.intent.action.VIEW");
        startActivityForResult(intent, 200);
    }

    private CommanAdapter<GroupMembers> getAdapter(List<GroupMembers> list) {
        return new CommanAdapter<GroupMembers>(list, mContext, R.layout.item_group_member_list) {
            @Override
            public void convert(int position, final GroupMembers item, BoeryunViewHolder viewHolder) {
                viewHolder.setUserPhoto(R.id.iv_head, item.getUuid());
                viewHolder.setTextValue(R.id.tv_name, item.getName());
                ImageView iv_select = viewHolder.getView(R.id.iv_select);
                iv_select.setVisibility(View.VISIBLE);
                if (item.isSelected()) {
                    iv_select.setImageResource(R.drawable.ic_select);
                } else {
                    iv_select.setImageResource(R.drawable.ic_un_select);
                }

                if (item.isManager()) {
                    viewHolder.getView(R.id.tv_manager).setVisibility(View.VISIBLE);
                } else {
                    viewHolder.getView(R.id.tv_manager).setVisibility(View.GONE);
                }

                viewHolder.getView(R.id.iv_head).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, UserInfoActivity.class);
                        intent.putExtra("userId", item.getUuid());
                        startActivity(intent);
                    }
                });
            }
        };
    }

    private List<User> turnToUserList(List<GroupMembers> list) {
        List<User> users = new ArrayList<>();

        for (GroupMembers members : list) {
            User user = new User();
            user.setUuid(members.getUuid());
            user.setName(members.getName());
            users.add(user);
        }
        return users;
    }

    private List<GroupMembers> turnToMemberList(List<User> list) {
        List<GroupMembers> members = new ArrayList<>();

        for (User User : list) {
            GroupMembers members1 = new GroupMembers();
            members1.setUuid(User.getUuid());
            members1.setName(User.getName());
            members.add(members1);
            members1.setSelected(true);
        }
        return members;
    }
}
