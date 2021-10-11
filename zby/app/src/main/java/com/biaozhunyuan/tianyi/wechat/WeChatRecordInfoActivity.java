package com.biaozhunyuan.tianyi.wechat;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.wechat.model.WeChatMsg;

import org.json.JSONException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * 作者： bohr
 * 日期： 2020-06-15 15:21
 * 描述： 微信聊天消息详情页面
 */
public class WeChatRecordInfoActivity extends BaseActivity {

    private ImageView ivBack;
    private TextView tvTitle;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView rv;

    private Context mContext;
    private String weChatId;
    private String staffId;
    private String weChatName;
    private Demand demand;
    private int pageIndex = 1;
    private WeChatInfoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_record);
        initViews();
        initData();
        initdemand();
        getMsgList();
        setOnEvent();
    }

    private void initViews() {
        ivBack = findViewById(R.id.iv_back_chat);
        tvTitle = findViewById(R.id.tv_name_chat);
        refreshLayout = findViewById(R.id.swipe_layout);
        rv = findViewById(R.id.recylerView);
    }

    private void initData() {
        getIntentData();
        mContext = WeChatRecordInfoActivity.this;
        tvTitle.setText(weChatName);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new WeChatInfoAdapter(null);
        rv.setAdapter(adapter);
    }

    private void initdemand() {
        demand = new Demand();
        demand.pageIndex = pageIndex;
        demand.pageSize = 10;
        Map<String, String> map = new HashMap<>();
        map.put("staffId", staffId);
        map.put("wechatId", weChatId);
        map.put("isRoomMsg", weChatId.contains("@chatroom") + "");
        demand.keyMap = map;
        String url = Global.BASE_JAVA_URL + GlobalMethord.微信聊天消息;
        demand.src = url;
    }

    private void getIntentData() {
        if (getIntent().getExtras() != null) {
            weChatId = getIntent().getStringExtra("WeChatId");
            weChatName = getIntent().getStringExtra("WeChatName");
            staffId = getIntent().getStringExtra("StaffId");
        }
    }

    private void setOnEvent() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMsgList();
            }
        });
    }

    private void getMsgList() {
        if (pageIndex == 1) {
            refreshLayout.setRefreshing(true);
        }

        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<WeChatMsg> list = null;
                try {
                    list = JsonUtils.jsonToArrayEntity(JsonUtils.getStringValue
                            (JsonUtils.pareseData(response), "data"), WeChatMsg.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Collections.reverse(list);
                refreshLayout.setRefreshing(false);
                setData(pageIndex == 1, list);
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });
    }

    private void setData(boolean isRefresh, List data) {
        pageIndex++;
        final int size = data == null ? 0 : data.size();
        if (isRefresh) {
            adapter.setNewData(data);
            rv.scrollToPosition(adapter.getItemCount() - 1); //滑动到底部
        } else {
            if (size > 0) {
                adapter.addData(0, data);
            }
        }
        if (size < 20) {
            //第一页如果不够一页就不显示没有更多数据布局
            adapter.loadMoreEnd();
        } else {
            adapter.loadMoreComplete();
        }
    }
}
