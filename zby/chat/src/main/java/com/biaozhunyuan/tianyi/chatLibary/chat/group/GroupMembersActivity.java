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
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.model.user.UserList;
import com.biaozhunyuan.tianyi.common.view.AlertDialog;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.common.view.BoeryunSearchViewNoButton;
import com.example.chat.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GroupMembersActivity extends BaseActivity {


    private BoeryunHeaderView headerView;
    private BoeryunSearchViewNoButton searchView;
    private LinearLayout bottom_select;
    private TextView tvSize;
    private TextView tv_selected_text;
    private TextView tvReturn;
    private ListView lv;

    private Context mContext;
    private boolean isDeleteMembers = false;
    private List<GroupMembers> members;
    private CommanAdapter<GroupMembers> adapter;
    private List<GroupMembers> selectMembers;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_members);
        initViews();
        getIntentData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            Bundle bunble = data.getExtras();
            UserList userList1 = (UserList) bunble.getSerializable("newseleteduser");
            selectMembers = turnToMemberList(userList1.getUsers());
            for (GroupMembers member : members) {
                member.setSelected(false);
                for (GroupMembers selectMember : selectMembers) {
                    if (selectMember.getUuid().equals(member)) {
                        member.setSelected(true);
                    }
                }
            }
            adapter.notifyDataSetChanged();
            tvSize.setText(selectMembers.size() + "人");
        }
    }

    private void getIntentData() {
        if (getIntent().getExtras() != null) {
            isDeleteMembers = getIntent().getBooleanExtra("isDeleteMembers", false);
            members = (List<GroupMembers>) getIntent().getSerializableExtra("groupMembers");
            if (members == null) {
                members = new ArrayList<>();
            }
            initData();
        }
    }

    private void initViews() {
        mContext = GroupMembersActivity.this;
        headerView = findViewById(R.id.headerview);
        bottom_select = findViewById(R.id.bottom_select);
        tvSize = findViewById(R.id.tv_count_select_user);
        tv_selected_text = findViewById(R.id.tv_selected_text);
        tvReturn = findViewById(R.id.tv_sure_select_user);
        searchView = findViewById(R.id.search_view);
        lv = findViewById(R.id.lv_list);
        dialog = new AlertDialog(mContext).builder();
    }

    private void initData() {
        selectMembers = new ArrayList<>();
        if (isDeleteMembers) {
            headerView.setTitle("移除群成员");
            bottom_select.setVisibility(View.VISIBLE);
        } else {
            headerView.setTitle("群成员(" + members.size() + "人)");
            bottom_select.setVisibility(View.GONE);
        }
        adapter = getAdapter(members);
        lv.setAdapter(adapter);

        setOnEvent();
    }

    private void setOnEvent() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GroupMembers item = (GroupMembers) lv.getItemAtPosition(position);
                if (isDeleteMembers) {
                    item.setSelected(!item.isSelected());
                    if (item.isSelected()) {
                        selectMembers.add(item);
                    } else {
                        selectMembers.remove(item);
                    }
                    tvSize.setText(selectMembers.size() + "人");
                    adapter.notifyDataSetChanged();
                } else {
                    Intent intent = new Intent(mContext, UserInfoActivity.class);
                    intent.putExtra("userId", item.getUuid());
                    startActivity(intent);
                }
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
                startSelectUserAcitivity();
            }
        });

        tvSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSelectUserAcitivity();
            }
        });

        tvReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectMembers.size() > 0) {
                    String deleteName = "";
                    if (selectMembers.size() > 3) {
                        for (GroupMembers selectMember : selectMembers.subList(0, 3)) {
                            deleteName += selectMember.getName() + ",";
                        }
                    } else {
                        for (GroupMembers selectMember : selectMembers) {
                            deleteName += selectMember.getName() + ",";
                        }
                    }
                    if (deleteName.length() > 0) {
                        deleteName = deleteName.substring(0, deleteName.length() - 1);
                        if (selectMembers.size() > 3) {
                            deleteName += "等";
                        }
                    }
                    if (selectMembers.size() == 1) {
                        dialog.setMsg("确定要删除群成员" + deleteName + "吗？");
                    } else {
                        dialog.setMsg("确定要删除" + deleteName + selectMembers.size() + "个群成员吗？");
                    }
                    dialog.setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            returnResult();
                        }
                    }).setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dissMiss();
                        }
                    }).show();
                } else {
                    returnResult();
                }
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
                    lv.setAdapter(getAdapter(list));
                } else {
                    lv.setAdapter(getAdapter(members));
                }
            }
        });
    }

    private void returnResult() {
        Intent intent = new Intent();
        intent.putExtra("deleteUsers", (Serializable) selectMembers);
        setResult(Activity.RESULT_OK, intent);
        finish();
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
            public void convert(int position, GroupMembers item, BoeryunViewHolder viewHolder) {
                viewHolder.setUserPhoto(R.id.iv_head, item.getUuid());
                viewHolder.setTextValue(R.id.tv_name, item.getName());
                ImageView iv_select = viewHolder.getView(R.id.iv_select);
                if (isDeleteMembers) {
                    iv_select.setVisibility(View.VISIBLE);
                    if (item.isSelected()) {
                        iv_select.setImageResource(R.drawable.ic_select);
                    } else {
                        iv_select.setImageResource(R.drawable.select_off);
                    }
                } else {
                    iv_select.setVisibility(View.GONE);
                }

                if (item.isManager()) {
                    viewHolder.getView(R.id.tv_manager).setVisibility(View.VISIBLE);
                } else {
                    viewHolder.getView(R.id.tv_manager).setVisibility(View.GONE);
                }
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
