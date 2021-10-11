package com.biaozhunyuan.tianyi.chatLibary.chat.group;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import com.biaozhunyuan.tianyi.chatLibary.chat.MessageRecordActivity;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.GroupSession;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.UnreadMessage;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DateDeserializer;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunSearchViewNoButton;
import com.example.chat.R;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * 群组搜索聊天记录列表
 */
public class GroupSearchActivity extends BaseActivity {


    private BoeryunSearchViewNoButton searchView;
    private ListView lv;


    private Context mContext;
    private DictionaryHelper helper;
    private GroupSession groupSession;
    private String keyword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_search);
        initViews();
        initData();
        setOnEvent();
    }

    private void initData() {
        mContext = GroupSearchActivity.this;
        helper = new DictionaryHelper(mContext);
        if (getIntent().getExtras() != null) {
            groupSession = (GroupSession) getIntent().getSerializableExtra("groupInfo");
            keyword = getIntent().getStringExtra("keyword");
            if (!TextUtils.isEmpty(keyword)) {
                searchView.geteText().setText(keyword);
                getRecordByKeyWord(keyword);
            }
        }
    }

    private void initViews() {
        searchView = findViewById(R.id.search_view);
        lv = findViewById(R.id.lv);
        searchView.setSearchGetFoucus();
        searchView.setSearchButtonVisable();
        searchView.setSearchText("取消");
    }

    private void setOnEvent() {
        searchView.setOnSearchedListener(new BoeryunSearchViewNoButton.OnSearchedListener() {
            @Override
            public void OnSearched(String str) {
                if (!TextUtils.isEmpty(str)) {
                    getRecordByKeyWord(str);
                }

            }
        });

        searchView.setOnSearchButtonListener(new BoeryunSearchViewNoButton.OnSearchButtonClickListener() {
            @Override
            public void OnClick() {
                finish();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UnreadMessage.UnreadMessageContent content = (UnreadMessage.UnreadMessageContent) lv.getItemAtPosition(position);
                Intent intent = new Intent(mContext, MessageRecordActivity.class);
                GroupSession session = new GroupSession();
                session.setChatId(content.getGroupId());
                try {
                    session.setName(JsonUtils.getStringValue(content.getKeyValues(),"name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                session.setLastMessageId(content.getUuid());
                intent.putExtra("ChatInfo", session);
                startActivity(intent);
            }
        });
    }

    private void getRecordByKeyWord(final String key) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.搜索群聊天记录;
        Map<String, String> map = new HashMap<>();
        map.put("groupId", groupSession.getChatId());
        map.put("keyword", key);
        map.put("sort", "createTime desc");

        StringRequest.postAsyn(url, map, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    List<UnreadMessage.UnreadMessageContent> messages = JsonUtils.jsonToArrayEntity(JsonUtils.getStringValue
                            (JsonUtils.pareseData(response), "data"), UnreadMessage.UnreadMessageContent.class);
                    lv.setAdapter(getAdapter(messages, key));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    private CommanAdapter<UnreadMessage.UnreadMessageContent> getAdapter
            (List<UnreadMessage.UnreadMessageContent> list, final String key) {
        return new CommanAdapter<UnreadMessage.UnreadMessageContent>(list, mContext, R.layout.item_group_search) {
            @Override
            public void convert(int position, UnreadMessage.UnreadMessageContent item, BoeryunViewHolder viewHolder) {
                viewHolder.setUserPhoto(R.id.iv_head, item.getFromId());
                viewHolder.setTextValue(R.id.tv_name, helper.getUserNameById(item.getFromId()));
                viewHolder.setTextValue(R.id.tv_time, DateDeserializer.LongFormatTime(item.getCreateTime()));


                int startIndex = item.getContent().indexOf(key);
                if (startIndex == -1) {
                    startIndex = 0;
                }
                int endIndex = startIndex + key.length();
                SpannableStringBuilder spannable = new SpannableStringBuilder(item.getContent());
                spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#2D7FC7")),
                        startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                TextView tvContent = viewHolder.getView(R.id.tv_content);
                tvContent.setText(spannable);
            }
        };
    }
}
