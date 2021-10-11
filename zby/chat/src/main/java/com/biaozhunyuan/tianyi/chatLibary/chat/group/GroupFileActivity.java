package com.biaozhunyuan.tianyi.chatLibary.chat.group;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;


import com.biaozhunyuan.tianyi.chatLibary.chat.model.GroupSession;
import com.biaozhunyuan.tianyi.common.attach.Attach;
import com.biaozhunyuan.tianyi.common.attach.DownloadAdapter;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;
import com.example.chat.R;

import org.json.JSONException;

import java.util.List;

import okhttp3.Request;


/**
 * 群组查看图片记录页面
 */

public class GroupFileActivity extends BaseActivity {

    private PullToRefreshAndLoadMoreListView lv;
    private BoeryunHeaderView headerView;

    private Context mContext;
    private int pageIndex = 1;
    private Demand<Attach> demand;
    private GroupSession groupSession;
    private DownloadAdapter adapter;
    private List<Attach> attaches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_group_file);
        initView();
        getIntentData();
        initDemand();
        initData();
        getGroupImage();
        setOnEvent();
    }

    private void initDemand() {
        demand = new Demand<>(Attach.class);
        demand.src = Global.BASE_JAVA_URL + GlobalMethord.获取群组的文件记录;
        demand.pageIndex = pageIndex;
        demand.pageSize = 100;
        demand.sort = "createTime desc";
        demand.key = "groupId";
        demand.value = groupSession.getChatId();
    }

    private void getGroupImage() {
        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                attaches = null;
                try {
                    attaches = JsonUtils.jsonToArrayEntity(JsonUtils.getStringValue
                            (JsonUtils.pareseData(response), "data"), Attach.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (attaches != null) {

                    if (demand.pageIndex == 1) {
                        lv.onRefreshComplete();
                        adapter = new DownloadAdapter(mContext, attaches, lv);
                        adapter.setHeaderCount(1);
                        lv.setAdapter(adapter);
                    } else {
                        adapter.addBottom(attaches);
                        lv.loadCompleted();
                    }
                    pageIndex += 1;
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

    private void initView() {
        lv = findViewById(R.id.lv);
        headerView = findViewById(R.id.headerview);
    }

    private void getIntentData() {
        if (getIntent().getExtras() != null) {
            groupSession = (GroupSession) getIntent().getSerializableExtra("groupInfo");
        }
    }

    private void initData() {
        mContext = GroupFileActivity.this;
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

        lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                getGroupImage();
            }
        });

        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getGroupImage();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Attach attach = (Attach) lv.getItemAtPosition(position);
                if (checkEndsWithInStringArray(attach.filename, mContext.getResources()
                        .getStringArray(R.array.fileEndingImage))) {  //如果附件是图片类型，直接显示
                    ImageUtils.startSingleImageBrower(mContext, ImageUtils.getDownloadUrlById(attach.uuid));
                } else if (checkEndsWithInStringArray(attach.filename, mContext.getResources()
                        .getStringArray(R.array.fileEndingPdf))) { //pdf格式
                    ComponentName comp = new ComponentName(mContext, "com.biaozhunyuan.tianyi.attch.PdfActivity");
                    Intent intent = new Intent();
                    intent.setComponent(comp);
                    intent.putExtra("filepath", ImageUtils.getDownloadUrlById(attach.uuid));
                    intent.putExtra("title", attach.filename);
                    startActivity(intent);
                }
            }
        });
    }

    // 定义用于检查要打开的附件文件的后缀是否在遍历后缀数组中
    private boolean checkEndsWithInStringArray(String checkItsEnd,
                                               String[] fileEndings) {
        for (String aEnd : fileEndings) {
            if (checkItsEnd.toLowerCase().endsWith(aEnd.toLowerCase()))
                return true;
        }
        return false;
    }
}
