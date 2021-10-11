package com.biaozhunyuan.tianyi.chatLibary.chat;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.chatLibary.chat.model.ChatMessage;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.Command;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.GroupSession;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.HistoryMessage;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.example.chat.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Request;

/**
 * Created by wangAnMin on 2019/3/13.
 * <p>
 * 聊天记录页面，默认展示十条，上拉加载十条数据
 */

public class MessageRecordActivity extends BaseActivity {

    private Context mContext;
    private Demand<HistoryMessage> demand;
    private List<ChatMessage> historyMessages = new ArrayList<>();
    private ChatAdapter adapter;
    private GroupSession groupSession;
    private int pageIndex = 1;

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private ImageView ivBack;
    private TextView tvTittle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_record);
        initData();
        initViews();
        getIntentData();
    }

    private void initData() {
        mContext = MessageRecordActivity.this;
        demand = new Demand<>(HistoryMessage.class);
        demand.sort = "desc";
        demand.sortField = "createTime";
        demand.pageIndex = pageIndex;
        demand.pageSize = 10;
    }

    private void getIntentData() {
        if (getIntent().getExtras() != null) {
            groupSession = (GroupSession) getIntent().getSerializableExtra("ChatInfo");
            if (groupSession != null) {
                tvTittle.setText(groupSession.getName());
                getHistoryMessage();
            }
        }
    }


    private void initViews() {
        refreshLayout = findViewById(R.id.swipe_layout);
        recyclerView = findViewById(R.id.recylerView);
        ivBack = findViewById(R.id.iv_back_chat);
        tvTittle = findViewById(R.id.tv_name_chat);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex += 1;
                getHistoryMessage();
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /**
     * 获取历史消息
     */
    private void getHistoryMessage() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取消息记录 + "?groupId=" + groupSession.getChatId();
        demand.src = url;
        refreshLayout.setRefreshing(true);
        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                refreshLayout.setRefreshing(false);
                if (demand.data != null) {
                    if (demand.data.size() > 0) {
                        List<ChatMessage> messages = new ArrayList<>();
                        for (HistoryMessage historyMessage : demand.data) {
                            ChatMessage message = new ChatMessage();
                            message.setChatId(historyMessage.getGroupId());
                            message.setCreateTime(historyMessage.getCreateTime());
                            message.setFrom(historyMessage.getFromId() + "@" + Global.mUser.getCorpId() + "/");
                            message.setBody(historyMessage.getContent());
                            message.setFormat(historyMessage.getType());
                            /*String keyValues = historyMessage.getKeyValues();
                            if (!TextUtils.isEmpty(keyValues)) {
                                Gson gson = new Gson();
                                HashMap<String, String> map = new HashMap<String, String>();
                                map = gson.fromJson(keyValues, map.getClass());
                                message.setKeyValues(map);
                            }*/
                            message.setKeyValues(historyMessage.getKeyValue());
                            message.setCmd(Command.chat);
                            if (Global.mUser.getUuid().equals(historyMessage.getFromId())) {
                                if (ChatMessage.FORMAT_FILE.equals(historyMessage.getType())) {
                                    message.setChatCategory(ChatMessage.CHAT_RIGHT_FILE);
                                } else if (ChatMessage.FORMAT_VOICE.equals(historyMessage.getType())) {
                                    message.setChatCategory(ChatMessage.CHAT_RIGHT_AUDIO);
                                } else {
                                    message.setChatCategory(ChatMessage.CHAT_RIGHT);
                                }
                            } else {
                                if (ChatMessage.FORMAT_FILE.equals(historyMessage.getType())) {
                                    message.setChatCategory(ChatMessage.CHAT_LEFT_FILE);
                                } else if (ChatMessage.FORMAT_VOICE.equals(historyMessage.getType())) {
                                    message.setChatCategory(ChatMessage.CHAT_LEFT_AUDIO);
                                } else {
                                    message.setChatCategory(ChatMessage.CHAT_Left);
                                }
                            }
                            messages.add(message);
                        }
                        Collections.reverse(messages);
                        historyMessages.addAll(0, messages);
                        if (adapter == null) {
                            adapter = new ChatAdapter(MessageRecordActivity.this, historyMessages);
                            recyclerView.setAdapter(adapter);
                            recyclerView.scrollToPosition(adapter.getItemCount() - 1); //滑动到底部
                        } else {
                            adapter.notifyDataSetChanged();
                        }

                    }
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {

                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onResponseCodeErro(String result) {

                refreshLayout.setRefreshing(false);
            }
        });
    }
}
