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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.biaozhunyuan.tianyi.chatLibary.chat.group.GroupSearchActivity;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.GroupModel;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.GroupSession;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.UnreadMessage;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunSearchViewNoButton;
import com.example.chat.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;


/**
 * 所有的搜索结果页面
 */
public class AllSearchResultActivity extends BaseActivity {

    private BoeryunSearchViewNoButton searchView;
    private ListView lv;


    private Context mContext;
    private String type = ""; //类型：员工、群聊、群聊记录
    private String key = ""; //搜索的关键词

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        getIntentData();
        initViews();
        setOnEvent();
    }

    private void initViews() {
        mContext = AllSearchResultActivity.this;
        searchView = findViewById(R.id.search_view);
        lv = findViewById(R.id.lv);
        searchView.setSearchText("取消");
        searchView.setSearchGetFoucus();
        searchView.setSearchButtonVisable();
        searchView.geteText().setText(key);

        searchView.setOnSearchButtonListener(new BoeryunSearchViewNoButton.OnSearchButtonClickListener() {
            @Override
            public void OnClick() {
                finish();
            }
        });

        searchView.setOnSearchedListener(new BoeryunSearchViewNoButton.OnSearchedListener() {
            @Override
            public void OnSearched(String str) {
                key = str;
                if ("员工".equals(type)) {
                    getStaffByKey();
                } else if ("群聊".equals(type)) {
                    getGroupByKey();
                } else if ("群聊记录".equals(type)) {
                    getMessageByKey();
                }
            }
        });
    }

    private void getIntentData() {
        if (getIntent().getExtras() != null) {
            type = getIntent().getStringExtra("type");
            key = getIntent().getStringExtra("key");


            if ("员工".equals(type)) {
                getStaffByKey();
            } else if ("群聊".equals(type)) {
                getGroupByKey();
            } else if ("群聊记录".equals(type)) {
                getMessageByKey();
            }
        }
    }

    private void getStaffByKey() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.员工搜索 + key;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<User> users = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), User.class);
                lv.setAdapter(getStaffAdapter(users, key));

            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });
    }

    private void getGroupByKey() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.搜索全部群组;
        Map<String, String> map = new HashMap<>();
        map.put("keyword", key);

        StringRequest.postAsyn(url, map, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<GroupModel> users = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), GroupModel.class);
                lv.setAdapter(getGroupAdapter(users, key));
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });
    }

    private void getMessageByKey() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.搜索全部群聊记录;
        Map<String, String> map = new HashMap<>();
        map.put("keyword", key);

        StringRequest.postAsyn(url, map, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<UnreadMessage.UnreadMessageContent> users = JsonUtils.jsonToArrayEntity
                        (JsonUtils.pareseData(response), UnreadMessage.UnreadMessageContent.class);
                lv.setAdapter(getRecordAdapter(users, key));
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

                if ("员工".equals(type)) {
                    User user = (User) lv.getItemAtPosition(position);
                    ChartIntentUtils.startChatInfo(mContext, user.getUuid());
                } else if ("群聊".equals(type)) {
                    GroupModel item = (GroupModel) lv.getItemAtPosition(position);

                    GroupSession session = new GroupSession();
                    session.setChatId(item.getUuid());
                    session.setName(item.getName());
                    session.setAvatar(item.getIcon());
                    session.setDepartMent(item.getIsDepartment());
                    Intent intent = new Intent(mContext, ChatActivity.class);
                    intent.putExtra("chatUser", session);
                    startActivity(intent);
                } else if ("群聊记录".equals(type)) {
                    UnreadMessage.UnreadMessageContent content = (UnreadMessage.UnreadMessageContent) lv.getItemAtPosition(position);
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
    }

    private CommanAdapter<User> getStaffAdapter(List<User> list, final String str) {
        return new CommanAdapter<User>(list, mContext, R.layout.item_search_chat_staff) {
            @Override
            public void convert(int position, User item, BoeryunViewHolder viewHolder) {
                viewHolder.setUserPhoto(R.id.iv_head, item.getUuid());
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
                ImageUtils.displyImageById(item.getIcon(), (ImageView) viewHolder.getView(R.id.iv_head));
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


}
