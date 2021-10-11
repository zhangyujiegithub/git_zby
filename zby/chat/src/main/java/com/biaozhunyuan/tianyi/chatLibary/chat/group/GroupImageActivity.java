package com.biaozhunyuan.tianyi.chatLibary.chat.group;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;



import com.biaozhunyuan.tianyi.chatLibary.chat.model.GroupSession;
import com.biaozhunyuan.tianyi.common.attach.Attach;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.common.view.NoScrollGridView;
import com.example.chat.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * 群组查看图片记录页面
 */

public class GroupImageActivity extends BaseActivity {

    private NoScrollGridView gridView;
    private BoeryunHeaderView headerView;
    private SmartRefreshLayout refreshLayout;

    private Context mContext;
    private int pageIndex = 1;
    private Demand<Attach> demand;
    private GroupSession groupSession;
    private CommanAdapter<Attach> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_image);
        initView();
        getIntentData();
        initDemand();
        initData();
        getGroupImage();
        setOnEvent();
    }

    private void initDemand() {
        demand = new Demand<>();
        demand.src = Global.BASE_JAVA_URL + GlobalMethord.获取群组的图片记录;
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
                List<Attach> attaches = null;
                try {
                    attaches = JsonUtils.jsonToArrayEntity(JsonUtils.getStringValue
                            (JsonUtils.pareseData(response), "data"), Attach.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMore();
                if (attaches != null) {
                    if (demand.pageIndex == 1) {
                        adapter = getAdapter(attaches);
                        gridView.setAdapter(adapter);
                    } else {
                        adapter.addBottom(attaches, false);
                        adapter.notifyDataSetChanged();
                    }
                    pageIndex += 1;
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMore();
            }
        });
    }

    private void initView() {
        gridView = findViewById(R.id.gridView);
        headerView = findViewById(R.id.headerview);
        refreshLayout = findViewById(R.id.refresh_view);
    }

    private void getIntentData() {
        if (getIntent().getExtras() != null) {
            groupSession = (GroupSession) getIntent().getSerializableExtra("groupInfo");
        }
    }

    private void initData() {
        mContext = GroupImageActivity.this;
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

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<Attach> attachList = adapter.getDataList();
                ArrayList<String> attchIds = new ArrayList<>();
                for (Attach attach : attachList) {
                    attchIds.add(ImageUtils.getDownloadUrlById(attach.uuid));
                }
                ImageUtils.startImageBrower(mContext, position, attchIds);
            }
        });

        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                getGroupImage();
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                pageIndex = 1;
                getGroupImage();
            }
        });
    }


    private CommanAdapter<Attach> getAdapter(List<Attach> list) {
        return new CommanAdapter<Attach>(list, mContext, R.layout.item_group_image) {
            @Override
            public void convert(int position, Attach item, BoeryunViewHolder viewHolder) {
                ImageView iv = viewHolder.getView(R.id.iv);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(gridView.getColumnWidth(), gridView.getColumnWidth());
                iv.setLayoutParams(params);
                viewHolder.setImageById(item.uuid, R.id.iv);
            }
        };
    }
}
