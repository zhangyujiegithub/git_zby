package com.biaozhunyuan.tianyi.chatLibary.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;


import com.biaozhunyuan.tianyi.chatLibary.chat.model.ChatMessage;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.GroupSession;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.helper.SharedPreferencesHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.view.AlertDialog;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.example.chat.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


/**
 * 转发消息页面
 */
public class ForwardMessageActivity extends BaseActivity {

    private ListView lv;
    private BoeryunHeaderView headerView;
    private RelativeLayout rlSearch;

    private Context mContext;
    private SharedPreferencesHelper helper;
    private List<GroupSession> list = new ArrayList<>();
    private NewsAdapter adapter;
    private ChatMessage chatMessage;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forward_message);
        getIntentData();
        initViews();
        initData();
        setOnEvent();
    }

    private void getIntentData() {
        if (getIntent().getExtras() != null) {
            chatMessage = (ChatMessage) getIntent().getSerializableExtra("ChatMessage");
        }
    }


    private void initViews() {
        lv = findViewById(R.id.lv_conversation_list);
        headerView = findViewById(R.id.headerview);
        rlSearch = findViewById(R.id.rl_search_ico);
    }

    private void initData() {
        mContext = ForwardMessageActivity.this;
        helper = new SharedPreferencesHelper(mContext);
        dialog = new AlertDialog(mContext)
                .builder()
                .setTitle("发送给")
                .setTitleGravity(Gravity.LEFT)
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

        List<GroupSession> groupSessions = helper.getListData(Global.mUser.getCorpId() + Global.mUser.getUuid(), GroupSession.class);
        Collections.reverse(groupSessions);
        //只显示 没有退出的群聊
        for (int i = 0; i < groupSessions.size(); i++) {
            if (!groupSessions.get(i).isQuite()) {
                list.add(groupSessions.get(i));
            }
        }
        adapter = new NewsAdapter(mContext, list);
        lv.setAdapter(adapter);
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

        rlSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SearchAllActivity.class);
                intent.putExtra("ForwardMessage", chatMessage);
                startActivity(intent);
            }
        });

        adapter.setOnItemClickListener(new NewsAdapter.ItemClickListener() {
            @Override
            public void onItemClick(final GroupSession session) {
                dialog.setMsg(session.getName())
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                chatMessage.setChatId(session.getChatId());
                                chatMessage.setFrom(Global.mUser.getUuid() + "@" + Global.mUser.getCorpId() + "/");
                                chatMessage.setSendTime(ViewHelper.getCurrentFullTime());
                                HashMap<String, String> keyValues = chatMessage.getKeyValues();
                                if (keyValues != null) {
                                    keyValues.put("name", session.getName());
                                    keyValues.put("isSingle", session.isSingle() + "");
                                    keyValues.put("members", session.getMembers());
                                    keyValues.put("isDepartment", session.isDepartMent() + "");
                                }
                                switch (chatMessage.getChatCategory()) {
                                    case ChatMessage.CHAT_Left:
                                        chatMessage.setChatCategory(ChatMessage.CHAT_RIGHT);
                                        break;
                                    case ChatMessage.CHAT_LEFT_AUDIO:
                                        chatMessage.setChatCategory(ChatMessage.CHAT_RIGHT_AUDIO);
                                        break;
                                    case ChatMessage.CHAT_LEFT_FILE:
                                        chatMessage.setChatCategory(ChatMessage.CHAT_RIGHT_FILE);
                                        break;
                                }
                                if (ChartIntentUtils.socketService != null) {
                                    ChartIntentUtils.socketService.sendMessage(chatMessage);
                                }
                                MsgCacheManager.saveMessage(mContext, chatMessage); //保存消息
                                SessionCacheManger.saveSession(mContext, session.getChatId(), chatMessage); //保存会话
                                EventBus.getDefault().postSticky("505");
                                showShortToast("转发成功");
                                finish();
                            }
                        }).show();
            }
        });
    }
}
