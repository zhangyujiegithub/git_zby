
package com.biaozhunyuan.tianyi.chatLibary.chat.group;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;


import com.biaozhunyuan.tianyi.chatLibary.chat.ChatActivity;
import com.biaozhunyuan.tianyi.chatLibary.chat.UserInfoActivity;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.GroupMembers;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.GroupModel;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.GroupSession;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.RecentMessage;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.base.ParseException;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.SharedPreferencesHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.model.user.UserList;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.common.view.NoScrollGridView;
import com.example.chat.R;


import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.Request;

/**
 * Created by wangAnMin on 2019/3/5.
 * 群组详情信息页面（群组成员，群组名称，修改群组名称..)
 */

public class GroupInfoActivity extends BaseActivity {

    private final int REQUEST_CODE_SELECT_MEMBER = 101;
    private final int REQUEST_CODE_DELETE_MEMBER = 102;
    private final int REQUEST_CODE_UPDATE_NAME = 103;
    private Context mContext;
    private String groupNames;
    private GroupSession groupSession;
    //    private AlertDialog alertDialog;
    private boolean isClearAllMessage = false;
    private boolean isDepartment = false;
    private boolean isTop = false;
    private SharedPreferencesHelper preferencesHelper;


    private BoeryunHeaderView headerView;
    private NoScrollGridView gridView;
    private TextView tvGroupName;//群聊名称
    private TextView tvNumber;//群聊人数
    private LinearLayout rlMsgRecord; //消息记录
    private TextView tvClearAll; //清空聊天记录
    private TextView tvQuiteGroup; //删除并退出群组
    private LinearLayout llMembers;
    private LinearLayout llGroupImage; //群聊图片
    private LinearLayout llGroupFile; //群聊文件
    private LinearLayout llUpdateName; //修改群名称
    private LinearLayout llSetTop; //设置置顶
    private LinearLayout llSetNoInterrupt; //设置免打扰
    private TextView tvDeptTag;
    private ImageView ivArrowName;
    private Switch switchTop;
    private Switch switchNoInterrupt; //消息免打扰
    private RelativeLayout rlGroupManage;
    private List<GroupMembers> groupMembers;
    private GroupModel mGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        initViews();
        getIntentData();
        setOnEvent();
    }


    /**
     * 获取跳转页面所携带过来的数据
     */
    private void getIntentData() {
        if (getIntent().getExtras() != null) {
            groupSession = (GroupSession) getIntent().getSerializableExtra("GroupSession");
            if (groupSession != null) {
                initData();
                getGroupInfo();
            }
        }
    }


    /**
     * 初始化view
     */
    private void initViews() {
        headerView = findViewById(R.id.headerview_group_info);
        gridView = findViewById(R.id.grid_group_members);
        tvGroupName = findViewById(R.id.tv_group_name);
        tvNumber = findViewById(R.id.tv_nmb_member);
        rlMsgRecord = findViewById(R.id.rl_message_record);
        tvClearAll = findViewById(R.id.clear_all_message);
        llMembers = findViewById(R.id.ll_members);
        llGroupImage = findViewById(R.id.ll_group_image);
        llGroupFile = findViewById(R.id.ll_group_file);
        llUpdateName = findViewById(R.id.ll_update_name);
        ivArrowName = findViewById(R.id.iv_arrow_name);
        tvDeptTag = findViewById(R.id.tv_dept_tag);
        llSetTop = findViewById(R.id.ll_set_top);
        llSetNoInterrupt = findViewById(R.id.ll_set_no_interrupt);
        switchTop = findViewById(R.id.switch_top);
        switchNoInterrupt = findViewById(R.id.switch_no_interrupt);
        tvQuiteGroup = findViewById(R.id.quit_group);
        rlGroupManage = findViewById(R.id.rl_group_manage);
    }


    /**
     * 初始化数据
     */
    private void initData() {
        mContext = GroupInfoActivity.this;
        preferencesHelper = new SharedPreferencesHelper(mContext);
        tvGroupName.setText(groupSession.getName());//群聊名称
        switchNoInterrupt.setChecked(groupSession.isSetNoInterrupt());
    }


    /**
     * 点击事件处理
     */
    private void setOnEvent() {
        headerView.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickListener() {
            @Override
            public void onClickBack() {
                setOnResult();
            }

            @Override
            public void onClickFilter() {

            }

            @Override
            public void onClickSaveOrAdd() {

            }
        });

        /**
         * 弹出修改群聊名称的弹出框
         */
        llUpdateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UpdateGroupNameActivity.class);
                intent.putExtra("groupName", groupSession.getName());
                startActivityForResult(intent, REQUEST_CODE_UPDATE_NAME);
            }
        });

        rlMsgRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GroupSearchActivity.class);
                intent.putExtra("groupInfo", groupSession);
                startActivity(intent);
            }
        });


        /**
         * 清空聊天记录
         */
        tvClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearMessage();
                //清除会话的聊天消息
                HashMap<String, RecentMessage> hashMapData = preferencesHelper.getHashMapData
                        (Global.mUser.getUuid() + "ChatRecord", RecentMessage.class);
                hashMapData.remove(groupSession.getChatId());
                preferencesHelper.putHashMapData(Global.mUser.getUuid() + "ChatRecord", hashMapData);


                //清除会话列表的最后一条消息
                List<GroupSession> sessions = preferencesHelper.getListData
                        (Global.mUser.getCorpId() + Global.mUser.getUuid(), GroupSession.class);
                for (GroupSession session : sessions) {
                    if (groupSession.getChatId().equals(session.getChatId())) {
                        session.setLastMessage("");
                        break;
                    }
                }
                preferencesHelper.putListData(Global.mUser.getCorpId() + Global.mUser.getUuid(), sessions);
                showShortToast("清除成功");
                isClearAllMessage = true;
            }
        });

        /**
         * 删除并退出群组
         */

        tvQuiteGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quiteGroup(groupSession.getChatId());
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, UserInfoActivity.class);
                GroupMembers item = (GroupMembers) gridView.getItemAtPosition(position);
                intent.putExtra("userId", item.getUuid());
                startActivity(intent);
            }
        });


        /**
         * 查看群成员
         */
        llMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GroupMembersActivity.class);
                intent.putExtra("isDeleteMembers", false);
                intent.putExtra("groupMembers", (Serializable) groupMembers);
                startActivity(intent);
            }
        });


        llGroupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GroupImageActivity.class);
                intent.putExtra("groupInfo", groupSession);
                startActivity(intent);
            }
        });

        llGroupFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GroupFileActivity.class);
                intent.putExtra("groupInfo", groupSession);
                startActivity(intent);
            }
        });


        rlGroupManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GroupManageActivity.class);
                intent.putExtra("groupInfo", groupSession);
                startActivity(intent);
            }
        });

        switchTop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchTop.setEnabled(false);
                if (isChecked) {
                    setGroupTop();
                } else {
                    cancelGroupTop();
                }
            }
        });


        //消息免打扰/取消消息免打扰
        switchNoInterrupt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchNoInterrupt.setEnabled(false);
                if (isChecked) {
                    closeMessageNotice();
                } else {
                    openMessageNotice();
                }
            }
        });


    }

    private void setOnResult() {
        Intent intent = new Intent(mContext, ChatActivity.class);
        intent.putExtra("isClearAllMessage", isClearAllMessage);
        if (!TextUtils.isEmpty(groupNames)) {
            intent.putExtra("groupNames", groupNames);
        }
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setOnResult();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 更新群组名称
     *
     * @param groupName 群组新的名称
     */
    private void updateGroupName(final String groupName) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.修改群组名称 + "?groupId="
                + groupSession.getChatId() + "&groupName=" + groupName;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                showShortToast("修改成功");
                tvGroupName.setText(groupName);
                groupSession.setName(groupName);
                groupNames = groupName;
                /**
                 * 更新本地群组名称
                 */
                List<GroupSession> sessions = preferencesHelper.getListData(Global.mUser.getCorpId() + Global.mUser.getUuid(), GroupSession.class);
                for (GroupSession session : sessions) {
                    if (session.getChatId() == null)
                        continue;
                    if (session.getChatId().equals(groupSession.getChatId())) {
                        session.setName(groupName);
                        break;
                    }
                }
                preferencesHelper.putListData(Global.mUser.getCorpId() + Global.mUser.getUuid(), sessions);
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                showShortToast("修改失败");
            }
        });
    }

    /**
     * 获取群组信息
     */
    private void getGroupInfo() {
        ProgressDialogHelper.show(mContext);
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取群组信息 + "?groupId=" + groupSession.getChatId();

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    mGroup = JsonUtils.jsonToEntity(JsonUtils.pareseData(response), GroupModel.class);
                    if (mGroup != null) {
                        initGroupData();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                ProgressDialogHelper.dismiss();
            }

            @Override
            public void onResponseCodeErro(String result) {
                ProgressDialogHelper.dismiss();
            }
        });
    }

    private void initGroupData() {
        isDepartment = mGroup.getIsDepartment() == 1;
        getGroupMembers();
        if (isDepartment) {
            llUpdateName.setEnabled(false);
            tvQuiteGroup.setVisibility(View.GONE);
            ivArrowName.setVisibility(View.GONE);
            tvDeptTag.setVisibility(View.VISIBLE);
        }
        if (mGroup.getIsTop() == 1) {
            isTop = true;
            switchTop.setChecked(true);
        } else {
            switchTop.setChecked(false);
        }
    }

    private void setGroupTop() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.置顶消息 + "?groupId=" + groupSession.getChatId();

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                switchTop.setEnabled(true);
                isTop = true;
                groupSession.setTop(isTop);
                updateLocalSessionList(groupSession);
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                switchTop.setEnabled(true);
            }

            @Override
            public void onResponseCodeErro(String result) {
                switchTop.setEnabled(true);
                isTop = true;
                groupSession.setTop(isTop);
                updateLocalSessionList(groupSession);
            }
        });
    }


    private void cancelGroupTop() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.取消置顶消息 + "?groupId=" + groupSession.getChatId();

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                switchTop.setEnabled(true);
                isTop = false;
                groupSession.setTop(isTop);
                updateLocalSessionList(groupSession);
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                switchTop.setEnabled(true);
            }

            @Override
            public void onResponseCodeErro(String result) {
                switchTop.setEnabled(true);
            }
        });
    }

    /**
     * 设置消息免打扰
     */
    private void closeMessageNotice() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.消息免打扰 + "?groupId=" + groupSession.getChatId();

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                switchNoInterrupt.setEnabled(true);
                setLocalMessageNotice(true);
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                switchNoInterrupt.setEnabled(true);
            }

            @Override
            public void onResponseCodeErro(String result) {
                switchNoInterrupt.setEnabled(true);
            }
        });
    }


    /**
     * 关闭消息免打扰
     */
    private void openMessageNotice() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.取消消息免打扰 + "?groupId=" + groupSession.getChatId();

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                switchNoInterrupt.setEnabled(true);
                setLocalMessageNotice(false);
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                switchNoInterrupt.setEnabled(true);
            }

            @Override
            public void onResponseCodeErro(String result) {
                switchNoInterrupt.setEnabled(true);
            }
        });
    }


    /**
     * 设置本地数据 群组为置顶/取消置顶
     *
     * @param session 要置顶/取消置顶的群组
     */
    private void updateLocalSessionList(GroupSession session) {
        List<GroupSession> topList = new ArrayList<>(); //取到置顶的数据
        List<GroupSession> unTopList = new ArrayList<>(); //取到未置顶的数据
        List<GroupSession> lists = preferencesHelper.getListData
                (Global.mUser.getCorpId() + Global.mUser.getUuid(), GroupSession.class);
        for (GroupSession list : lists) {
            if (list.isTop()) {
                topList.add(list);
            } else {
                unTopList.add(list);
            }
            if (session.getChatId().equals(list.getChatId())) {
                list.setTop(session.isTop());
            }
        }
        //将置顶的会话按照置顶时间排序
        sortListByTopTime(topList);
        //将未置顶的会话按照最后一条消息的发送时间排序
        sortListByLastSendTime(unTopList);
        lists.clear();
        lists.addAll(topList);
        lists.addAll(unTopList);
        preferencesHelper.putListData(Global.mUser.getCorpId() + Global.mUser.getUuid(), lists);
    }


    /**
     * 设置本地消息免打扰开关
     *
     * @param isSetNoInterrupt 是否免打扰
     */
    private void setLocalMessageNotice(boolean isSetNoInterrupt) {
        List<GroupSession> lists = preferencesHelper.getListData
                (Global.mUser.getCorpId() + Global.mUser.getUuid(), GroupSession.class);
        for (GroupSession list : lists) {
            if (list.getChatId().equals(groupSession.getChatId())) {
                list.setSetNoInterrupt(isSetNoInterrupt);
                break;
            }
        }
        preferencesHelper.putListData(Global.mUser.getCorpId() + Global.mUser.getUuid(), lists);
    }


    /**
     * 清空服务端聊天记录
     */
    private void clearMessage() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.清空聊天记录 + "?groupId=" + groupSession.getChatId();

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {

            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });
    }

    /**
     * 获取群组成员列表
     */
    private void getGroupMembers() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取群组成员 + "?groupId=" + groupSession.getChatId();

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                groupMembers = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), GroupMembers.class);

                if (!response.contains(Global.mUser.getUuid())) {
                    llUpdateName.setClickable(false);
                    llUpdateName.setEnabled(false);
                }

                if (groupMembers != null) {
                    for (GroupMembers groupMember : groupMembers) {
                        if (groupMember.getUuid().equals(mGroup.getGroupOwner())) {
                            groupMember.setManager(true);
                            break;
                        }
                    }
                    tvNumber.setText(groupMembers.size() + "人");
                    List<GroupMembers> showMemebers = new ArrayList<>();
                    if (groupMembers.size() >= 6) {
                        showMemebers.addAll(groupMembers.subList(0, 6));
                    } else {
                        showMemebers.addAll(groupMembers);
                    }


                    if (!isDepartment) { //部门聊天不显示 添加和删除群成员按钮
                        //群主显示群管理功能，并且可以添加和删除群成员
                        if (Global.mUser.getUuid().equals(mGroup.getGroupOwner())) {
                            rlGroupManage.setVisibility(View.VISIBLE);

                            GroupMembers addMember = new GroupMembers();
                            addMember.setType("1");
                            showMemebers.add(addMember); //添加成员按钮

                            GroupMembers deleteMember = new GroupMembers();
                            deleteMember.setType("2");
                            showMemebers.add(deleteMember); //删除成员按钮
                        } else if (mGroup.isManagerUpdateMemberOnly() == 0
                                && response.contains(Global.mUser.getUuid())) {
                            //如果 群组不只是群主可以添加和移除群成员 并且 群成员中包含当前员工才可以添加群成员
                            GroupMembers addMember = new GroupMembers();
                            addMember.setType("1");
                            showMemebers.add(addMember); //添加成员按钮
                        }
                    }
                    gridView.setAdapter(getMemberAdapter(showMemebers));
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                ProgressDialogHelper.dismiss();
            }

            @Override
            public void onResponseCodeErro(String result) {
                ProgressDialogHelper.dismiss();
            }
        });
    }


    /**
     * 删除群组中的成员
     *
     * @param groupId 要删除群组成员的群组id
     * @param staffId 在群组中要删除的成员的id
     */
    private void deleteMemberOfGroup(String groupId, String staffId) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.删除群组成员 +
                "?groupId=" + groupId + "&staffIds=" + staffId;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                showShortToast("移除群成员成功");
                getGroupMembers();
                EventBus.getDefault().postSticky("移除群成员成功");
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });

    }


    /**
     * 删除并退出群组
     *
     * @param groupId 要删除群组成员的群组id
     */
    private void quiteGroup(String groupId) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.删除并退出群组 +
                "?groupId=" + groupId;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                showShortToast("退出群聊成功");
//                getGroupMembers();
                removeCurrentConversition();
                EventBus.getDefault().postSticky("退出群聊成功");
                finish();
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });

    }


    /**
     * 添加群组成员
     *
     * @param staffId
     */
    private void addGroupMember(String staffId) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.新增群组成员 +
                "?groupId=" + groupSession.getChatId() + "&staffIds=" + staffId;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                showShortToast("添加成功");
                EventBus.getDefault().postSticky("添加群成员成功");
                getGroupMembers();
                /**
                 * 更新群组最后更新时间
                 */
                List<GroupSession> sessions = preferencesHelper.getListData(Global.mUser.getCorpId() + Global.mUser.getUuid(), GroupSession.class);
                for (GroupSession session : sessions) {
                    if (TextUtils.isEmpty(session.getChatId()))
                        continue;
                    if (session.getChatId().equals(groupSession.getChatId())) {
                        session.setLastUpdateTime(ViewHelper.getCurrentFullTime());
                        break;
                    }
                }
                preferencesHelper.putListData(Global.mUser.getCorpId() + Global.mUser.getUuid(), sessions);
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                showShortToast(JsonUtils.pareseMessage(result));
            }
        });
    }

    private CommanAdapter<GroupMembers> getMemberAdapter(List<GroupMembers> members) {
        return new CommanAdapter<GroupMembers>(members, mContext, R.layout.item_group_member) {
            @Override
            public void convert(int position, GroupMembers item, BoeryunViewHolder viewHolder) {
                if ("1".equals(item.getType())) {
                    viewHolder.setTextValue(R.id.tv_name, "");
                    viewHolder.setImageResoure(R.id.iv_head, R.drawable.ic_add);


                    /**
                     * 邀请新成员进入群组
                     */
                    viewHolder.getView(R.id.iv_head).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ComponentName comp = new ComponentName(mContext, "com.biaozhunyuan.tianyi.notice.SelectedNotifierActivity");
                            Intent intent = new Intent();
                            intent.putExtra("isSingleSelect", false);
                            intent.putExtra("unClickAbleUsers", (Serializable) turnToUserList(groupMembers));
                            intent.setComponent(comp);
                            intent.setAction("android.intent.action.VIEW");
                            startActivityForResult(intent, REQUEST_CODE_SELECT_MEMBER);
                        }
                    });
                } else if ("2".equals(item.getType())) {
                    viewHolder.setTextValue(R.id.tv_name, "");
                    viewHolder.setImageResoure(R.id.iv_head, R.drawable.ico_delete);

                    /**
                     * 移除成员
                     */
                    viewHolder.getView(R.id.iv_head).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, GroupMembersActivity.class);
                            intent.putExtra("isDeleteMembers", true);

                            //移除员工去掉管理员，不能移除管理员
                            List<GroupMembers> membersList = new ArrayList<>();
                            membersList.addAll(groupMembers);
                            for (int i = 0; i < membersList.size(); i++) {
                                GroupMembers members1 = membersList.get(i);
                                if (members1.isManager()) {
                                    membersList.remove(i);
                                    break;
                                }
                            }
                            intent.putExtra("groupMembers", (Serializable) membersList);
                            startActivityForResult(intent, REQUEST_CODE_DELETE_MEMBER);
                        }
                    });
                } else {
                    viewHolder.setTextValue(R.id.tv_name, item.getName());
                    viewHolder.setUserPhoto(R.id.iv_head, item.getUuid());
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_SELECT_MEMBER) {
                UserList userList = (UserList) data.getSerializableExtra("RESULT_SELECT_USER");
                String uuids = "";
                for (User members : userList.getUsers()) {
                    uuids += members.getUuid() + ",";
                }
                if (uuids.length() > 0) {
                    uuids = uuids.substring(0, uuids.length() - 1);
                }
                addGroupMember(uuids);
            } else if (requestCode == REQUEST_CODE_DELETE_MEMBER) {
                List<GroupMembers> list = (List<GroupMembers>) data.getSerializableExtra("deleteUsers");
                if (list != null) {
                    String uuids = "";
                    for (GroupMembers members : list) {
                        uuids += members.getUuid() + ",";
                    }
                    if (uuids.length() > 0) {
                        uuids = uuids.substring(0, uuids.length() - 1);
                        deleteMemberOfGroup(groupSession.getChatId(), uuids);
                    }
                }
            } else if (requestCode == REQUEST_CODE_UPDATE_NAME) {
                String name = data.getStringExtra("GroupName");
                if (!TextUtils.isEmpty(name)) {
                    updateGroupName(name);
                }
            }
        }
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


    /**
     * 根据置顶时间正序排列
     *
     * @param list
     */
    private void sortListByTopTime(List<GroupSession> list) {
        Collections.sort(list, new Comparator<GroupSession>() {
            @Override
            public int compare(GroupSession o1, GroupSession o2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date dt1 = format.parse(o1.getTopTime());
                    Date dt2 = format.parse(o2.getTopTime());
                    if (dt1.getTime() < dt2.getTime()) {
                        return -1;
                    } else if (dt1.getTime() > dt2.getTime()) {
                        return 1;
                    } else {
                        return 0;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }


    /**
     * 根据最后一条消息发送时间倒序排序
     *
     * @param list
     */
    private void sortListByLastSendTime(List<GroupSession> list) {
        Collections.sort(list, new Comparator<GroupSession>() {
            @Override
            public int compare(GroupSession o1, GroupSession o2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date dt1 = format.parse(o1.getLastMessageSendTime());
                    Date dt2 = format.parse(o2.getLastMessageSendTime());
                    if (dt1.getTime() > dt2.getTime()) {
                        return -1;
                    } else if (dt1.getTime() < dt2.getTime()) {
                        return 1;
                    } else {
                        return 0;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }


    /**
     * 从本地移除当前的会话
     */
    private void removeCurrentConversition() {
        List<GroupSession> sessions = preferencesHelper.getListData
                (Global.mUser.getCorpId() + Global.mUser.getUuid(), GroupSession.class);
        for (int i = 0; i < sessions.size(); i++) {
            GroupSession session = sessions.get(i);
            if (groupSession.getChatId().equals(session.getChatId())) {
                sessions.remove(session);
                break;
            }
        }
        preferencesHelper.putListData(Global.mUser.getCorpId() + Global.mUser.getUuid(), sessions);
    }
}
