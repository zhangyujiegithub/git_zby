package com.biaozhunyuan.tianyi.chatLibary.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.biaozhunyuan.tianyi.chatLibary.chat.group.GroupSearchActivity;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.ChatMessage;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.GroupModel;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.GroupSession;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.SearchResultModel;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.UnreadMessage;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.base.ParseException;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.AlertDialog;
import com.biaozhunyuan.tianyi.common.view.BoeryunSearchViewNoButton;
import com.biaozhunyuan.tianyi.common.view.CircleImageView;
import com.biaozhunyuan.tianyi.common.view.NoScrollListView;
import com.example.chat.R;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * 聊天列表搜索页面，可以搜索人员、群聊、聊天记录
 */
public class SearchAllActivity extends BaseActivity {

    private BoeryunSearchViewNoButton searchView;
    private LinearLayout llUser;
    private LinearLayout llGroup;
    private LinearLayout llRecord;

    private NoScrollListView lvUser;
    private NoScrollListView lvGroup;
    private NoScrollListView lvRecord;

    private LinearLayout llUserMore;
    private LinearLayout llGroupMore;
    private LinearLayout llRecordMore;

    private TextView tvHint;


    private Context mContext;
    private String key;
    private DictionaryHelper helper;
    private CommanAdapter<SearchResultModel.Staff> staffAdapter;
    private CommanAdapter<GroupModel> groupAdapter;
    private CommanAdapter<UnreadMessage.UnreadMessageContent> recordAdapter;
    private AlertDialog dialog;
    private ChatMessage forwardMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_all);
        intiViews();
        getIntentExtra();
        setOnEvent();
    }

    private void intiViews() {
        mContext = SearchAllActivity.this;
        helper = new DictionaryHelper(mContext);
        searchView = findViewById(R.id.search_view);
        searchView.setHint("搜索");
        searchView.setSearchText("取消");
        searchView.setSearchButtonVisable();

        llUser = findViewById(R.id.ll_user);
        llGroup = findViewById(R.id.ll_Group);
        llRecord = findViewById(R.id.ll_record);

        lvUser = findViewById(R.id.lv_user);
        lvGroup = findViewById(R.id.lv_group);
        lvRecord = findViewById(R.id.lv_record);

        llUserMore = findViewById(R.id.ll_user_more);
        llGroupMore = findViewById(R.id.ll_group_more);
        llRecordMore = findViewById(R.id.ll_record_more);
        tvHint = findViewById(R.id.tv_hint);
    }

    private void getIntentExtra() {
        if (getIntent().getExtras() != null) {
            forwardMessage = (ChatMessage) getIntent().getSerializableExtra("ForwardMessage");
            if (forwardMessage != null) {
                initDialog();
            }
        }
    }

    private void initDialog() {
        dialog = new AlertDialog(mContext)
                .builder()
                .setTitle("发送给")
                .setTitleGravity(Gravity.LEFT)
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
    }


    private void setOnEvent() {
        searchView.setOnSearchButtonListener(new BoeryunSearchViewNoButton.OnSearchButtonClickListener() {
            @Override
            public void OnClick() {
                finish();
            }
        });

        searchView.setOnSearchedListener(new BoeryunSearchViewNoButton.OnSearchedListener() {
            @Override
            public void OnSearched(String str) {
                if (!TextUtils.isEmpty(str)) {
                    getMessageRecordByWord(str);
                } else {
                    tvHint.setVisibility(View.VISIBLE);
                    llUser.setVisibility(View.GONE);
                    llGroup.setVisibility(View.GONE);
                    llRecord.setVisibility(View.GONE);
                }
            }
        });

        lvUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchResultModel.Staff user = (SearchResultModel.Staff) lvUser.getItemAtPosition(position);
                if (forwardMessage != null) {
                    getChatId(user.uuid);
                } else {
                    ChartIntentUtils.startChatInfo(mContext, user.uuid);
                }
            }
        });

        lvGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GroupModel item = (GroupModel) lvGroup.getItemAtPosition(position);
                if (forwardMessage != null) {
                    GroupSession session = new GroupSession();
                    session.setChatId(item.getUuid());
                    session.setName(item.getName());
                    session.setSingle(Integer.parseInt(item.getIsSingle()));
                    session.setDepartMent(item.getIsDepartment());
                    forwradMessage(session);
                } else {

                    GroupSession session = new GroupSession();
                    session.setChatId(item.getUuid());
                    session.setName(item.getName());
                    session.setAvatar(item.getIcon());
                    session.setDepartMent(item.getIsDepartment());
                    Intent intent = new Intent(mContext, ChatActivity.class);
                    intent.putExtra("chatUser", session);
                    startActivity(intent);
                }
            }
        });

        lvRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UnreadMessage.UnreadMessageContent content = (UnreadMessage.UnreadMessageContent) lvRecord.getItemAtPosition(position);
                if (forwardMessage != null) {
                    GroupSession session = new GroupSession();
                    session.setChatId(content.getUuid());
                    session.setName(content.getName());
                    session.setSingle(Integer.parseInt(content.getIsSingle()));
                    session.setDepartMent(content.getIsDepartment());
                    forwradMessage(session);
                } else {
                    if (content.getMessageCount() > 1) {
                        Intent intent = new Intent(mContext, GroupSearchActivity.class);
                        GroupSession session = new GroupSession();
                        session.setChatId(content.getUuid());
                        session.setLastMessageId(content.getMessageId());
                        intent.putExtra("groupInfo", session);
                        intent.putExtra("keyword", key);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(mContext, MessageRecordActivity.class);
                        GroupSession session = new GroupSession();
                        session.setChatId(content.getUuid());
                        session.setName(content.getName());
                        session.setLastMessageId(content.getMessageId());
                        intent.putExtra("ChatInfo", session);
                        startActivity(intent);
                    }
                }

            }
        });

        llUserMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AllSearchResultActivity.class);
                intent.putExtra("type", "员工");
                intent.putExtra("key", key);
                startActivity(intent);
            }
        });

        llGroupMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AllSearchResultActivity.class);
                intent.putExtra("type", "群聊");
                intent.putExtra("key", key);
                startActivity(intent);
            }
        });

        llRecordMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AllSearchResultActivity.class);
                intent.putExtra("type", "群聊记录");
                intent.putExtra("key", key);
                startActivity(intent);
            }
        });
    }


    /**
     * 搜索全部聊天记录
     *
     * @param str 关键字
     */
    private void getMessageRecordByWord(final String str) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.搜索全部聊天记录;
        key = str;
        Map<String, String> map = new HashMap<>();
        map.put("keyword", str);

        StringRequest.postAsyn(url, map, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                tvHint.setVisibility(View.GONE);
                if (staffAdapter != null) {
                    staffAdapter.clearData();
                }
                if (groupAdapter != null) {
                    groupAdapter.clearData();
                }
                if (recordAdapter != null) {
                    recordAdapter.clearData();
                }
                llUser.setVisibility(View.GONE);
                llGroup.setVisibility(View.GONE);
                llRecord.setVisibility(View.GONE);
                llUserMore.setVisibility(View.GONE);
                llRecordMore.setVisibility(View.GONE);
                llGroupMore.setVisibility(View.GONE);
                try {
                    SearchResultModel searchModel = JsonUtils.jsonToEntity(JsonUtils.pareseData(response), SearchResultModel.class);
                    if (searchModel != null) {
                        List<SearchResultModel.Staff> staffList = searchModel.getStaffList();
                        if (staffList.size() > 0) {
                            llUser.setVisibility(View.VISIBLE);
                            if (staffList.size() > 3) {
                                llUserMore.setVisibility(View.VISIBLE);
                                staffList = staffList.subList(0, 3);
                            } else {
                                llUserMore.setVisibility(View.GONE);
                            }
                            staffAdapter = getStaffAdapter(staffList, str);
                            lvUser.setAdapter(staffAdapter);
                        }
                        List<GroupModel> groupList = searchModel.getGroupNameIncludeKeyList();
                        if (groupList.size() > 0) {
                            llGroup.setVisibility(View.VISIBLE);
                            if (groupList.size() > 3) {
                                llGroupMore.setVisibility(View.VISIBLE);
                                groupList = groupList.subList(0, 3);
                            } else {
                                llGroupMore.setVisibility(View.GONE);
                            }
                            groupAdapter = getGroupAdapter(groupList, str);
                            lvGroup.setAdapter(groupAdapter);
                        }
                        List<UnreadMessage.UnreadMessageContent> messageList = searchModel.getMessageIncludeKeyList();
                        if (messageList.size() > 0) {
                            llRecord.setVisibility(View.VISIBLE);
                            if (messageList.size() > 3) {
                                llRecordMore.setVisibility(View.VISIBLE);
                                messageList = messageList.subList(0, 3);
                            } else {
                                llRecordMore.setVisibility(View.GONE);
                            }
                            recordAdapter = getRecordAdapter(messageList, str);
                            lvRecord.setAdapter(recordAdapter);
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
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

    private CommanAdapter<SearchResultModel.Staff> getStaffAdapter(List<SearchResultModel.Staff> list, final String str) {
        return new CommanAdapter<SearchResultModel.Staff>(list, mContext, R.layout.item_search_chat_staff) {
            @Override
            public void convert(int position, SearchResultModel.Staff item, BoeryunViewHolder viewHolder) {
                viewHolder.setUserPhoto(R.id.iv_head, item.uuid);
                String name = item.name;
                SpannableStringBuilder spannable = getSpannableStringBuilder(name, str);
                TextView tvContent = viewHolder.getView(R.id.tv_name);
                tvContent.setText(spannable);
            }
        };
    }

    @NonNull
    private SpannableStringBuilder getSpannableStringBuilder(String name, String str) {
        int startIndex;
        if (TextUtils.isEmpty(name)) {
            startIndex = 0;
            name = "";
            str = "";
        } else {
            startIndex = name.indexOf(str);
        }
        if (startIndex == -1) {
            startIndex = 0;
        }
        int endIndex;
        if (!name.contains(str)) {
            endIndex = 0;
        } else {
            endIndex = startIndex + str.length();
        }
        SpannableStringBuilder spannable = new SpannableStringBuilder(name);
        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#2D7FC7")),
                startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }


    private CommanAdapter<GroupModel> getGroupAdapter(List<GroupModel> list, final String key) {
        return new CommanAdapter<GroupModel>(list, mContext, R.layout.item_search_group_chat) {
            @Override
            public void convert(int position, GroupModel item, BoeryunViewHolder viewHolder) {
                CircleImageView ivHead = viewHolder.getView(R.id.iv_head);
                ImageUtils.displyImageById(item.getIcon(), ivHead);
                TextView tvGroupName = viewHolder.getView(R.id.tv_group_name);
                tvGroupName.setText(getSpannableStringBuilder(item.getName(), key));
                TextView tvRecord = viewHolder.getView(R.id.tv_chat_record);
                tvRecord.setText(getSpannableStringBuilder("成员:" + item.getStaffName(), key));
            }
        };
    }


    private CommanAdapter<UnreadMessage.UnreadMessageContent> getRecordAdapter(List<UnreadMessage.UnreadMessageContent> list, final String key) {
        return new CommanAdapter<UnreadMessage.UnreadMessageContent>(list, mContext, R.layout.item_search_group_chat) {
            @Override
            public void convert(int position, UnreadMessage.UnreadMessageContent item, BoeryunViewHolder viewHolder) {
                viewHolder.setUserPhoto(R.id.iv_head, item.getCreator());
                TextView tvGroupName = viewHolder.getView(R.id.tv_group_name);
                tvGroupName.setText(item.getName());
                TextView tvRecord = viewHolder.getView(R.id.tv_chat_record);
                if (item.getMessageCount() == 1) {
                    tvRecord.setText(getSpannableStringBuilder(item.getContent(), key));
                } else {
                    tvRecord.setText(item.getMessageCount() + "条相关记录");
                }
            }
        };
    }


    private void forwradMessage(final GroupSession session) {
        {
            dialog.setMsg(session.getName())
                    .setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            forwardMessage.setChatId(session.getChatId());
                            forwardMessage.setFrom(Global.mUser.getUuid() + "@" + Global.mUser.getCorpId() + "/");
                            forwardMessage.setSendTime(ViewHelper.getCurrentFullTime());
                            HashMap<String, String> keyValues = forwardMessage.getKeyValues();
                            if (keyValues != null) {
                                keyValues.put("name", session.getName());
                                keyValues.put("isSingle", session.isSingle() + "");
                                keyValues.put("members", session.getMembers());
                                keyValues.put("isDepartment", session.isDepartMent() + "");
                            }
                            switch (forwardMessage.getChatCategory()) {
                                case ChatMessage.CHAT_Left:
                                    forwardMessage.setChatCategory(ChatMessage.CHAT_RIGHT);
                                    break;
                                case ChatMessage.CHAT_LEFT_AUDIO:
                                    forwardMessage.setChatCategory(ChatMessage.CHAT_RIGHT_AUDIO);
                                    break;
                                case ChatMessage.CHAT_LEFT_FILE:
                                    forwardMessage.setChatCategory(ChatMessage.CHAT_RIGHT_FILE);
                                    break;
                            }
                            if (ChartIntentUtils.socketService != null) {
                                ChartIntentUtils.socketService.sendMessage(forwardMessage);
                            }
                            MsgCacheManager.saveMessage(mContext, forwardMessage); //保存消息
                            SessionCacheManger.saveSession(mContext, session.getChatId(), forwardMessage); //保存会话
                            EventBus.getDefault().postSticky("505");
                            showShortToast("转发成功");
                            finish();
                        }
                    }).show();
        }
    }

    private void getChatId(String userId) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.新增会话 + "?groupName="
                + helper.getUserNameById(userId) + "&staffIds=" + userId;
        final String userUuids = userId;
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    GroupSession session = JsonUtils.jsonToEntity(JsonUtils.pareseData(response), GroupSession.class);
                    if (session != null) {
                        session.setChatId(session.getUuid());
                        session.setAvatar(userUuids);
                        session.setMembers(userUuids + "," + Global.mUser.getUuid());  //设置群成员
                        session.setName(helper.getUserNameById(userUuids));
                        session.setSingle(1);

                        forwradMessage(session);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                Toast.makeText(mContext, JsonUtils.pareseMessage(result), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
